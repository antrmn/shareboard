package comment;

import persistence.Specification;
import persistence.StatementSetters;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
}