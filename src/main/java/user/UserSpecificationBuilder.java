package user;

import persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.StringJoiner;

public class UserSpecificationBuilder {
    //Common
    private ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    private boolean ascending = true;
    private String orderBy = "username";
    private int limit = 50;
    private int offset = 0;

    //StringJoiners per formare la stringa
    StringJoiner joinsJoiner = new StringJoiner("\n");
    StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ");

    public Specification build() {
        orderBy = orderBy + " " + (ascending ? "ASC" : "DESC");
        return new Specification(wheresJoiner.toString(),
                                 joinsJoiner.toString(),
                                 orderBy, params, limit, offset);
    }

    public UserSpecificationBuilder doesFollowSection(int id){
        joinsJoiner.add("INNER JOIN follow AS f ON f.user_id = user.id ");
        wheresJoiner.add("section_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public UserSpecificationBuilder isOlderThan(Instant date){
        wheresJoiner.add("creation_date >= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public UserSpecificationBuilder isNewerThan(Instant date){
        wheresJoiner.add("creation_date <= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
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

    public UserSpecificationBuilder ascendedOrder(){
        ascending = true;
        return this;
    }

    public UserSpecificationBuilder descenderOrder(){
        ascending = false;
        return this;
    }

    public UserSpecificationBuilder setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public UserSpecificationBuilder setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
