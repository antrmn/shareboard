package ban;

import persistence.Specification;
import persistence.StatementSetters;
import post.Post;
import user.User;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class BanDAO {
    private static final BanMapper bm = new BanMapper();
    private final Connection con;

    public BanDAO(Connection con) throws SQLException {
        this.con = con;
    }

    public List<Ban> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM ban %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                                     specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                                    .executeQuery();
        List<Ban> bans = bm.toBeans(rs);
        ps.close();
        rs.close();
        return bans;
    }

    public int update(Ban ban) throws SQLException {
        String statement = "UPDATE ban SET %s WHERE id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        if(ban.getAdmin() != null && ban.getAdmin().getId() != null){
            params.add(new Pair<>(ban.getAdmin().getId(), Types.INTEGER));
            valuesToSet.add("admin_id=?");
        }
        if(ban.getUser() != null && ban.getUser().getId() != null){
            params.add(new Pair<>(ban.getUser().getId(), Types.INTEGER));
            valuesToSet.add("user_id=?");
        }
        if(ban.getSection() != null && ban.getSection().getId() != null){
            params.add(new Pair<>(ban.getSection().getId(), Types.INTEGER));
            valuesToSet.add("section_id=?");
        }
        if(ban.getStartTime() != null){
            params.add(new Pair<>(Timestamp.from(ban.getStartTime()), Types.TIMESTAMP));
            valuesToSet.add("start_time=?");
        }
        if(ban.getEndTime() != null){
            params.add(new Pair<>(Timestamp.from(ban.getEndTime()), Types.TIMESTAMP));
            valuesToSet.add("end_time=?");
        }
        if(ban.getGlobal() != null){
            params.add(new Pair<>(ban.getGlobal(), Types.BOOLEAN));
            valuesToSet.add("is_global=?");
        }

        params.add(new Pair<>(ban.getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public List<Integer> insert(List<Ban> bans) throws SQLException {
        String statement = "INSERT INTO ban (%s) VALUES %s";
        String columns = "admin_id, section_id, user_id, start_time, end_time, is_global";
        String questionMarks = "(?,?,?,?,?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(Ban ban : bans) {
            params.add(new Pair<>(ban.getAdmin() == null ? null : ban.getAdmin().getId(), Types.INTEGER));
            params.add(new Pair<>(ban.getSection() == null ? null : ban.getSection().getId(), Types.INTEGER));
            params.add(new Pair<>(ban.getUser() == null ? null : ban.getUser().getId(), Types.INTEGER));
            params.add(new Pair<>(ban.getStartTime() == null ? null : Timestamp.from(ban.getStartTime()), Types.TIMESTAMP));
            params.add(new Pair<>(ban.getEndTime() == null ? null : Timestamp.from(ban.getEndTime()), Types.TIMESTAMP));
            params.add(new Pair<>(ban.getGlobal(), Types.BOOLEAN));
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
        String statement = "DELETE FROM ban WHERE %s LIMIT ?";

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

}