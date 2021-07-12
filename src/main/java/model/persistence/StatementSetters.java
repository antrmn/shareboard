package model.persistence;


import util.Pair;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public final class StatementSetters {
    /**
     * Inserisce i parametri in un preparedStatement in funzione di una lista passata in input.<br>
     *
     * @param ps Il PreparedStatement su cui invocare i metodi "setXXX".
     * @param params La lista di parametri da inserire. I parametri devono essere pari al numero di placeholders '?'
     *               presenti nella query ed essere disposti in base all'ordine di comparsa dei placeholders '?'.
     * @return Il PreparedStatement passato in input
     * @throws SQLException in caso di errore
     */
    public static PreparedStatement setParameters(PreparedStatement ps, List<Pair<Object, Integer>> params) throws SQLException {
        int i=1;
        String paramsString = params.stream().map(x -> String.format("(%s,%s)", x.getLeft(), x.getRight()))
                                             .collect(Collectors.joining(","));
        //System.out.println("Preparing an SQL statement with these parameters: " + paramsString);
        for (Pair<Object, Integer> param : params) {
            ps.setObject(i++, param.getLeft(), param.getRight());
        }
        //System.out.println("Prepared an SQL statement: " + ps.toString().replace(";", ";\n"));
        return ps;
    }

    private StatementSetters(){}
}
