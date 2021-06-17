package user;

import persistence.Specification;
import persistence.StatementSetters;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class UserDAO{
    private static final UserMapper um = new UserMapper();
    private final Connection con;

    public UserDAO(Connection con) {
        this.con = con;
    }

    public List<User> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM v_user AS user %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                .executeQuery();
        List<User> users = um.toBeans(rs);
        ps.close();
        rs.close();
        return users;
    }

    public int update(User user) throws SQLException {
        String statement = "UPDATE user SET %s WHERE id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        if(user.getUsername() != null){
            params.add(new Pair<>(user.getUsername(), Types.VARCHAR));
            valuesToSet.add("username=?");
        }
        if(user.getEmail() != null){
            params.add(new Pair<>(user.getEmail(), Types.VARCHAR));
            valuesToSet.add("email=?");
        }
        if(user.getDescription() != null){
            params.add(new Pair<>(user.getDescription(), Types.VARCHAR));
            valuesToSet.add("description=?");
        }
        if(user.getPicture() != null){
            params.add(new Pair<>(user.getPicture(), Types.VARCHAR));
            valuesToSet.add("picture=?");
        }
        if(user.getPassword() != null){
            params.add(new Pair<>(user.getPassword().getPassword(), Types.BINARY));
            valuesToSet.add("password=?");
            params.add(new Pair<>(user.getPassword().getSalt(), Types.BINARY));
            valuesToSet.add("salt=?");
        }
        params.add(new Pair<>(user.getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }
    
    public List<Integer> insert(List<User> users) throws SQLException {
        String statement = "INSERT INTO user (%s) VALUES %s";
        String columns = "username, password, salt, email, description, picture";
        String questionMarks = "(?,?,?,?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(User user : users) {
            params.add(new Pair<>(user.getUsername(), Types.VARCHAR));
            params.add(new Pair<>(user.getPassword().getPassword(), Types.BINARY));
            params.add(new Pair<>(user.getPassword().getSalt(), Types.BINARY));
            params.add(new Pair<>(user.getEmail(), Types.VARCHAR));
            params.add(new Pair<>(user.getDescription(), Types.VARCHAR));
            params.add(new Pair<>(user.getPicture(), Types.VARCHAR));
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
        String statement = "DELETE FROM user WHERE %s LIMIT ?";

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

    public int delete(int id) throws SQLException {
        return delete(List.of(id));
    }

    public User get(int id, boolean getPassword) throws SQLException{
        UserSpecificationBuilder usb = new UserSpecificationBuilder().byId(id);
        if(getPassword)
            usb.getPassword();
        List<User> singleton = fetch(usb.build());
        return singleton.isEmpty() ? null : singleton.get(0);
        //TODO: propaga in altri DAO
    }

    public User get(String username, boolean getPassword) throws SQLException{
        UserSpecificationBuilder usb = new UserSpecificationBuilder().byUsernameExact(username);
        if(getPassword)
            usb.getPassword();
        List<User> singleton = fetch(usb.build());
        return singleton.isEmpty() ? null : singleton.get(0);
    }

    public int insert(User user) throws SQLException {
        return insert(List.of(user)).size();
    }
}