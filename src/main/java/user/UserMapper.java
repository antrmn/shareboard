package user;


import persistence.AbstractMapper;

import java.util.HashMap;
import java.util.Map;


public class UserMapper extends AbstractMapper<User> {

    static Map<String, SQL_TriConsumer<User>> map = new HashMap<>(){{
        put("id",                (u,s,rs) -> u.setId(rs.getInt(s)));
        put("username",          (u,s,rs) -> u.setUsername(rs.getString(s)));
        put("creation_date",     (u,s,rs) -> u.setCreationDate(rs.getTimestamp(s).toInstant()));
        put("password",          (u,s,rs) -> u.setPassword(rs.getString(s)));
        put("description",       (u,s,rs) -> u.setDescription(rs.getString(s)));
        put("picture",           (u,s,rs) -> u.setPicture(rs.getString(s)));
        put("email",             (u,s,rs) -> u.setEmail(rs.getString(s)));
        put("is_admin",          (u,s,rs) -> u.setAdmin(rs.getBoolean(s)));
    }};

    public UserMapper() {
        super(map);
    }

    @Override
    protected User instantiate() {
        return new User();
    }
}
