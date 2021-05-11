package user;

import util.Pair;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringJoiner;

public class UserSpecification {

    private static final String column = "id, username, email, description, picture, creation_date, is_admin";

    StringJoiner joins = new StringJoiner("\n");
    StringJoiner wheres = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
    ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    String sortBy = "creation_date";
    String sortOrder = "ASC";
    String columnsToRetrieve = column;
    int limit = 50;
    int offset = 0;

    public UserSpecification doesFollowSection(int id){
        joins.add("INNER JOIN follow AS f ON f.user_id = user.id ");
        wheres.add("section_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public UserSpecification isOlderThan(Date date){
        wheres.add("creation_date >= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public UserSpecification isNewerThan(Date date){
        wheres.add("creation_date <= ?");
        params.add(new Pair<>(date, Types.TIMESTAMP));
        return this;
    }

    public UserSpecification byId(int id){
        wheres.add("id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public UserSpecification byUsername(String username){
        wheres.add("username LIKE ?");
        params.add(new Pair<>('%'+username+'%', Types.VARCHAR));
        return this;
    }

    public UserSpecification sortByTime(){
        sortBy = "creation_date";
        return this;
    }

    public UserSpecification sortByName(){
        sortBy = "username";
        return this;
    }

    public UserSpecification ascendedOrder(){
        sortOrder = "ASC";
        return this;
    }

    public UserSpecification descenderOrder(){
        sortOrder = "DESC";
        return this;
    }

    public UserSpecification setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public UserSpecification setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
