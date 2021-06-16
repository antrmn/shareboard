package ban;

import persistence.Specification;
import util.Pair;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

public class BanSpecificationBuilder extends Specification.Builder<BanSpecificationBuilder>{
    private List<String> columnsList = List.of("id AS ban_id", "admin_id", "section_id", "user_id", "start_time",
                                               "end_time", "is_global");

    private StringJoiner joinsJoiner = new StringJoiner("\n");
    private StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ").setEmptyValue(" ");

    private boolean joinUserNeeded = false;
    private boolean joinSectionNeeded = false;
    private boolean joinAdminNeeded = false;

    public BanSpecificationBuilder() {
        super("ban");
        this.orderBy = "ban_id";
    }

    @Override
    protected BanSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        if(joinSectionNeeded)
            joinsJoiner.add(" JOIN section ON section.id=ban.section_id");
        if(joinUserNeeded)
            joinsJoiner.add(" JOIN user ON user.id=ban.user_id");
        if(joinUserNeeded)
            joinsJoiner.add(" JOIN user AS admin_user ON admin_user.id=ban.admin_id");

        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
    }

    public BanSpecificationBuilder byUserId(int id){
        wheresJoiner.add(" user_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public BanSpecificationBuilder byId(int id){
        wheresJoiner.add(" id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public BanSpecificationBuilder bySectionId(int id){
        wheresJoiner.add(" section_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public BanSpecificationBuilder byAdminId(int id){
        wheresJoiner.add(" admin_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public BanSpecificationBuilder startAfter(Instant instant){
        wheresJoiner.add(" start_time >= ?");
        params.add(new Pair<>(Timestamp.from(instant), Types.TIMESTAMP));
        return this;
    }

    public BanSpecificationBuilder startBefore(Instant instant){
        wheresJoiner.add(" start_time <= ?");
        params.add(new Pair<>(Timestamp.from(instant), Types.TIMESTAMP));
        return this;
    }

    public BanSpecificationBuilder endAfter(Instant instant){
        wheresJoiner.add(" end_time >= ?");
        params.add(new Pair<>(Timestamp.from(instant), Types.TIMESTAMP));
        return this;
    }

    public BanSpecificationBuilder endBefore(Instant instant){
        wheresJoiner.add(" end_time <= ?");
        params.add(new Pair<>(Timestamp.from(instant), Types.TIMESTAMP));
        return this;
    }

    public BanSpecificationBuilder global(){
        wheresJoiner.add("is_global=1");
        return this;
    }

    public BanSpecificationBuilder showSectionNames(){
        joinSectionNeeded = true;
        columnsList.add("section.name AS section_name");
        return this;
    }

    public BanSpecificationBuilder showUserNames(){
        joinUserNeeded = true;
        columnsList.add("user.name AS user_name");
        return this;
    }

    public BanSpecificationBuilder showAdminNames(){
        joinUserNeeded = true;
        columnsList.add("user_admin.username AS admin_username");
        return this;
    }
}
