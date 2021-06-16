package comment;

import persistence.Specification;
import persistence.StatementSetters;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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
        List<Comment> comments = cm.toBeans(rs);
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

    /**
     * <p>Restituisce i commenti specificati seguendo una struttura gerarchica.</p><br>
     * <p>Se l'id specificato corrisponde all'id di un post, verranno restituiti tutti i commenti di quel post.<br>
     *     Se invece l'id specificato corrisponde a un commento, verranno restituiti tutti i commenti
     *     a partire da quello specificato (incluso)</p><br>
     * <p>Se viene invocata la {@link Comment#getParentComment()} ai root comment verrà restituito un dummy comment con id 0. <br>
     * Se vengono mostrati i commenti a partire da un dato commento (quindi, con isCommentId settato a true)
     * il commento di partenza restituirà null al metodo {@link Comment#getParentComment()}</p><br>
     *
     * @param id L'id del commento/post da dove far partire la ricerca
     * @param isCommentId True se l'id corrisponde all'id di un commento, false se corrisponde all'id di un post
     * @param maxDepth Massimo livello di ricorsione raggiungibile nella ricerca
     * @param loggedUserId L'id dell'utente loggato per mostrare i valori della colonna "voti". Specificare un valore &lt;=0 se l'utente non è loggato.
     * @return Una mappa che ha come chiave un id e come valore una lista di commenti che rispondo direttamente al commento con tale id.
     *         I commenti al "root level" (ossia quelli che non rispondono a nessun commento) sono mappati a indice 0.
     * @throws SQLException In caso di errore SQL
     */
    public Map<Integer, ArrayList<Comment>> fetchHierarchy(int id, boolean isCommentId, int maxDepth, int loggedUserId) throws SQLException {
        String query = " WITH RECURSIVE Recurse_Comments AS ( \n" +
                            " %s \n" + //Se l'utente è loggato: CTE per vedere i voti. Altrimenti: stringa vuota
                            " %s \n" + //SELECT per passo base
                            " UNION ALL" +
                            " %s \n " + //SELECT per passo ricorsivo
                       ")\n" +
                       "%s"; //outerSelect

        List<Pair<Object,Integer>> params = new ArrayList<>();

        String voteCTE;
        if(loggedUserId > 0) {
            //restituisce una tabella con i voti dell'utente specificato
            voteCTE = " WITH votes_from_user_cte AS (SELECT comment_id, vote, user_id FROM comment_vote JOIN user ON user_id=user.id WHERE user_id=?) ";
            params.add(new Pair<>(loggedUserId, Types.INTEGER));
        } else {
            voteCTE = "";
        }

        //La view utilizzata per questa query non è v_comment ma v_comment_complete per evitare di aggiungere troppe join
        String baseCase = "SELECT %s FROM v_comment_complete AS vcc %s %s";
        {
            String columns = "0 AS depth, vcc.*, cte.vote ";

            String joins;
            if(loggedUserId > 0)
                joins = " LEFT JOIN votes_from_user_cte AS cte ON cte.comment_id = vcc.id";
            else
                joins = " CROSS JOIN (SELECT 0 AS vote) AS cte";

            String where;
            if (isCommentId) {
                where = "WHERE id = ?";
                params.add(new Pair<>(id, Types.INTEGER));
            }
            else {
                where = "WHERE parent_comment_id IS NULL";
            }

            baseCase = String.format(baseCase, columns, joins, where);
        }

        String recursiveStep = "SELECT %s FROM v_comment_complete AS com %s " +
                                    "JOIN Recurse_Comments AS r ON com.parent_comment_id = r.id  %s";
        {
            String columns = "depth+1 AS depth, com.*, cte.vote";

            String joins;
            if(loggedUserId > 0)
                joins = " LEFT JOIN votes_from_user_cte AS cte ON cte.comment_id = com.id";
            else
                joins = " CROSS JOIN (SELECT 0 AS vote) AS cte";

            String where;
            if (maxDepth >= 0) {
                where = "WHERE depth <= ?";
                params.add(new Pair<>(maxDepth-1, Types.INTEGER));
            }
            else {
                where = " ";
            }

            recursiveStep = String.format(recursiveStep, columns, joins, where);
        }

        String outerSelect = "SELECT * FROM Recurse_Comments %s";
        {
            String where;
            if(isCommentId){
                where = "";
            } else {
                where = " WHERE post_id = ? ";
                params.add(new Pair<>(id, Types.INTEGER));
            }
            outerSelect = String.format(outerSelect, where);
        }

        query = String.format(query, voteCTE, baseCase, recursiveStep, outerSelect);

        PreparedStatement ps = con.prepareStatement(query);
        StatementSetters.setParameters(ps, params);
        ResultSet rs = ps.executeQuery();

        Map<Integer, ArrayList<Comment>> commentsMap = cm.toBeansHierarchy(rs);
        ps.close();
        rs.close();
        return commentsMap;
    }

    public int delete(int id) throws SQLException {
        return delete(List.of(id));
    }

    public Comment get(int id) throws SQLException{
        CommentSpecificationBuilder csb = new CommentSpecificationBuilder().byId(id);
        return fetch(csb.build()).get(0);
    }

    public int insert(Comment comment) throws SQLException {
        return insert(List.of(comment)).size();
    }
}