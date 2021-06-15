package section;

import persistence.Specification;
import util.Pair;

import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class SectionSpecificationBuilder extends Specification.Builder<SectionSpecificationBuilder>{
    List<String> columnsList = List.of("section.id","section.description","section.name","section.picture","section.banner");

    StringJoiner joinsJoiner = new StringJoiner("\n");
    StringJoiner wheresJoiner = new StringJoiner(" AND ", " WHERE ", " ");

//    String table
    public SectionSpecificationBuilder() {
        super("section");
    }

    @Override
    protected SectionSpecificationBuilder getThisBuilder() {
        return this;
    }

    public Specification build() {
        joins = joinsJoiner.toString();
        wheres = wheresJoiner.toString();
        columns = String.join(", ", columnsList);
        return super.build();
    }

    public SectionSpecificationBuilder isFollowedByUser(int id){
        joinsJoiner.add(" JOIN (follow as f) ON f.section_id = section.id ");
        wheresJoiner.add(" f.user_id=? ");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public SectionSpecificationBuilder doesTitleContain(String title){
        wheresJoiner.add("section.name LIKE ? ");
        params.add(new Pair<>("%"+title+"%", Types.VARCHAR));
        return this;
    }

    public SectionSpecificationBuilder byId(int id){
        wheresJoiner.add("id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public SectionSpecificationBuilder byName(String name){
        wheresJoiner.add("name=?");
        params.add(new Pair<>(name, Types.VARCHAR));
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
