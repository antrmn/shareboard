package section;

import post.Post;
import util.Pair;
import util.SetNull;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SectionDAO {

    private Connection con;

    public SectionDAO(Connection con) throws SQLException {
        if( (this.con = con) == null)
            throw new SQLException("Null connection");
    }

    
    public List<Section> fetch(SectionSpecification specification) throws SQLException {
        String query = String.format("SELECT %s FROM section %s %s ORDER BY %s %s LIMIT ? OFFSET ?",
                specification.columnsToRetrieve, specification.joins, specification.wheres,
                specification.sortBy, specification.sortOrder);

        List<Pair<Object,Integer>> params = specification.params;

        PreparedStatement ps = con.prepareStatement(query);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        ps.setInt(i++, specification.limit);
        ps.setInt(i++, specification.offset);

        ResultSet rs = ps.executeQuery();
        ArrayList<Section> posts = new ArrayList<>();
        while(rs.next()){
            posts.add(SectionMapper.toBean(rs));
        }

        return posts;
    }

    public int update(Section section) throws SQLException {
        String statement = "UPDATE section SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner valuesToSet = new StringJoiner(",");

        if(section.getName() != null){
            params.add(new Pair(section.getName(), Types.VARCHAR));
            valuesToSet.add("name=?");
        }
        if(section.getDescription() != null){
            params.add(new Pair(section.getDescription(), Types.VARCHAR));
            valuesToSet.add("description=?");
        }
        if(section.getPicture() != null){
            params.add(new Pair(section.getPicture(), Types.VARCHAR));
            valuesToSet.add("picture=?");
        }

        if(valuesToSet.length()==0){
            throw new RuntimeException("Empty SET clause");
        }

        statement = String.format(statement,valuesToSet);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){
            if(param.getLeft() == SetNull.NULL){
                ps.setNull(i++, param.getRight());
            } else{
                ps.setObject(i++, param.getLeft(), param.getRight());
            }
        }

        ps.setInt(i++, section.getId());
        return ps.executeUpdate();
    }

    public int create(List<Section> sections) throws SQLException {
        String columnToSet = "name, description, picture";
        String statement = String.format("INSERT INTO section (%s) VALUES ", columnToSet);
        String questionMarks = "(?,?,?)";

        ArrayList<Pair<Object, Types>> params = new ArrayList<>();
        StringJoiner set = new StringJoiner(",");

        for(Section section : sections){
            params.add(new Pair(section.getName(), Types.VARCHAR));
            params.add(new Pair(section.getDescription(), Types.VARCHAR));
            params.add(new Pair(section.getPicture(), Types.VARCHAR));
            set.add(questionMarks);
        }

        if(set.length()==0){
            throw new RuntimeException("Empty VALUES clause");
        }

        statement = String.format(statement, set);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Types> param : params){
            ps.setObject(i++, param.getLeft(), (SQLType) param.getRight());
        }

        return ps.executeUpdate();
    }

    public int delete(int[] ids) throws SQLException {
        String statement = "DELETE FROM section WHERE id IN (%s)";

        StringJoiner sj = new StringJoiner(",");
        for (int id : ids){
            sj.add("?");
        }

        statement = String.format(statement, sj);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (int id : ids){
            ps.setInt(i++, id);
        }

        return ps.executeUpdate(statement);
    }

    public int create(Section section) throws SQLException {
        return create(List.of(section));
    }

    public int delete(int id) throws SQLException {
        return delete(new int[]{id});
    }

    public Section get(int id) throws SQLException, NamingException {
        return fetch(new SectionSpecification().byId(id)).get(0);
    }
}