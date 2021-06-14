package follow;

import persistence.Specification;
import util.Pair;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

public class FollowSpecificationBuilder extends Specification.Builder<FollowSpecificationBuilder>{
    private List<String> columnsList = List.of("post_id", "user_id");

    private StringJoiner joinsJoiner = new StringJoiner("\n");
    private StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ").setEmptyValue(" ");

    private boolean joinUserNeeded = false;
    private boolean joinSectionNeeded = false;

    public FollowSpecificationBuilder() {
        super("follow");
        this.orderBy = "user_id, section_id";
    }

    @Override
    protected FollowSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        if(joinSectionNeeded)
            joinsJoiner.add(" JOIN section ON section.id=follow.section_id");
        if(joinUserNeeded)
            joinsJoiner.add(" JOIN user ON user.id=follow.user_id");

        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public FollowSpecificationBuilder byUserId(int id){
        wheresJoiner.add(" user_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public FollowSpecificationBuilder bySectionId(int id){
        wheresJoiner.add(" section_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public FollowSpecificationBuilder byUserName(String name){
        joinUserNeeded = true;
        wheresJoiner.add(" username LIKE ? ");
        params.add(new Pair<>("%"+name+"%", Types.VARCHAR));
        return this;
    }

    public FollowSpecificationBuilder bySectionName(String name){
        joinSectionNeeded = true;
        wheresJoiner.add("section.name LIKE ? ");
        params.add(new Pair<>("%"+name+"%", Types.VARCHAR));
        return this;
    }

    public FollowSpecificationBuilder showSectionNames(){
        joinSectionNeeded = true;
        columnsList.add("section.name AS section_name");
        return this;
    }

    public FollowSpecificationBuilder showUserNames(){
        joinUserNeeded = true;
        columnsList.add("user.name AS username");
        return this;
    }
}
