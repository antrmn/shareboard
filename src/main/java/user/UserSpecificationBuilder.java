package user;

import persistence.Specification;
import util.Pair;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

public class UserSpecificationBuilder extends Specification.Builder<UserSpecificationBuilder>{
    List<String> columnsList = List.of("user.id", "user.username", "user.email", "user.description", "user.picture",
            "user.creation_date", "user.is_admin");

    private final StringJoiner joinsJoiner = new StringJoiner("\n");
    private final StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ");

    public UserSpecificationBuilder() {
        super("v_user");
        columns = String.join(", ", columnsList);
    }

    @Override
    protected UserSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public UserSpecificationBuilder doesFollowSection(int id){
        joinsJoiner.add("INNER JOIN follow AS f ON f.user_id = user.id ");
        wheresJoiner.add("section_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public UserSpecificationBuilder isOlderThan(Instant date){
        wheresJoiner.add("creation_date <= ?");
        params.add(new Pair<>(Timestamp.from(date), Types.TIMESTAMP));
        return this;
    }

    public UserSpecificationBuilder isNewerThan(Instant date){
        wheresJoiner.add("creation_date >= ?");
        params.add(new Pair<>(Timestamp.from(date), Types.TIMESTAMP));
        return this;
    }

    public UserSpecificationBuilder byId(int id){
        wheresJoiner.add("id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public UserSpecificationBuilder byUsername(String username){
        wheresJoiner.add("username LIKE ?");
        params.add(new Pair<>('%'+username+'%', Types.VARCHAR));
        return this;
    }

    public UserSpecificationBuilder sortByTime(){
        orderBy = "creation_date";
        return this;
    }

    public UserSpecificationBuilder sortByName(){
        orderBy = "username";
        return this;
    }
}
