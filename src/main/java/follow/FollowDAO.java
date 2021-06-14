package follow;

import persistence.Specification;
import persistence.StatementSetters;
import section.Section;
import user.User;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class FollowDAO {
    private static final FollowMapper fm = new FollowMapper();
    private final Connection con;

    public FollowDAO(Connection con) throws SQLException {
        this.con = con;
    }

    public List<Follow> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM follow %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, specification.getColumns(), specification.getJoins(),
                                     specification.getWheres(),  specification.getOrderBy());
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = StatementSetters.setParameters(ps, specification.getParams())
                                    .executeQuery();
        List<Follow> follows = fm.toBeans(rs);
        ps.close();
        rs.close();
        return follows;
    }

    //Questo metodo NON FA NIENTE! La tablla follow non presenta colonne che non sono primary key (per ora).
    public int update(Follow follow) throws SQLException {
        String statement = "UPDATE follow SET %s WHERE section_id=? AND user_id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        //Inserisci parametri da settare (se ci sono)

        params.add(new Pair<>(follow.getSection() == null ? null : follow.getSection().getId(), Types.INTEGER));
        params.add(new Pair<>(follow.getUser() == null ? null : follow.getUser().getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public List<Integer> insert(List<Follow> follows) throws SQLException {
        String statement = "INSERT INTO follow (%s) VALUES %s";
        String columns = "user_id, section_id";
        String questionMarks = "(?,?)";

        List<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner questionMarksJoiner = new StringJoiner(",");
        for(Follow follow : follows) {
            params.add(new Pair<>(follow.getUser() == null ? null : follow.getUser().getId(), Types.INTEGER));
            params.add(new Pair<>(follow.getSection() == null ? null : follow.getSection().getId(), Types.INTEGER));
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

    public int delete(List<Pair<User, Section>> pks) throws SQLException {
        String statement = "DELETE FROM post WHERE %s LIMIT ?";

        StringJoiner whereJoiner = new StringJoiner(" OR ");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        for (Pair<User, Section> pk : pks) {
            params.add(new Pair<>(pk.getLeft().getId(), Types.INTEGER));
            params.add(new Pair<>(pk.getRight().getId(), Types.INTEGER));
            whereJoiner.add(" user_id=? AND section_id=? ");
        }
        params.add(new Pair<>(pks.size(), Types.INTEGER)); //Il LIMIT sara' pari ai parametri inseriti per evitare imprevisti

        statement = String.format(statement, whereJoiner.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        StatementSetters.setParameters(ps,params);
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        return rowsDeleted;
    }

}