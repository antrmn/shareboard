package section;


import post.Post;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;


public class SectionMapper {

    @FunctionalInterface
    private interface SQL_Consumer<T>{
        public void accept(T t) throws SQLException;
    }

    private Section s;
    private ResultSet rs;

    private final Map<String, SQL_Consumer<String>> map = Map.of(
            "section_id",                x -> s.setId(rs.getInt(x)),
            "section_description",       x -> s.setDescription(rs.getString(x)),
            "section_name",              x -> s.setName(rs.getString(x)),
            "section_picture",           x -> s.setPicture(rs.getString(x))
    );

    private SectionMapper(Section s, ResultSet rs){
        this.s = s;
        this.rs = rs;
    }

    public static Section toBean(ResultSet rs) throws SQLException{
        SectionMapper sm = new SectionMapper(new Section(), rs);
        ResultSetMetaData rsmd = rs.getMetaData();
        Section s = new Section();

        for(int i=1; i<=rsmd.getColumnCount(); i++){
            String column = rsmd.getColumnName(i);
            SQL_Consumer<String> setter = sm.map.get(column);
            if(setter != null){
                setter.accept(column);
            }
        }

        return s;
    }
}
