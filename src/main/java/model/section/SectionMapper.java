package model.section;


import model.persistence.AbstractMapper;
import model.persistence.SQL_TriConsumer;

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
        put("banner",            (p,s,rs) -> p.setBanner(rs.getString(s)));
    }};

    public List<Section> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<Section> beans = new ArrayList<>();
        while (rs.next()) {
            Section bean = new Section();
            for (String column : columns) {
                SQL_TriConsumer<Section> consumer = map.get(column);
                if(consumer != null)
                    consumer.accept(bean, column, rs);
            }
            beans.add(bean);
        }
        return beans;
    }
}
