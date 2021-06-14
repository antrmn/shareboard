package follow;

import persistence.AbstractMapper;
import persistence.SQL_TriConsumer;
import section.Section;
import user.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FollowMapper implements AbstractMapper<Follow> {

    static LinkedHashMap<String, SQL_TriConsumer<User>> mapUser = new LinkedHashMap<>(){{
        put("user_id",      (u,s,rs) -> u.setId(rs.getInt(s)));
        put("username",     (u,s,rs) -> u.setUsername(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Section>> mapSection = new LinkedHashMap<>(){{
        put("section_id",     (sec,s,rs) -> sec.setId(rs.getInt(s)));
        put("section_name",   (sec,s,rs) -> sec.setName(rs.getString(s)));
    }};

    public List<Follow> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<Follow> beans = new ArrayList<>();
        HashMap<Integer, User> users = new HashMap<>();
        HashMap<Integer, Section> sections = new HashMap<>();
        while (rs.next()) {
            Follow follow = new Follow();

            User user = null;
            if (columns.contains("user_id")) {
                user = users.get(rs.getInt("user_id"));
                if (user == null) {
                    user = new User();
                    for (String column : columns) {
                        SQL_TriConsumer<User> consumer = mapUser.get(column);
                        if(consumer != null)
                            consumer.accept(user, column, rs);
                    }
                    users.put(user.getId(), user);
                }
            }
            follow.setUser(user);

            Section section = null;
            if (columns.contains("section_id")) {
                section = sections.get(rs.getInt("section_id"));
                if (section == null) {
                    section = new Section();
                    for (String column : columns) {
                        SQL_TriConsumer<Section> consumer = mapSection.get(column);
                        if(consumer != null)
                            consumer.accept(section, column, rs);
                    }
                    sections.put(section.getId(), section);
                }
            }
            follow.setSection(section);

            beans.add(follow);
        }
        return beans;
    }
}
