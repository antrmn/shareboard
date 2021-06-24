package model.comment;

import model.persistence.Specification;
import util.Pair;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.StringJoiner;

public class CommentSpecificationBuilder extends Specification.Builder<CommentSpecificationBuilder>{
    private int loggedUserId = 0;

    StringJoiner joinsJoiner = new StringJoiner("\n");
    StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ");

    public CommentSpecificationBuilder() {
        super("v_comment");
        String userJoin = " JOIN v_user AS user ON comment.author_id=user.id ";
        String postJoin = " JOIN post ON post.id = comment.post_id";
        joinsJoiner.add(postJoin);
        joinsJoiner.add(userJoin);
    }

    @Override
    protected CommentSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        if (loggedUserId > 0) {
            params.add(0, new Pair<>(loggedUserId, Types.INTEGER));
            String loggedUserJoin = "LEFT JOIN (SELECT" +
                    " comment_id, vote, user_id" +
                    " FROM comment_vote " +
                    " JOIN user " +
                    " ON user_id=user.id " +
                    " WHERE user_id=?)" +
                    " AS vc1 " +
                    "ON vc1.comment_id = comment.id";
            joinsJoiner.add(loggedUserJoin);
        } else {
            String notLoggedUserJoin = "CROSS JOIN (SELECT 0 AS vote) AS vc1";
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
        wheresJoiner.add("comment.id=?");
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
