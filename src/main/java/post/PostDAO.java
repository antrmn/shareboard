package post;

import util.Pair;
import util.SetNull;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;

public class PostDAO {

    private Connection con;

    public PostDAO(Connection con) throws SQLException {
        if( (this.con = con) == null)
            throw new SQLException("Null connection");
    }

    
    public List<Post> fetch(PostSpecification specification) throws SQLException {
        String query = String.format("SELECT %s FROM v_post AS post %s %s ORDER BY %s %s LIMIT ? OFFSET ?",
                specification.columnsToRetrieve, specification.joins, specification.wheres,
                specification.sortBy, specification.sortOrder);

        List<Pair<Object,Integer>> params = specification.params;

        PreparedStatement ps = con.prepareStatement(query);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        ps.setInt(i++, specification.limit);
        ps.setInt(i++, specification.offset);

        ResultSet rs = ps.executeQuery();
        ArrayList<Post> posts = new ArrayList<>();
        while(rs.next()){
            posts.add(PostMapper.toBean(rs));
        }

        return posts;
    }

    public int update(Post post) throws SQLException {
        String statement = "UPDATE post SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner valuesToSet = new StringJoiner(",");

        if(post.getTitle() != null){
            params.add(new Pair(post.getTitle(), Types.VARCHAR));
            valuesToSet.add("title=?");
        }
        if(post.getContent() != null){
            params.add(new Pair(post.getContent(), Types.VARCHAR));
            valuesToSet.add("content=?");
        }
        if(post.getType() != null){
            params.add(new Pair(post.getType().name(), Types.VARCHAR));
            valuesToSet.add("type=?");
        }
        if(post.getSection() != null && post.getSection().getId() != null){
            params.add(new Pair(post.getSection().getId(), Types.INTEGER));
            valuesToSet.add("section_id=?");
        }
        if(post.getAuthor() != null && post.getAuthor().getId() != null){
            params.add(new Pair(post.getAuthor().getId(), Types.VARCHAR));
            valuesToSet.add("author_id=?");
        }

        if(valuesToSet.length()==0){
            throw new RuntimeException("Empty SET clause");
        }

        statement = String.format(statement,valuesToSet);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){
            if(param.getLeft() == SetNull.NULL){
                ps.setNull(i++, param.getRight());
            } else{
                ps.setObject(i++, param.getLeft(), param.getRight());
            }
        }

        ps.setInt(i++, post.getId());
        return ps.executeUpdate();
    }

    public int create(List<Post> posts) throws SQLException {
        String columnToSet = "title, content, type, author_id, section_id";
        String statement = String.format("INSERT INTO post (%s) VALUES ", columnToSet);
        String questionMarks = "(?,?,?,?,?)";

        ArrayList<Pair<Object, Types>> params = new ArrayList<>();
        StringJoiner set = new StringJoiner(",");

        for(Post post : posts){
            params.add(new Pair(post.getTitle(), Types.VARCHAR));
            params.add(new Pair(post.getContent(), Types.VARCHAR));
            params.add(new Pair(post.getType() == null ? null : post.getType().name(), Types.VARCHAR));
            params.add(new Pair(post.getAuthor() == null ? null : post.getAuthor().getId(), Types.INTEGER));
            params.add(new Pair(post.getSection() == null ? null : post.getSection().getId(), Types.INTEGER));
            set.add(questionMarks);
        }

        if(set.length()==0){
            throw new RuntimeException("Empty VALUES clause");
        }

        statement = String.format(statement, set);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Types> param : params){
            ps.setObject(i++, param.getLeft(), (SQLType) param.getRight());
        }

        return ps.executeUpdate();
    }

    public int delete(int[] ids) throws SQLException {
        String statement = "DELETE FROM post WHERE id IN (%s)";

        StringJoiner sj = new StringJoiner(",");
        for (int id : ids){
            sj.add("?");
        }

        statement = String.format(statement, sj);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (int id : ids){
            ps.setInt(i++, id);
        }

        return ps.executeUpdate(statement);
    }

    public int create(Post post) throws SQLException {
        return create(List.of(post));
    }

    public int delete(int id) throws SQLException {
        return delete(new int[]{id});
    }

    public Post get(int id) throws SQLException, NamingException {
        return fetch(new PostSpecification().byId(id)).get(0);
    }
}