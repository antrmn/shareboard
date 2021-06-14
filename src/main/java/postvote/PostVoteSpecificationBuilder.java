package postvote;

import persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class PostVoteSpecificationBuilder extends Specification.Builder<PostVoteSpecificationBuilder>{
    private List<String> columnsList = List.of("post_id", "user_id", "vote");

    private StringJoiner joinsJoiner = new StringJoiner("\n");
    private StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ").setEmptyValue(" ");

    private boolean joinUserNeeded = false;
    private boolean joinPostNeeded = false;

    public PostVoteSpecificationBuilder() {
        super("post_vote");
        this.orderBy = "user_id, post_id";
    }

    @Override
    protected PostVoteSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        if(joinPostNeeded)
            joinsJoiner.add(" JOIN post ON post.id=post_vote.post_id");
        if(joinUserNeeded)
            joinsJoiner.add(" JOIN user ON user.id=post_vote.user_id");

        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public PostVoteSpecificationBuilder byUserId(int id){
        wheresJoiner.add(" user_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public PostVoteSpecificationBuilder byPostId(int id){
        wheresJoiner.add(" post_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public PostVoteSpecificationBuilder byUserName(String name){
        joinUserNeeded = true;
        wheresJoiner.add(" username LIKE ? ");
        params.add(new Pair<>("%"+name+"%", Types.VARCHAR));
        return this;
    }

    public PostVoteSpecificationBuilder byVote(Short vote){
        wheresJoiner.add(" vote = ? ");
        params.add(new Pair<>(vote, Types.TINYINT));
        return this;
    }

    public PostVoteSpecificationBuilder upvotes(){
        return byVote(PostVote.UPVOTE);
    }

    public PostVoteSpecificationBuilder downvotes(){
        return byVote(PostVote.DOWNVOTE);
    }

    public PostVoteSpecificationBuilder showPostTitles(String title){
        joinPostNeeded = true;
        columnsList.add("post.title AS post_title");
        return this;
    }

    public PostVoteSpecificationBuilder showUserNames(){
        joinUserNeeded = true;
        columnsList.add("user.name AS user_name");
        return this;
    }
}
