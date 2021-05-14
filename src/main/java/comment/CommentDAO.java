package comment;

import persistence.Specification;
import persistence.StatementSetters;
import util.Pair;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommentDAO {
    private static final CommentMapper cm = new CommentMapper();
    private final Connection con;

    public CommentDAO(Connection con) {
        this.con = con;
    }

    public List<Comment> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM v_comment AS comment %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                .executeQuery();
        List<Comment> comments = new ArrayList<>();
        while(rs.next()){
            comments.add(cm.toBean(rs));
        }
        ps.close();
        rs.close();
        return comments;
    }

    public int update(Comment comment) throws SQLException {
        String statement = "UPDATE comment SET %s WHERE id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        if(comment.getText() != null){
            params.add(new Pair<>(comment.getText(), Types.VARCHAR));
            valuesToSet.add("content=?");
        }
        if(comment.getPost() != null && comment.getPost().getId() != null){
            params.add(new Pair<>(comment.getPost().getId(), Types.INTEGER));
            valuesToSet.add("post_id=?");
        }
        if(comment.getAuthor() != null && comment.getAuthor().getId() != null){
            params.add(new Pair<>(comment.getAuthor().getId(), Types.VARCHAR));
            valuesToSet.add("author_id=?");
        }
        if(comment.getParentComment() != null && comment.getParentComment().getId() != null){
            params.add(new Pair<>(comment.getParentComment().getId(), Types.VARCHAR));
            valuesToSet.add("parent_comment_id=?");
        }
        params.add(new Pair<>(comment.getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public List<Integer> insert(List<Comment> comments) throws SQLException {
        String statement = "INSERT INTO comment (%s) VALUES %s";
        String columns = "content, author_id, parent_comment_id, post_id";
        String questionMarks = "(?,?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(Comment comment : comments) {
            params.add(new Pair<>(comment.getText(), Types.VARCHAR));
            params.add(new Pair<>(comment.getAuthor() == null ? null : comment.getAuthor().getId(), Types.INTEGER));
            params.add(new Pair<>(comment.getParentComment() == null ? null : comment.getParentComment().getId(), Types.INTEGER));
            params.add(new Pair<>(comment.getPost() == null ? null : comment.getPost().getId(), Types.INTEGER));
            questionMarksJoiner.add(questionMarks);
        }

        statement = String.format(statement, columns, questionMarksJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        StatementSetters.setParameters(ps, params).executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        List<Integer> ids = new ArrayList<>();
        while(rs.next()){
            ids.add(rs.getInt(1));
        }
        ps.close();
        rs.close();
        return ids;
    }

    public int delete(List<Integer> ids) throws SQLException {
        String statement = "DELETE FROM comment WHERE %s LIMIT ?";

        StringJoiner whereJoiner = new StringJoiner(" OR ");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        for (Integer id : ids) {
            params.add(new Pair<>(id, Types.INTEGER));
            whereJoiner.add(" id=? ");
        }
        params.add(new Pair<>(ids.size(), Types.INTEGER)); //Il LIMIT sarà pari ai parametri inseriti per evitare imprevisti

        statement = String.format(statement, whereJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        StatementSetters.setParameters(ps,params);
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        return rowsDeleted;
    }

    public Map<Integer, ArrayList<Comment>> fetchHierarchy(int id, boolean isCommentId, int maxDepth, int loggedUserId) throws SQLException {
        //Lista di colonne da restituire
        List<String> columns = List.of("id AS comment_id", "content AS comment_content", "creation_date AS comment_creation_date",
                "author_id AS user_id", "parent_comment_id", "username", "is_admin", "post_id", "post_title",
                "section_id", "section_name", "votes AS comment_votes", "cte.vote AS comment_vote");
        //Le stesse colonne di sopra, ma con il prefisso "com." (per la recursive CTE)
        List<String> columnsRecurse = columns.stream().map(string -> "com."+string).collect(Collectors.toList());

        List<Pair<Object,Integer>> params = new ArrayList<>();

        //All'interno della recursive CTE è definita un'altra CTE (non ricorsiva) per aggiungere la colonna "voto" al risultati.
        //Se l'utente è loggato verrà mostrato il voto al commento, altrimenti sarà restituito solo il valore '0' per quella colonna.
        String CTE = "";
        String baseCaseJoin = "CROSS JOIN (SELECT 0 AS vote) AS cte";
        String recursiveStepJoin = "CROSS JOIN (SELECT 0 AS vote) AS cte";
        if(loggedUserId > 0){
            CTE = " WITH votes_from_user_cte AS (SELECT comment_id, vote, user_id FROM comment_vote JOIN user ON user_id=user.id WHERE user_id=?) ";
            baseCaseJoin = "LEFT JOIN votes_from_user_cte AS cte ON cte.comment_id = comment_id";
            recursiveStepJoin = "LEFT JOIN votes_from_user_cte AS cte ON cte.comment_id = com.comment_id";
            params.add(new Pair<>(loggedUserId, Types.INTEGER));
        }

        //L'ordine di inserimento dei parametri conta!
        if(isCommentId)
            params.add(new Pair<>(id, Types.INTEGER));
        if(maxDepth >= 0)
            params.add(new Pair<>(maxDepth, Types.INTEGER));
        if(!isCommentId)
            params.add(new Pair<>(id, Types.INTEGER));

        String query =
                "WITH RECURSIVE Recurse_Comments AS ( "+
                        " %s "+
                        "SELECT 1 AS depth, %s FROM v_comment %s %s "+
                        "UNION ALL "+
                        "SELECT depth+1 AS depth, %s FROM v_comment AS com %s %s " +
                        "JOIN Recurse_Comments AS r ON com.parent_comment_id = r.comment_id ) " +
                        "SELECT * FROM Recurse_Comments %s";

        query = String.format(query,
                CTE,
                String.join(",", columns),
                baseCaseJoin,
                isCommentId ? "WHERE id = ?" : "WHERE parent_comment_id IS NULL",
                String.join(",", columnsRecurse),
                recursiveStepJoin,
                maxDepth >= 0 ? "WHERE depth <= ?-1" : " ",
                isCommentId ? "" : "WHERE post_id = ?");

        PreparedStatement ps = con.prepareStatement(query);
        StatementSetters.setParameters(ps, params);
        ResultSet rs = ps.executeQuery();

        HashMap<Integer, ArrayList<Comment>> commentsMap = new HashMap<>();
        while(rs.next()){
            Comment comment = cm.toBean(rs);
            int parentCommentId = comment.getParentComment().getId(); //Ricorda: JDBC restituisce 0 se una colonna di tipo INTEGER ha valore NULL.
            if(!commentsMap.containsKey(parentCommentId)){
                commentsMap.put(parentCommentId, new ArrayList<>());
            }
            commentsMap.get(parentCommentId).add(comment);
        }
        return commentsMap;
    }
}