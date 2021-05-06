package post;

import model.ConPool;
import model.User;
import section.Section;
import util.Pair;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class MySQLPostDAO implements PostDAO{
    public List<Post> fetch(){ return null;}

    public List<Post> fetch(Integer loggedUserId, Integer sectionFollowedByUserId, Integer sectionId, Integer authorId,
                            String titleLike, String contentLike, Date from, Date to, String authorUsername,
                            Post.Type type, SortCriterion sortBy, Boolean descending, Integer limit, Integer offset)
            throws SQLException, NamingException {
        //TODO: HASHMAP? riduci i parametri. <Where, parametro, tipo> (e <Join, parametro, tipo>).
        String columnToRetrieve = "id, title, content, type, creation_date, " +
                                  "author_id, username, is_admin, section_id, section_name, " +
                                  "votes, n_comments";



        StringJoiner joins = new StringJoiner("\n");
        StringJoiner wheres = new StringJoiner(" AND ", " WHERE ", "").setEmptyValue("");
        LinkedList<Pair<Object, Integer>> params = new LinkedList<>();

        if(loggedUserId != null){
            columnToRetrieve += ", submitted_vote ";
            joins.add("LEFT JOIN " +
                    "(SELECT post_id, vote AS submitted_vote, user_id FROM post_vote " +
                    "JOIN user ON user_id=user.id " +
                    "WHERE user_id=?) " +
                    "AS v1 " +
                    "ON v1.post_id = post.id ");
            params.add(new Pair<>(loggedUserId, java.sql.Types.INTEGER));
        }
        if(sectionFollowedByUserId != null){
            joins.add("INNER JOIN (follow as f) ON f.section_id = sec.id ");
            wheres.add("f.user_id=?");
            params.add(new Pair<>(sectionFollowedByUserId, java.sql.Types.INTEGER));
        }
        if(sectionId != null){
            wheres.add("sec.id = ? ");
            params.add(new Pair<>(sectionId, java.sql.Types.INTEGER));
        }
        if(authorId != null){
            wheres.add("user.id = ? ");
            params.add(new Pair<>(authorId, java.sql.Types.INTEGER));
        }
        if(titleLike != null){
            wheres.add("post.title LIKE ?");
            params.add(new Pair<>(titleLike, java.sql.Types.VARCHAR));
        }
        if(contentLike != null){
            wheres.add("post.content LIKE ?");
            params.add(new Pair<>(contentLike, java.sql.Types.VARCHAR));
        }
        if(from != null){
            wheres.add("post.creation_date >= ?");
            params.add(new Pair<>(from, Types.TIMESTAMP));
        }
        if(to != null){
            wheres.add("post.creation_date <= ?");
            params.add(new Pair<>(to, Types.TIMESTAMP));
        }
        if(authorUsername != null){
            wheres.add("user.username = ?");
            params.add(new Pair<>(sectionId, Types.TIMESTAMP));
        }
        if(type != null){
            wheres.add("post.type = ?");
            params.add(new Pair<>(type.name(), Types.VARCHAR));
        }

        String stringSortBy;
        if(sortBy == null||sortBy==SortCriterion.DATE){
            stringSortBy = "post.creation_date";
        } else{
            stringSortBy = "votes";
        }

        if(descending == null)
            descending = false;
        String query = String.format("SELECT DISTINCT %s FROM v_post AS post %s %s ORDER BY %s %s LIMIT ? OFFSET ?",
                columnToRetrieve, joins, wheres, stringSortBy, descending?"DESC":"ASC");

        if(limit == null || limit < 0){
            limit = 50;
        }
        if(offset == null || offset < 0){
            offset = 0;
        }
        params.add(new Pair<>(limit, Types.INTEGER));
        params.add(new Pair<>(offset, Types.INTEGER));

        Connection con = ConPool.getConnection();
        PreparedStatement ps = con.prepareStatement(query);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        ResultSet rs = ps.executeQuery();
        ArrayList<Post> posts = new ArrayList<>();
        while(rs.next()){
            Post post = new Post();
            post.setId(rs.getInt("id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setType(Post.Type.valueOf(rs.getString("type")));
            post.setCreationDate(rs.getDate("creation_date"));
            post.setVotes(rs.getInt("votes"));
            post.setnComments(rs.getInt("n_comments"));
            User author = new User();
            author.setId(rs.getInt("author_id"));
            author.setUsername(rs.getString("username"));
            author.setAdmin(rs.getBoolean("is_admin"));
            post.setAuthor(author);
            Section section = new Section();
            section.setId(rs.getInt("section_id"));
            section.setName(rs.getString("section_name"));
            post.setSection(section);
            posts.add(post);
        }
        return posts;
    }

    public List<Post> get() {
        return null;
    }

    @Override
    public int create() {
        return 0;
    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public int delete() {
        return 0;
    }
}