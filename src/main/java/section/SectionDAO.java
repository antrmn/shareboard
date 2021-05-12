package section;

import persistence.GenericDAO;
import util.Pair;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class SectionDAO extends GenericDAO<Section, SectionMapper> {

    private Connection con;

    public SectionDAO(Connection con) {
        super(con,
                List.of("section.id","section.description","section.name","section.picture"),
                List.of("description","name","picture"),
                "section",
                "section",
                new SectionMapper());
    }


    @Override
    protected String fillUpdateStatement(Section section, List<Pair<Object, Integer>> params) {
        StringJoiner valuesToSet = new StringJoiner(",");

        if(section.getName() != null){
            params.add(new Pair<>(section.getName(), Types.VARCHAR));
            valuesToSet.add("name=?");
        }
        if(section.getDescription() != null){
            params.add(new Pair<>(section.getDescription(), Types.VARCHAR));
            valuesToSet.add("description=?");
        }
        if(section.getPicture() != null){
            params.add(new Pair<>(section.getPicture(), Types.VARCHAR));
            valuesToSet.add("picture=?");
        }
        params.add(new Pair<>(section.getId(), Types.INTEGER));
        return valuesToSet.toString();
    }

    @Override
    protected void fillInsertStatement(Section section, List<Pair<Object, Integer>> params) {
        params.add(new Pair<>(section.getName(), Types.VARCHAR));
        params.add(new Pair<>(section.getDescription(), Types.VARCHAR));
        params.add(new Pair<>(section.getPicture(), Types.VARCHAR));
    }
}