package model.post;

import model.persistence.Specification;
import model.persistence.StatementSetters;
import util.Pair;

import java.sql.*;
import java.time.Instant;
import java.util.*;


public class PostDAO {
    private static final PostMapper pm = new PostMapper();
    private final Connection con;

    public PostDAO(Connection con) throws SQLException {
        this.con = con;
    }

    public List<Post> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM v_post AS post %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                                     specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                                    .executeQuery();
        List<Post> posts = pm.toBeans(rs);
        ps.close();
        rs.close();
        return posts;
    }

    public int update(Post post) throws SQLException {
        String statement = "UPDATE post SET %s WHERE id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        if(post.getTitle() != null){
            params.add(new Pair<>(post.getTitle(), Types.VARCHAR));
            valuesToSet.add("title=?");
        }
        if(post.getContent() != null){
            params.add(new Pair<>(post.getContent(), Types.VARCHAR));
            valuesToSet.add("content=?");
        }
        if(post.getType() != null){
            params.add(new Pair<>(post.getType().name(), Types.VARCHAR));
            valuesToSet.add("type=?");
        }
        if(post.getSection() != null && post.getSection().getId() != null){
            params.add(new Pair<>(post.getSection().getId(), Types.INTEGER));
            valuesToSet.add("section_id=?");
        }
        if(post.getAuthor() != null && post.getAuthor().getId() != null){
            params.add(new Pair<>(post.getAuthor().getId(), Types.VARCHAR));
            valuesToSet.add("author_id=?");
        }
        params.add(new Pair<>(post.getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public List<Integer> insert(List<Post> posts) throws SQLException {
        String statement = "INSERT INTO post (%s) VALUES %s";
        String columns = "title, content, type, author_id, section_id";
        String questionMarks = "(?,?,?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(Post post : posts) {
            params.add(new Pair<>(post.getTitle(), Types.VARCHAR));
            params.add(new Pair<>(post.getContent(), Types.VARCHAR));
            params.add(new Pair<>(post.getType() == null ? null : post.getType().name(), Types.VARCHAR));
            params.add(new Pair<>(post.getAuthor() == null ? null : post.getAuthor().getId(), Types.INTEGER));
            params.add(new Pair<>(post.getSection() == null ? null : post.getSection().getId(), Types.INTEGER));
            questionMarksJoiner.add(questionMarks);
        }

        statement = String.format(statement, columns, questionMarksJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        StatementSetters.setParameters(ps, params).executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        List<Integer> ids = new ArrayList<>();
        while(rs.next()){
            ids.add(rs.getInt(1));
        }
        ps.close();
        rs.close();
        return ids;
    }

    public int delete(List<Integer> ids) throws SQLException {
        String statement = "DELETE FROM post WHERE %s LIMIT ?";

        StringJoiner whereJoiner = new StringJoiner(" OR ");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        for (Integer id : ids) {
            params.add(new Pair<>(id, Types.INTEGER));
            whereJoiner.add(" id=? ");
        }
        params.add(new Pair<>(ids.size(), Types.INTEGER)); //Il LIMIT sara' pari ai parametri inseriti per evitare imprevisti

        statement = String.format(statement, whereJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        StatementSetters.setParameters(ps,params);
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        return rowsDeleted;
    }

    public Map<String, Integer> getPostCountBySection() throws SQLException {
        String query = "SELECT COUNT(*),section.name FROM post INNER JOIN section ON post.section_id = section.id GROUP BY(section.name)";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        Map<String, Integer> data = new HashMap<>();
        while(rs.next()){
            String section = rs.getString(2);
            Integer postCount = rs.getInt(1);
            data.put(section, postCount);
        }
        ps.close();
        rs.close();
        return data;
    }

    public Map<Instant, Integer> getPostCountByDays() throws SQLException {
        String query = "SELECT COUNT(*), creation_date FROM post where post.creation_date >= now() - interval 5 day GROUP BY CAST(post.creation_date AS DATE) ORDER BY post.creation_date DESC;";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        Map<Instant, Integer> data = new HashMap<>();
        while(rs.next()){
            Instant date = rs.getTimestamp(2).toInstant();
            Integer postCount = rs.getInt(1);
            data.put(date, postCount);
        }
        ps.close();
        rs.close();
        return data;
    }

    /*--- Shorthands ---*/

    public int delete(int id) throws SQLException {
        return delete(List.of(id));
    }


    public Post get(int id, int loggedUserId) throws SQLException{
        PostSpecificationBuilder psb = new PostSpecificationBuilder().byId(id).loggedUser(loggedUserId);
        List<Post> singleton = fetch(psb.build());
        return singleton.isEmpty() ? null : singleton.get(0);
    }

    public Post get(int id) throws SQLException{
        return get(id, -1);
    }

    public int insert(Post post) throws SQLException {
        List<Integer> single = insert(List.of(post));
        return single.isEmpty() ? null : single.get(0);
    }
}