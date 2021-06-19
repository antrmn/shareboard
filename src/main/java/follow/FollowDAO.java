package follow;

import persistence.Specification;
import persistence.StatementSetters;
import section.Section;
import user.User;
import util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;


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

    /* Questo metodo NON FA NIENTE! La tabella follow per ora non presenta colonne editabili, ma il metodo resta "aperto"
        per eventuali modifiche. */
    public int update(Follow follow) throws SQLException {
        String statement = "UPDATE follow SET %s WHERE section_id=? AND user_id=? LIMIT 1";

        StringJoiner valuesToSet = new StringJoiner(",");
        List<Pair<Object, Integer>> params = new ArrayList<>();
        //Inserisci qui i parametri da settare (se ci sono)

        params.add(new Pair<>(follow.getSection() == null ? null : follow.getSection().getId(), Types.INTEGER));
        params.add(new Pair<>(follow.getUser() == null ? null : follow.getUser().getId(), Types.INTEGER));

        statement = String.format(statement, valuesToSet.toString());
        PreparedStatement ps = con.prepareStatement(statement);
        int rowsUpdated = StatementSetters.setParameters(ps, params).executeUpdate();
        ps.close();
        return rowsUpdated;
    }

    public int insert(List<Follow> follows) throws SQLException {
        String statement = "INSERT INTO follow (%s) VALUES %s ON DUPLICATE KEY UPDATE follow_date=follow_date";
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
        PreparedStatement ps = con.prepareStatement(statement);
        int insertedRows = StatementSetters.setParameters(ps, params).executeUpdate();

        ps.close();
        return insertedRows;
    }

    public int delete(List<Pair<User, Section>> pks) throws SQLException {
        String statement = "DELETE FROM follow WHERE %s LIMIT ?";

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

    public int delete(Pair<User, Section> pk) throws SQLException {
        return delete(List.of(pk));
    }

    public int delete (int userId, int sectionId) throws SQLException {
        User u = new User();
        u.setId(userId);
        Section s = new Section();
        s.setId(sectionId);
        return delete(List.of(new Pair<>(u,s)));
    }

    public Follow get(Pair<User,Section> pk) throws SQLException{
        FollowSpecificationBuilder fsb = new FollowSpecificationBuilder().byUserId(pk.getLeft().getId())
                                                                         .bySectionId(pk.getRight().getId());
        List<Follow> singleton = fetch(fsb.build());
        return singleton.isEmpty() ? null : singleton.get(0);
    }

    public int insert(Follow follow) throws SQLException {
        return insert(List.of(follow));
    }

    public int insert (Collection<Integer> sectionIds, int userId) throws SQLException {
        List<Follow> followList = sectionIds.stream().map(x -> {
            Follow follow = new Follow();
            Section section = new Section();
            section.setId(x);
            follow.setSection(section);
            User user = new User();
            user.setId(userId);
            follow.setUser(user);
            return follow;
        }).collect(Collectors.toList());
        return insert(followList);
    }
}