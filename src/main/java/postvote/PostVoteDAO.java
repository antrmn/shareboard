package postvote;

import persistence.Specification;
import persistence.StatementSetters;
import post.Post;
import user.User;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class PostVoteDAO {
    private static final PostVoteMapper pvm = new PostVoteMapper();
    private final Connection con;

    public PostVoteDAO(Connection con) throws SQLException {
        this.con = con;
    }

    public List<PostVote> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM post_vote %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                                     specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                                    .executeQuery();
        List<PostVote> postVotes = pvm.toBeans(rs);
        ps.close();
        rs.close();
        return postVotes;
    }

    public int update(PostVote postVote) throws SQLException {
        String statement = "UPDATE post_vote SET %s WHERE user_id=? AND post_id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        if(postVote.getVote() != null){
            params.add(new Pair<>(postVote.getVote(), Types.TINYINT));
            valuesToSet.add("vote=?");
        }

        params.add(new Pair<>(postVote.getUser() == null ? null : postVote.getUser().getId(), Types.INTEGER));
        params.add(new Pair<>(postVote.getPost() == null ? null : postVote.getPost().getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public int insert(List<PostVote> postVotes) throws SQLException {
        String statement = "INSERT INTO post_vote (%s) VALUES %s";
        String columns = "user_id, post_id, vote";
        String questionMarks = "(?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(PostVote postVote : postVotes) {
            params.add(new Pair<>(postVote.getUser() == null ? null : postVote.getUser().getId(), Types.INTEGER));
            params.add(new Pair<>(postVote.getPost() == null ? null : postVote.getPost().getId(), Types.INTEGER));
            params.add(new Pair<>(postVote.getVote(), Types.TINYINT));
            questionMarksJoiner.add(questionMarks);
        }

        statement = String.format(statement, columns, questionMarksJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        int insertedRows = StatementSetters.setParameters(ps, params).executeUpdate();

        ps.close();
        return insertedRows;
    }

    public int delete(List<Pair<User, Post>> pks) throws SQLException {
        String statement = "DELETE FROM post_vote WHERE %s LIMIT ?";

        StringJoiner whereJoiner = new StringJoiner(" OR ");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        for (Pair<User, Post> pk : pks) {
            params.add(new Pair<>(pk.getLeft().getId(), Types.INTEGER));
            params.add(new Pair<>(pk.getRight().getId(), Types.INTEGER));
            whereJoiner.add(" user_id=? AND post_id=? ");
        }
        params.add(new Pair<>(pks.size(), Types.INTEGER)); //Il LIMIT sara' pari ai parametri inseriti per evitare imprevisti

        statement = String.format(statement, whereJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        StatementSetters.setParameters(ps,params);
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        return rowsDeleted;
    }

    public int delete(Pair<User,Post> pk) throws SQLException {
        return delete(List.of(pk));
    }

    public PostVote get(Pair<User,Post> pk) throws SQLException{
        PostVoteSpecificationBuilder pvsb = new PostVoteSpecificationBuilder().byUserId(pk.getLeft().getId())
                                                                              .byPostId(pk.getRight().getId());
        return fetch(pvsb.build()).get(0);
    }

    public int insert(PostVote postVote) throws SQLException {
        return insert(List.of(postVote));
    }
}