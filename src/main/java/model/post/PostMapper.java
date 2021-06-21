package model.post;

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

public class PostMapper implements AbstractMapper<Post> {

    static LinkedHashMap<String, SQL_TriConsumer<User>> mapAuthor = new LinkedHashMap<>(){{
        put("author_id",      (u,s,rs) -> u.setId(rs.getInt(s)));
        put("author_username",(u,s,rs) -> u.setUsername(rs.getString(s)));
        put("is_admin",       (u,s,rs) -> u.setAdmin(rs.getBoolean(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Section>> mapSection = new LinkedHashMap<>(){{
        put("section_id",     (sec,s,rs) -> sec.setId(rs.getInt(s)));
        put("section_name",   (sec,s,rs) -> sec.setName(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Post>> mapPost = new LinkedHashMap<>(){{
             put("id",             (p,s,rs) -> p.setId(rs.getInt(s)));
             put("title",          (p,s,rs) -> p.setTitle(rs.getString(s)));
             put("content",        (p,s,rs) -> p.setContent(rs.getString(s)));
             put("type",           (p,s,rs) -> p.setType(Post.Type.valueOf(rs.getString(s))));
             put("creation_date",  (p,s,rs) -> p.setCreationDate(rs.getTimestamp(s).toInstant()));
             put("votes",          (p,s,rs) -> p.setVotes(rs.getInt(s)));
             put("n_comments",     (p,s,rs) -> p.setnComments(rs.getInt(s)));
             put("vote",           (p,s,rs) -> p.setVote(rs.getInt(s)));
    }};

    public List<Post> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<Post> beans = new ArrayList<>();
        HashMap<Integer, User> authors = new HashMap<>();
        HashMap<Integer, Section> sections = new HashMap<>();
        while (rs.next()) {
            Post post = new Post();

            User author = null;
            if (columns.contains("author_id")) {
                author = authors.get(rs.getInt("author_id"));
                if (author == null) {
                    author = new User();
                    for (String column : columns) {
                        SQL_TriConsumer<User> consumer = mapAuthor.get(column);
                        if(consumer != null)
                            consumer.accept(author, column, rs);
                    }
                    authors.put(author.getId(), author);
                }
            }
            post.setAuthor(author);

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
            post.setSection(section);

            for (String column : columns) {
                SQL_TriConsumer<Post> consumer = mapPost.get(column);
                if(consumer != null)
                    consumer.accept(post, column, rs);
            }
            beans.add(post);
        }
        return beans;
    }
}
