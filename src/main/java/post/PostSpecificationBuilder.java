package post;

import persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.time.Instant;
import java.util.StringJoiner;

public class PostSpecificationBuilder extends Specification.Builder<PostSpecificationBuilder>{
    //Join sempre aggiunte
    private final String userJoin = " JOIN v_user AS user ON post.author_id=user.id ";
    private final String sectionJoin = " JOIN section ON post.section_id = section.id";

    //Utente loggato?
    private int loggedUserId = 0;
    private final String notLoggedUserJoin = "CROSS JOIN (SELECT 0 AS vote) AS v1";
    private final String loggedUserJoin = "LEFT JOIN (SELECT" +
            " post_id, vote, user_id" +
            " FROM post_vote " +
            " JOIN user " +
            " ON user_id=user.id " +
            " WHERE user_id=?)" +
            " AS v1 " +
            "ON v1.post_id = post.id";

    //StringJoiners per formare la stringa
    StringJoiner joinsJoiner = new StringJoiner("\n");
    StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ").setEmptyValue(" ");

    @Override
    protected PostSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        joinsJoiner.add(userJoin);
        joinsJoiner.add(sectionJoin);
        joinsJoiner.add(loggedUserId <= 0 ? notLoggedUserJoin : loggedUserJoin);
        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public PostSpecificationBuilder loggedUser(Integer id){
        loggedUserId = id;
        return this;
    }

    public PostSpecificationBuilder isVotedBy(int id){
        joinsJoiner.add(" JOIN (post_vote as pv) ON pv.post_id=post.id ");
        wheresJoiner.add(" user_id=? ");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecificationBuilder isSectionFollowedByUser(int id){
        joinsJoiner.add(" JOIN (follow as f) ON f.section_id = section.id ");
        wheresJoiner.add(" f.user_id=? ");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecificationBuilder isInSection(int id){
        wheresJoiner.add(" section.id = ? ");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecificationBuilder isAuthor(int id){
        wheresJoiner.add(" author_id = ? ");
        params.add(new Pair<>(id, java.sql.Types.INTEGER));
        return this;
    }

    public PostSpecificationBuilder isAuthor(String name){
        wheresJoiner.add(" user.username = ? ");
        params.add(new Pair<>(name, java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecificationBuilder doesTitleContain(String title){
        wheresJoiner.add(" post.title LIKE ? ");
        params.add(new Pair<>("%"+title+"%", java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecificationBuilder doesBodyContain(String content){
        wheresJoiner.add(" post.content LIKE ? ");
        params.add(new Pair<>("%"+content+"%", java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecificationBuilder doesTitleOrBodyContains(String content){
        wheresJoiner.add(" (post.title LIKE ? OR post.content LIKE ?) ");
        params.add(new Pair<>("%"+content+"%", java.sql.Types.VARCHAR));
        params.add(new Pair<>("%"+content+"%", java.sql.Types.VARCHAR));
        return this;
    }

    public PostSpecificationBuilder isOlderThan(Instant date){
        wheresJoiner.add(" post.creation_date >= ? ");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public PostSpecificationBuilder isNewerThan(Instant date){
        wheresJoiner.add(" post.creation_date <= ? ");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public PostSpecificationBuilder isType(Post.Type type){
        wheresJoiner.add(" post.type = ? ");
        params.add(new Pair<>(type.name(), Types.VARCHAR));
        return this;
    }

    public PostSpecificationBuilder byId(int id){
        wheresJoiner.add(" id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public PostSpecificationBuilder sortByTime(){
        orderBy = "creation_date";
        return this;
    }

    public PostSpecificationBuilder sortByVotes(){
        orderBy = "votes";
        return this;
    }


}
