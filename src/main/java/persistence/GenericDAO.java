package persistence;

import util.Pair;
import util.SetNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


/**
 * Questa classe prevede le 4 operazioni più comuni per il DB: Read, Create, Update, Delete.
 * @param <T> Il tipo del Bean
 * @param <M> La classe del Mapper per il Bean specificato
 */
public abstract class GenericDAO<T, M extends AbstractMapper<T>> {

    protected Connection con;

    private final List<String> readableColumnsList;
    private final List<String> updatableColumnsList;
    private final String view;
    private final String table;
    private final M mapper;

    /**
     * @param con Un'istanza di Connection.
     * @param readableColumnsList La lista di colonne (e di alias) che saranno restituite dalla SELECT
     * @param updatableColumnsList La lista di colonne che saranno specificate nella INSERT
     * @param view La vista su cui opererà la SELECT. Può essere uguale a {@link #table}.
     * @param table Una tabella NON DI SOLA LETTURA su cui opereranno la SELECT, la INSERT e la DELETE
     * @param mapper Un'istanza del Mapper per questo Bean.
     */
    protected GenericDAO(Connection con, List<String> readableColumnsList, List<String> updatableColumnsList, String view, String table, M mapper){
        this.con = con;
        this.readableColumnsList = readableColumnsList;
        this.updatableColumnsList = updatableColumnsList;
        this.view = view;
        this.table = table;
        this.mapper = mapper;
    }

    /**
     * Questo metodo prende un Bean in input e, in base ai campi settati, esegue due azioni
     *
     * <p><ul>
     *     <li> Genera la parte di query che si pone dopo il SET di un UPDATE statement.
     *     (**Esempio**: se il campo Titolo del bean è settato, il metodo aggiungerà "Titolo=?" nello stringa in uscita</li>
     *     <li> Inserisce i campi editabili e diversi da null in nella lista specificata in input </li>
     * </ul></p>
     *
     * <b>NOTA:</b> l'ultimo parametro di params deve essere l'id del Bean da modificare
     *
     * @param bean Il Bean in input
     * @param params La lista (composte da coppie Valore-Tipo) che sarà riempita con i parametri da inserire nel PreparedStatement. L'ultimo elemento
     *               deve essere l'id del record da modificare
     * @return La parte di UPDATE statement che va subito dopo il SET clause
     */
    protected abstract String fillUpdateStatement(T bean, List<Pair<Object, Integer>> params);

    /**
     * Questo metodo prende un Bean in input e in base ai campi editabili riempie la lista di parametri specificata in input.
     * L'ordine dei parametri deve corrispondere alle colonne settate in {@link #updatableColumnsList}
     *
     * @param bean Il bean da inserire
     * @param params La lista dei parametri come coppie (Valore, Tipo valore)
     */
    protected abstract void fillInsertStatement(T bean, List<Pair<Object, Integer>> params);


    /**
     * @param specification L'oggetto Specification che conterrà i parametri per la SELECT e le specifiche per
     *                      formattare lo statement
     * @return Una lista di Bean
     * @throws SQLException In caso di errore SQL
     */
    public List<T> fetch(Specification specification) throws SQLException {
        String query = "SELECT %s FROM %s AS %s %s %s %s LIMIT ? OFFSET ?";
        query = String.format(query, String.join(",",readableColumnsList),
                             view,
                             table,
                             specification.getJoins(),
                             specification.getWheres(),
                             specification.getOrderBy()
        );


        List<Pair<Object,Integer>> params = specification.getParams();
        PreparedStatement ps = con.prepareStatement(query);
        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }
        ps.setInt(i++, specification.getLimit());
        ps.setInt(i, specification.getOffset());

        ResultSet rs = ps.executeQuery();
        ArrayList<T> beans = new ArrayList<>();


        while(rs.next()){
            beans.add(mapper.toBean(rs));
        }
        rs.close();
        return beans;
    }


    /**
     * Aggiorna i campi editabili al record con l'id specificato dal bean.
     * I campi editabili settati a null saranno ignorati. Se si vuole settare un campo del DB a NULL occorre usare la costante
     * {@link util.SetNull#NULL}.
     *
     * @param bean Il bean i cui valori editabili saranno inseriti nel Db
     * @return 1 in caso di successo, 0 altrimenti
     * @throws SQLException In caso di errore SQL
     */
    public int update(T bean) throws SQLException {
        String statement = "UPDATE %s SET %s WHERE id = ?";

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        String valuesToSet = fillUpdateStatement(bean, params);

        statement = String.format(statement, table, valuesToSet);

        if(valuesToSet.isBlank() || params.isEmpty()){
            throw new RuntimeException("Empty SET clause");
        }

        statement = String.format(statement,valuesToSet);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){ //NOTA: l'ultimo param deve essere l'id.
            if(param.getLeft() == SetNull.NULL){
                ps.setNull(i++, param.getRight());
            } else{
                ps.setObject(i++, param.getLeft(), param.getRight());
            }
        }

        return ps.executeUpdate();
    }

    /**
     * Questo metodo inserisce nuove righe nel DB utilizzando i valori dei bean presi in input.
     * I campi dei bean uguali a null saranno presi in considerazione.
     *
     * @param beans I bean da inserire
     * @return Il numero di righe inserite
     * @throws SQLException In caso di errore SQL
     */
    public int create(List<T> beans) throws SQLException {
        String statement = "INSERT INTO %s (%s) VALUES %s";

        StringJoiner _questionMarks = new StringJoiner(",","(",")");
        for(int i=0; i<updatableColumnsList.size(); i++)
            _questionMarks.add("?");
        String questionMarks = _questionMarks.toString();

        ArrayList<Pair<Object, Integer>> params = new ArrayList<>();
        StringJoiner values = new StringJoiner(",");
        for(T bean : beans){
            fillInsertStatement(bean, params);
            values.add(questionMarks);
        }

        if(values.length()==0){
            throw new RuntimeException("Empty VALUES clause");
        }

        statement = String.format(statement, table,
                                             String.join(",",updatableColumnsList),
                                             values);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (Pair<Object, Integer> param : params){
            ps.setObject(i++, param.getLeft(), param.getRight());
        }

        return ps.executeUpdate();
    }


    /**
     * Cancella i record dati gli id specificati
     *
     * @param ids Gli id dei campi da rimuovere
     * @return Numero di righe eliminate
     * @throws SQLException In caso di errore SQL
     */
    public int delete(int[] ids) throws SQLException {
        String statement = "DELETE FROM %s WHERE id IN (%s)";

        StringJoiner sj = new StringJoiner(",");
        for (int ignored : ids){
            sj.add("?");
        }

        statement = String.format(statement, table, sj);
        PreparedStatement ps = con.prepareStatement(statement);

        int i=1;
        for (int id : ids){
            ps.setInt(i++, id);
        }

        return ps.executeUpdate();
    }

    /**
     * Wrapper di {@link #create(List)} per 1 solo bean
     *
     * @param bean Il bean da inserire
     * @return 1 in caso di successo, 0 altrimenti
     * @throws SQLException In caso di errore SQL
     */
    public int create(T bean) throws SQLException {
        return create(List.of(bean));
    }


    /**
     * Wrapper di {@link #delete(int[])} specificando 1 solo id
     *
     * @param id L'id del record da eliminare
     * @return 1 in caso di successo, 0 altrimenti
     * @throws SQLException In caso di errore SQL
     */
    public int delete(int id) throws SQLException {
        return delete(new int[]{id});
    }
}