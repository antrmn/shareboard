package user;

import comment.Comment;
import util.Pair;
import util.SetNull;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserDAO {

    private Connection con;

    public UserDAO(Connection con) throws SQLException {
        if( (this.con = con) == null)
            throw new SQLException("Null connection");
    }


    public List<User> fetch(UserSpecification specification) throws SQLException {
        String query = String.format("SELECT %s FROM v_user AS user %s %s ORDER BY %s %s LIMIT ? OFFSET ?",
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
        ArrayList<User> users = new ArrayList<>();
        while(rs.next()){
            users.add(UserMapper.toBean(rs));
        }

        return users;
    }

    public int update(User user) throws SQLException {
        String statement = "UPDATE user SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner valuesToSet = new StringJoiner(",");

        if(user.getUsername() != null){
            params.add(new Pair(user.getUsername(), Types.VARCHAR));
            valuesToSet.add("username=?");
        }
        if(user.getEmail() != null){
            params.add(new Pair(user.getEmail(), Types.VARCHAR));
            valuesToSet.add("email=?");
        }
        if(user.getDescription() != null){
            params.add(new Pair(user.getDescription(), Types.VARCHAR));
            valuesToSet.add("description=?");
        }
        if(user.getPicture() != null){
            params.add(new Pair(user.getPicture(), Types.VARCHAR));
            valuesToSet.add("picture=?");
        }
        if(user.getPassword() != null){
            params.add(new Pair(user.getPicture(), Types.VARCHAR));
            valuesToSet.add("password=?");
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

        ps.setInt(i++, user.getId());
        return ps.executeUpdate();
    }

    public int create(List<User> users) throws SQLException {
        String columnToSet = "username, password, email, description, picture";
        String statement = String.format("INSERT INTO user (%s) VALUES ", columnToSet);
        String questionMarks = "(?,?,?,?,?)";

        ArrayList<Pair<Object, Types>> params = new ArrayList<>();
        StringJoiner set = new StringJoiner(",");

        for(User user : users){
            params.add(new Pair(user.getUsername(), Types.VARCHAR));
            params.add(new Pair(user.getPassword(), Types.VARCHAR));
            params.add(new Pair(user.getEmail(), Types.VARCHAR));
            params.add(new Pair(user.getDescription(), Types.VARCHAR));
            params.add(new Pair(user.getPicture(), Types.VARCHAR));
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
        String statement = "DELETE FROM user WHERE id IN (%s)";

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

    public int create(User user) throws SQLException {
        return create(List.of(user));
    }

    public int delete(int id) throws SQLException {
        return delete(new int[]{id});
    }

    public User get(int id) throws SQLException, NamingException {
        return fetch(new UserSpecification().byId(id)).get(0);
    }
}