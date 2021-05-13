package comment;

import persistence.Specification;
import util.Pair;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.StringJoiner;

public class CommentSpecificationBuilder extends Specification.Builder<CommentSpecificationBuilder>{
    //Join sempre aggiunte
    private final String userJoin = " JOIN v_user AS user ON comment.author_id=user.id ";
    private final String postJoin = " JOIN post ON post.id = comment.post_id";

    //Utente loggato?
    private int loggedUserId = 0;
    private final String notLoggedUserJoin = "CROSS JOIN (SELECT 0 AS vote) AS vc1";
    private final String loggedUserJoin = "LEFT JOIN (SELECT" +
            " comment_id, vote, user_id" +
            " FROM comment_vote " +
            " JOIN user " +
            " ON user_id=user.id " +
            " WHERE user_id=?)" +
            " AS vc1 " +
            "ON vc1.comment_id = comment.id";

    //StringJoiners per formare la stringa
    StringJoiner joinsJoiner = new StringJoiner("\n");
    StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ");

    @Override
    protected CommentSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        joinsJoiner.add(userJoin);
        joinsJoiner.add(postJoin);
        if (loggedUserId > 0) {
            params.add(new Pair<>(loggedUserId, Types.INTEGER));
            joinsJoiner.add(loggedUserJoin);
        } else {
            joinsJoiner.add(notLoggedUserJoin);
        }
        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public CommentSpecificationBuilder loggedUser(Integer id){
        loggedUserId = id;
        return this;
    }

    public CommentSpecificationBuilder isVotedBy(int id){
        joinsJoiner.add("INNER JOIN comment_vote AS cv ON cv.comment_id=comment_id ");
        wheresJoiner.add("user_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentSpecificationBuilder isAuthor(int id){
        wheresJoiner.add("user_id = ? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentSpecificationBuilder isAuthor(String name){
        wheresJoiner.add("username = ? ");
        params.add(new Pair<>(name, Types.VARCHAR));
        return this;
    }

    public CommentSpecificationBuilder doesBodyContains(String content){
        wheresJoiner.add("comment_content LIKE ?");
        params.add(new Pair<>('%'+content+'%', Types.VARCHAR));
        return this;
    }

    public CommentSpecificationBuilder isOlderThan(Instant date){
        wheresJoiner.add("comment_creation_date <= ?");
        params.add(new Pair<>(Timestamp.from(date), Types.TIMESTAMP));
        return this;
    }

    public CommentSpecificationBuilder isNewerThan(Instant date){
        wheresJoiner.add("comment_creation_date >= ?");
        params.add(new Pair<>(Timestamp.from(date), Types.TIMESTAMP));
        return this;
    }

    public CommentSpecificationBuilder byId(int id){
        wheresJoiner.add("comment_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentSpecificationBuilder sortByTime(){
        orderBy = "creation_date";
        return this;
    }

    public CommentSpecificationBuilder sortByVotes(){
        orderBy = "votes";
        return this;
    }
}
