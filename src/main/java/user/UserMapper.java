package user;


import comment.Comment;
import post.PostMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;


public class UserMapper {

    @FunctionalInterface
    private interface SQL_Consumer<T>{
        void accept(T t) throws SQLException;
    }

    private User u;
    private ResultSet rs;

    private final Map<String, SQL_Consumer<String>> map = Map.of(
            "user_id",                x -> u.setId(rs.getInt(x)),
            "username",               x -> u.setUsername(rs.getString(x)),
            "user_creation_date",     x -> u.setCreationDate(rs.getTimestamp(x).toInstant()),
            "user_password",          x -> u.setPassword(rs.getString(x)),
            "user_description",       x -> u.setDescription(rs.getString(x)),
            "user_picture",           x -> u.setPicture(rs.getString(x)),
            "user_email",             x -> u.setEmail(rs.getString(x)),
            "is_admin",               x -> u.setAdmin(rs.getBoolean(x))
    );

    private UserMapper(User u, ResultSet rs){
        this.u = u;
        this.rs = rs;
    }

    public static User toBean(ResultSet rs) throws SQLException{
        UserMapper um = new UserMapper(new User(), rs);
        ResultSetMetaData rsmd = rs.getMetaData();

        for(int i=1; i<=rsmd.getColumnCount(); i++){
            String column = rsmd.getColumnLabel(i);
            SQL_Consumer<String> setter = um.map.get(column);
            if(setter != null){
                setter.accept(column);
            }
        }
        return um.u;
    }
}
