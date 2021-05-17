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


    /**
     * Inserisce i parametri in un preparedStatement in funzione di una lista passata in input.<br>
     * L'elemento {@link #NULL} chiamerà il metodo SetNull.
     *
     * @param ps Il PreparedStatement su cui invocare i metodi "setXXX".
     * @param params La lista di parametri da inserire. I parametri devono essere pari al numero di placeholders '?'
     *               presenti nella query ed essere disposti in base all'ordine di comparsa dei placeholders '?'.
     * @return Il PreparedStatement passato in input
     * @throws SQLException in caso di errore
     */
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
