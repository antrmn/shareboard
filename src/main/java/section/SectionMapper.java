package section;


import persistence.AbstractMapper;
import persistence.SQL_TriConsumer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SectionMapper implements AbstractMapper<Section> {
    static HashMap<String, SQL_TriConsumer<Section>> map = new HashMap<>(){{
        put("id",                (p,s,rs) -> p.setId(rs.getInt(s)));
        put("description",       (p,s,rs) -> p.setDescription(rs.getString(s)));
        put("name",              (p,s,rs) -> p.setName(rs.getString(s)));
        put("picture",           (p,s,rs) -> p.setPicture(rs.getString(s)));
    }};

    public List<Section> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<Section> beans = new ArrayList<>();

        while (rs.next()) {
            Section bean = new Section();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String column = rsmd.getColumnLabel(i);
                SQL_TriConsumer<Section> setter = map.get(column);
                if (setter != null) {
                    setter.accept(bean, column, rs);
                }
            }
            beans.add(bean);
        }
        return beans;
    }
}
