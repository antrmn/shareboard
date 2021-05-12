package section;

import persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.util.StringJoiner;

public class SectionSpecificationBuilder extends Specification.Builder<SectionSpecificationBuilder>{

    //StringJoiners per formare la stringa
    StringJoiner joinsJoiner = new StringJoiner("\n");
    StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ");

    @Override
    protected SectionSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        return super.build();
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
}
