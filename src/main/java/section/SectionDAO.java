package section;

import persistence.Specification;
import persistence.StatementSetters;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SectionDAO {

    private static final SectionMapper sm = new SectionMapper();
    private final Connection con;

    public SectionDAO(Connection con) {
        this.con = con;
    }

    public List<Section> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM section %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                .executeQuery();
        List<Section> sections = sm.toBeans(rs);
        ps.close();
        rs.close();
        return sections;
    }

    public int update(Section section) throws SQLException {
        String statement = "UPDATE section SET %s WHERE id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
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

        if(section.getBanner() != null){
            params.add(new Pair<>(section.getBanner(), Types.VARCHAR));
            valuesToSet.add("banner=?");
        }
        params.add(new Pair<>(section.getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public List<Integer> insert(List<Section> sections) throws SQLException {
        String statement = "INSERT INTO section (%s) VALUES %s";
        String columns = "name, description, picture, banner";
        String questionMarks = "(?,?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(Section section : sections) {
            params.add(new Pair<>(section.getName(), Types.VARCHAR));
            params.add(new Pair<>(section.getDescription(), Types.VARCHAR));
            params.add(new Pair<>(section.getPicture(), Types.VARCHAR));
            params.add(new Pair<>(section.getBanner(), Types.VARCHAR));
            questionMarksJoiner.add(questionMarks);
        }

        statement = String.format(statement, columns, questionMarksJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        StatementSetters.setParameters(ps, params).executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        List<Integer> ids = new ArrayList<>();
        while(rs.next()){
            ids.add(rs.getInt(1));
        }
        ps.close();
        rs.close();
        return ids;
    }

    public int delete(List<Integer> ids) throws SQLException {
        String statement = "DELETE FROM section WHERE %s LIMIT ?";

        StringJoiner whereJoiner = new StringJoiner(" OR ");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        for (Integer id : ids) {
            params.add(new Pair<>(id, Types.INTEGER));
            whereJoiner.add(" id=? ");
        }
        params.add(new Pair<>(ids.size(), Types.INTEGER)); //Il LIMIT sarà pari ai parametri inseriti per evitare imprevisti

        statement = String.format(statement, whereJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        StatementSetters.setParameters(ps,params);
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        return rowsDeleted;
    }

}