package section;

import post.Post;
import util.Pair;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringJoiner;

public class SectionSpecification {

    private static final String column = "id AS section_id, description AS section_description, name AS section_name, picture AS secton_picture";

    StringJoiner joins = new StringJoiner("\n");
    StringJoiner wheres = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
    ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
    String sortBy = "name";
    String sortOrder = "ASC";
    String columnsToRetrieve = column;
    int limit = 50;
    int offset = 0;

    public SectionSpecification isFollowedByUser(int id){
        joins.add("INNER JOIN (follow as f) ON f.section_id = id ");
        wheres.add("f.user_id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public SectionSpecification doesTitleContains(String title){
        wheres.add("section.name LIKE %?%");
        params.add(new Pair<>(title, Types.VARCHAR));
        return this;
    }

    public SectionSpecification byId(int id){
        wheres.add("id=?");
        params.add(new Pair<>(id, Types.INTEGER));
        return this;
    }

    public SectionSpecification sortByName(){
        sortBy = "name";
        return this;
    }

    public SectionSpecification sortById(){
        sortBy = "id";
        return this;
    }

    public SectionSpecification ascendedOrder(){
        sortOrder = "ASC";
        return this;
    }

    public SectionSpecification descenderOrder(){
        sortOrder = "DESC";
        return this;
    }

    public SectionSpecification setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public SectionSpecification setOffset(int offset){
        this.offset = offset;
        return this;
    }
}
