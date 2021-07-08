package model.ban;

import model.persistence.AbstractMapper;
import model.persistence.SQL_TriConsumer;
import model.section.Section;
import model.user.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BanMapper implements AbstractMapper<Ban> {

    static LinkedHashMap<String, SQL_TriConsumer<User>> mapUser = new LinkedHashMap<>(){{
        put("user_id",        (u,s,rs) -> u.setId(rs.getInt(s)));
        put("username",       (u,s,rs) -> u.setUsername(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<User>> mapAdmin = new LinkedHashMap<>(){{
        put("admin_id",             (u,s,rs) -> u.setId(rs.getInt(s)));
        put("admin_username",       (u,s,rs) -> u.setUsername(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Section>> mapSection = new LinkedHashMap<>(){{
        put("section_id",         (sec,s,rs) -> sec.setId(rs.getInt(s)));
        put("section_name",       (sec,s,rs) -> sec.setName(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Ban>> mapBan = new LinkedHashMap<>(){{
        put("start_time",        (b,s,rs) -> b.setStartTime(rs.getTimestamp(s).toInstant()));
        put("end_time",          (b,s,rs) -> b.setEndTime(rs.getTimestamp(s) == null ? null : rs.getTimestamp(s).toInstant()));
        put("is_global",         (b,s,rs) -> b.setGlobal(rs.getBoolean(s)));
        put("ban_id",            (b,s,rs) -> b.setId(rs.getInt(s)));
    }};

    public List<Ban> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<Ban> beans = new ArrayList<>();
        HashMap<Integer, User> users = new HashMap<>();
        HashMap<Integer, Section> sections = new HashMap<>();
        HashMap<Integer, User> admins = new HashMap<>();
        while (rs.next()) {
            Ban ban = new Ban();

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
            ban.setUser(user);

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
            ban.setSection(section);

            User admin = null;
            if (columns.contains("admin_id")) {
                admin = admins.get(rs.getInt("admin_id"));
                if (admin == null) {
                    admin = new User();
                    for (String column : columns) {
                        SQL_TriConsumer<User> consumer = mapAdmin.get(column);
                        if(consumer != null)
                            consumer.accept(admin, column, rs);
                    }
                    admins.put(admin.getId(), admin);
                }
            }
            ban.setAdmin(admin);

            for (String column : columns){
                SQL_TriConsumer<Ban> consumer = mapBan.get(column);
                if(consumer != null)
                    consumer.accept(ban, column, rs);
            }

            beans.add(ban);
        }
        return beans;
    }
}
