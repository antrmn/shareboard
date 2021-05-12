package user;

import persistence.GenericDAO;
import util.Pair;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class UserDAO extends GenericDAO<User, UserMapper> {


    public UserDAO(Connection con) {
        super(con,
                List.of("user.id", "user.username", "user.email", "user.description", "user.picture",
                        "user.creation_date", "user.is_admin"),
                List.of("username", "email", "description", "picture"),
                "v_user",
                "user",
                new UserMapper()
        );
    }

    @Override
    protected String fillUpdateStatement(User user, List<Pair<Object, Integer>> params) {
        StringJoiner valuesToSet = new StringJoiner(",");

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
            params.add(new Pair<>(user.getPicture(), Types.VARCHAR));
            valuesToSet.add("password=?");
        }

        if(valuesToSet.length()==0){
            throw new RuntimeException("Empty SET clause");
        }
        params.add(new Pair<>(user.getId(), Types.INTEGER));
        return valuesToSet.toString();
    }

    @Override
    protected void fillInsertStatement(User user, List<Pair<Object, Integer>> params) {
        params.add(new Pair<>(user.getUsername(), Types.VARCHAR));
        params.add(new Pair<>(user.getPassword(), Types.VARCHAR));
        params.add(new Pair<>(user.getEmail(), Types.VARCHAR));
        params.add(new Pair<>(user.getDescription(), Types.VARCHAR));
        params.add(new Pair<>(user.getPicture(), Types.VARCHAR));
    }
}