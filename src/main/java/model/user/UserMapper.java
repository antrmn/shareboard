package model.user;


import model.persistence.AbstractMapper;
import model.persistence.SQL_TriConsumer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserMapper implements AbstractMapper<User> {

    static Map<String, SQL_TriConsumer<User>> map = new HashMap<>() {{
        put("id",               (u, s, rs) -> u.setId(rs.getInt(s)));
        put("username",         (u, s, rs) -> u.setUsername(rs.getString(s)));
        put("creation_date",    (u, s, rs) -> u.setCreationDate(rs.getTimestamp(s).toInstant()));
        put("password",         (u, s, rs) -> u.getPassword().setPassword(rs.getBytes(s)));
        put("salt",             (u, s, rs) -> u.getPassword().setSalt(rs.getBytes(s)));
        put("description",      (u, s, rs) -> u.setDescription(rs.getString(s)));
        put("picture",          (u, s, rs) -> u.setPicture(rs.getString(s)));
        put("email",            (u, s, rs) -> u.setEmail(rs.getString(s)));
        put("is_admin",         (u, s, rs) -> u.setAdmin(rs.getBoolean(s)));
    }};

    public List<User> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<User> beans = new ArrayList<>();
        while (rs.next()) {
            User bean = new User();

            if(columns.contains("salt") || columns.contains("password")){
                bean.setPassword(new HashedPassword());
            }

            for (String column : columns) {
                SQL_TriConsumer<User> consumer = map.get(column);
                if(consumer != null)
                    consumer.accept(bean, column, rs);
            }
            beans.add(bean);
        }
        return beans;
    }
}
