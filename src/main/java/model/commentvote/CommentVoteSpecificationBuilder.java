package model.commentvote;

import model.persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class CommentVoteSpecificationBuilder extends Specification.Builder<CommentVoteSpecificationBuilder>{
    private List<String> columnsList = List.of("comment_id", "user_id", "vote");

    private StringJoiner joinsJoiner = new StringJoiner("\n");
    private StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ").setEmptyValue(" ");

    private boolean joinUserNeeded = false;
    private boolean joinCommentNeeded = false;

    public CommentVoteSpecificationBuilder() {
        super("comment_vote");
        this.orderBy = "user_id, comment_id";
    }

    @Override
    protected CommentVoteSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        if(joinCommentNeeded)
            joinsJoiner.add(" JOIN comment ON comment.id=comment_vote.comment_id");
        if(joinUserNeeded)
            joinsJoiner.add(" JOIN user ON user.id=comment_vote.user_id");

        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public CommentVoteSpecificationBuilder byUserId(int id){
        wheresJoiner.add(" user_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentVoteSpecificationBuilder byCommentId(int id){
        wheresJoiner.add(" comment_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public CommentVoteSpecificationBuilder byUserName(String name){
        joinUserNeeded = true;
        wheresJoiner.add(" username LIKE ? ");
        params.add(new Pair<>("%"+name+"%", Types.VARCHAR));
        return this;
    }

    public CommentVoteSpecificationBuilder byVote(Short vote){
        wheresJoiner.add(" vote = ? ");
        params.add(new Pair<>(vote, Types.TINYINT));
        return this;
    }

    public CommentVoteSpecificationBuilder upvotes(){
        return byVote(CommentVote.UPVOTE);
    }

    public CommentVoteSpecificationBuilder downvotes(){
        return byVote(CommentVote.DOWNVOTE);
    }

    public CommentVoteSpecificationBuilder showUserNames(){
        joinUserNeeded = true;
        columnsList.add("user.name AS user_name");
        return this;
    }
}
