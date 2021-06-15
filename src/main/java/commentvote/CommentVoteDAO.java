package commentvote;

import persistence.Specification;
import persistence.StatementSetters;
import comment.Comment;
import user.User;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class CommentVoteDAO {
    private static final CommentVoteMapper cvm = new CommentVoteMapper();
    private final Connection con;

    public CommentVoteDAO(Connection con) throws SQLException {
        this.con = con;
    }

    public List<CommentVote> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM comment_vote %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                                     specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                                    .executeQuery();
        List<CommentVote> commentVotes = cvm.toBeans(rs);
        ps.close();
        rs.close();
        return commentVotes;
    }

    public int update(CommentVote commentVote) throws SQLException {
        String statement = "UPDATE comment_vote SET %s WHERE user_id=? AND comment_id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        if(commentVote.getVote() != null){
            params.add(new Pair<>(commentVote.getVote(), Types.TINYINT));
            valuesToSet.add("vote=?");
        }

        params.add(new Pair<>(commentVote.getUser() == null ? null : commentVote.getUser().getId(), Types.INTEGER));
        params.add(new Pair<>(commentVote.getComment() == null ? null : commentVote.getComment().getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public int insert(List<CommentVote> commentVotes) throws SQLException {
        String statement = "INSERT INTO comment_vote (%s) VALUES %s";
        String columns = "user_id, comment_id, vote";
        String questionMarks = "(?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(CommentVote commentVote : commentVotes) {
            params.add(new Pair<>(commentVote.getUser() == null ? null : commentVote.getUser().getId(), Types.INTEGER));
            params.add(new Pair<>(commentVote.getComment() == null ? null : commentVote.getComment().getId(), Types.INTEGER));
            params.add(new Pair<>(commentVote.getVote(), Types.TINYINT));
            questionMarksJoiner.add(questionMarks);
        }

        statement = String.format(statement, columns, questionMarksJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        int insertedRows = StatementSetters.setParameters(ps, params).executeUpdate();

        ps.close();
        return insertedRows;
    }

    public int delete(List<Pair<User, Comment>> pks) throws SQLException {
        String statement = "DELETE FROM comment_vote WHERE %s LIMIT ?";

        StringJoiner whereJoiner = new StringJoiner(" OR ");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        for (Pair<User, Comment> pk : pks) {
            params.add(new Pair<>(pk.getLeft().getId(), Types.INTEGER));
            params.add(new Pair<>(pk.getRight().getId(), Types.INTEGER));
            whereJoiner.add(" user_id=? AND comment_id=? ");
        }
        params.add(new Pair<>(pks.size(), Types.INTEGER)); //Il LIMIT sara' pari ai parametri inseriti per evitare imprevisti

        statement = String.format(statement, whereJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        StatementSetters.setParameters(ps,params);
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        return rowsDeleted;
    }

}