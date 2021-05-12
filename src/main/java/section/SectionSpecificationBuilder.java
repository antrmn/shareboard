package section;

import persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.util.ArrayList;
import java.util.StringJoiner;

public class SectionSpecificationBuilder {
    //Common
    private ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    private boolean ascending = true;
    private String orderBy = "name";
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

    public SectionSpecificationBuilder isFollowedByUser(int id){
        joinsJoiner.add(" JOIN (follow as f) ON f.section_id = section.id ");
        wheresJoiner.add(" f.user_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public SectionSpecificationBuilder doesTitleContains(String title){
        wheresJoiner.add("section.name LIKE ? ");
        params.add(new Pair<>("%"+title+"%", Types.VARCHAR));
        return this;
    }

    public SectionSpecificationBuilder byId(int id){
        wheresJoiner.add("id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public SectionSpecificationBuilder sortByName(){
        orderBy = "name";
        return this;
    }

    public SectionSpecificationBuilder sortById(){
        orderBy = "id";
        return this;
    }

    public SectionSpecificationBuilder ascendedOrder(){
        ascending = true;
        return this;
    }

    public SectionSpecificationBuilder descenderOrder(){
        ascending = false;
        return this;
    }

    public SectionSpecificationBuilder setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public SectionSpecificationBuilder setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
