package comment;

import post.Post;
import post.PostMapper;
import post.PostSpecification;
import util.Pair;
import util.SetNull;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommentDAO {

    private Connection con;

    public CommentDAO(Connection con) throws SQLException {
        if( (this.con = con) == null)
            throw new SQLException("Null connection");
    }


    public List<Comment> fetch(CommentSpecification specification) throws SQLException {
        String query = String.format("SELECT %s FROM v_comment AS comment %s %s ORDER BY %s %s LIMIT ? OFFSET ?",
                String.join(",",CommentSpecification.columns), specification.joins.add(specification.loggedUserJoin), specification.wheres,
                specification.sortBy, specification.sortOrder);

        if(specification.loggedUserId != null){
            specification.params.add(0, new Pair(specification.loggedUserId, Types.INTEGER));
        }
        List<Pair<Object,Integer>> params = specification.params;

        PreparedStatement ps = con.prepareStatement(query);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        ps.setInt(i++, specification.limit);
        ps.setInt(i++, specification.offset);

        ResultSet rs = ps.executeQuery();
        ArrayList<Comment> comments = new ArrayList<>();
        while(rs.next()){
            comments.add(CommentMapper.toBean(rs));
        }

        return comments;
    }

    public int update(Comment comment) throws SQLException {
        String statement = "UPDATE comment SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner valuesToSet = new StringJoiner(",");

        if(comment.getText() != null){
            params.add(new Pair(comment.getText(), Types.VARCHAR));
            valuesToSet.add("content=?");
        }
        if(comment.getAuthor() != null && comment.getAuthor().getId() != null){
            params.add(new Pair(comment.getAuthor().getId(), Types.VARCHAR));
            valuesToSet.add("author_id=?");
        }

        if(valuesToSet.length()==0){
            throw new RuntimeException("Empty SET clause");
        }

        statement = String.format(statement,valuesToSet);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){
            if(param.getLeft() == SetNull.NULL){
                ps.setNull(i++, param.getRight());
            } else{
                ps.setObject(i++, param.getLeft(), param.getRight());
            }
        }

        ps.setInt(i++, comment.getId());
        return ps.executeUpdate();
    }

    public int create(List<Comment> comments) throws SQLException {
        String columnToSet = "content, post_id, author_id, parent_comment_id";
        String statement = String.format("INSERT INTO comment (%s) VALUES ", columnToSet);
        String questionMarks = "(?,?,?,?)";

        ArrayList<Pair<Object, Types>> params = new ArrayList<>();
        StringJoiner set = new StringJoiner(",");

        for(Comment comment : comments){
            params.add(new Pair(comment.getText(), Types.VARCHAR));
            params.add(new Pair(comment.getPost() == null ? null : comment.getPost().getId(), Types.INTEGER));
            params.add(new Pair(comment.getAuthor() == null ? null : comment.getAuthor().getId(), Types.INTEGER));
            params.add(new Pair(comment.getParentComment() == null ? null : comment.getParentComment().getId(), Types.INTEGER));
            set.add(questionMarks);
        }

        if(set.length()==0){
            throw new RuntimeException("Empty VALUES clause");
        }

        statement = String.format(statement, set);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Types> param : params){
            ps.setObject(i++, param.getLeft(), (SQLType) param.getRight());
        }

        return ps.executeUpdate();
    }

    public int delete(int[] ids) throws SQLException {
        String statement = "DELETE FROM comment WHERE id IN (%s)";

        StringJoiner sj = new StringJoiner(",");
        for (int id : ids){
            sj.add("?");
        }

        statement = String.format(statement, sj);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (int id : ids){
            ps.setInt(i++, id);
        }

        return ps.executeUpdate();
    }

    public int create(Comment comment) throws SQLException {
        return create(List.of(comment));
    }

    public int delete(int id) throws SQLException {
        return delete(new int[]{id});
    }

    public Comment get(int id) throws SQLException, NamingException {
        return fetch(new CommentSpecification().byId(id)).get(0);
    }

    public Map<Integer, ArrayList<Comment>> fetchHierarchy(int id, boolean isCommentId, int maxDepth, int loggedUserId) throws SQLException {
        List<String> columns = List.of("id AS comment_id", "content AS comment_content", "creation_date AS comment_creation_date",
                "author_id AS user_id", "parent_comment_id", "username", "is_admin", "post_id", "post_title",
                "section_id", "section_name", "votes AS comment_votes", "cte.vote AS comment_vote");

        List<Integer> parameters = new ArrayList<>();

        String CTE = "";
        String baseCaseJoin = "CROSS JOIN (SELECT 0 AS vote) AS cte";
        String recursiveStepJoin = "CROSS JOIN (SELECT 0 AS vote) AS cte";
        if(loggedUserId > 0){
            CTE = " WITH votes_from_user_cte AS (SELECT comment_id, vote, user_id FROM comment_vote JOIN user ON user_id=user.id WHERE user_id=?) ";
            baseCaseJoin = "LEFT JOIN votes_from_user_cte AS cte ON cte.comment_id = comment_id";
            recursiveStepJoin = "LEFT JOIN votes_from_user_cte AS cte ON cte.comment_id = com.comment_id";
            parameters.add(loggedUserId);
        }

        if(isCommentId)
            parameters.add(id);
        if(maxDepth >= 0)
            parameters.add(maxDepth);
        if(!isCommentId)
            parameters.add(id);

        List<String> columnsRecurse = columns.stream().map(string -> "com."+string)
                .collect(Collectors.toList());

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
                                maxDepth >= 0 ? "WHERE depth <= ?-1" : "",
                                isCommentId ? "" : "WHERE post_id = ?");

        PreparedStatement ps = con.prepareStatement(query);

        int i=1;
        for(Integer param : parameters){
            ps.setInt(i++, param);
        }

        ResultSet rs = ps.executeQuery();
        HashMap<Integer, ArrayList<Comment>> commentsMap = new HashMap<>();
        while(rs.next()){
            Comment comment = CommentMapper.toBean(rs);
            int parentCommentId = comment.getParentComment().getId(); //0 se NULL

            if(!commentsMap.containsKey(parentCommentId)){
                commentsMap.put(parentCommentId, new ArrayList<>());
            }
            commentsMap.get(parentCommentId).add(comment);
        }

        return commentsMap;
    }
}