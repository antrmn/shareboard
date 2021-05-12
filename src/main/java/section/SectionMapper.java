package section;


import persistence.AbstractMapper;

import java.util.HashMap;


public class SectionMapper extends AbstractMapper<Section> {
    static HashMap<String, SQL_TriConsumer<Section, String>> map = new HashMap<>(){{
        put("id",                (p,s,rs) -> p.setId(rs.getInt(s)));
        put("description",       (p,s,rs) -> p.setDescription(rs.getString(s)));
        put("name",              (p,s,rs) -> p.setName(rs.getString(s)));
        put("picture",           (p,s,rs) -> p.setPicture(rs.getString(s)));
    }};

    public SectionMapper() {
        super(map);
    }

    @Override
    protected Section instantiate() {
        return new Section();
    }
}
