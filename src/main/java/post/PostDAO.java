package post;

import util.Pair;
import util.SetNull;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;

public class PostDAO {

    private Connection con;

    public PostDAO(Connection con) throws SQLException {
        this.con = con;
    }

    private List<String> readableColumns = List.of("id", "title", "content", "type", "creation_date",
            "user.id", "username", "is_admin", "section.id", "section.name", "votes", "n_comments", "v1.vote AS vote" );

    private List<String> updatableColumns = List.of("title", "content", "type", "author_id", "section_id");

    private String selectStatement = "SELECT %s FROM v_post AS post " +
                                        "JOIN v_user AS user ON author_id = user.id " +
                                        "JOIN section ON section_id = section.id " +
                                        " %s %s " +
                                     "ORDER BY %s %s LIMIT ? OFFSET ?";

    private String view = "v_post";
    private String table = "post";

    private String fillUpdateStatement(Post post, List<Pair<Object, Integer>> params){
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
        return valuesToSet.toString();
    }
    private void fillInsertStatement(Post post, List<Pair<Object, Integer>> params){
        params.add(new Pair(post.getTitle(), Types.VARCHAR));
        params.add(new Pair(post.getContent(), Types.VARCHAR));
        params.add(new Pair(post.getType() == null ? null : post.getType().name(), Types.VARCHAR));
        params.add(new Pair(post.getAuthor() == null ? null : post.getAuthor().getId(), Types.INTEGER));
        params.add(new Pair(post.getSection() == null ? null : post.getSection().getId(), Types.INTEGER));
    }
    
    public List<Post> fetch(PostSpecification specification) throws SQLException {
        String query = "SELECT %s FROM %s %s %s ORDER BY %s %s LIMIT ? OFFSET ?";
        String.format(query, readableColumns, viewTable, specification.joins, specification.wheres,
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
        String statement = "UPDATE %s SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        String valuesToSet = fillUpdateStatement(post, params);

        String.format(statement, table, valuesToSet);

        if(valuesToSet.isBlank() || params.isEmpty()){
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
        String statement = "INSERT INTO %s (%s) VALUES %s";

        StringJoiner _questionMarks = new StringJoiner(",","(",")");
        for(int i=0; i<updatableColumns.size(); i++)
            _questionMarks.add("?");
        String questionMarks = _questionMarks.toString();

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner values = new StringJoiner(",");
        for(Post post : posts){
            fillInsertStatement(post, params);
            values.add(questionMarks);
        }

        if(values.length()==0){
            throw new RuntimeException("Empty VALUES clause");
        }

        statement = String.format(statement, table, updatableColumns, values);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        return ps.executeUpdate();
    }

    public int delete(int[] ids) throws SQLException {
        String statement = "DELETE FROM %s WHERE id IN (%s)";

        StringJoiner sj = new StringJoiner(",");
        for (int id : ids){
            sj.add("?");
        }

        statement = String.format(statement, table,sj);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (int id : ids){
            ps.setInt(i++, id);
        }

        return ps.executeUpdate();
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