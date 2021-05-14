package persistence;

import util.Pair;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public final class StatementSetters {
    /**
     * Costante per definire SQL_NULL
     */
    public static Object NULL = new Object();

    public static PreparedStatement setParameters(PreparedStatement ps, List<Pair<Object, Integer>> params) throws SQLException {
        int i=1;
        for (Pair<Object, Integer> param : params) {
            if(param.getLeft() == NULL){
                ps.setNull(i++, param.getRight());
            } else{
                ps.setObject(i++, param.getLeft(), param.getRight());
            }
        }
        return ps;
    }

    private StatementSetters(){}
}
