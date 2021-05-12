package persistence;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;


public abstract class AbstractMapper<T> {

    /**
     * Un'interfaccia funzionale che, presi in input un bean, un ResultSet e una stringa (che indica una colonna
     * del ResultSet), procede a invocare il setter apposito del bean, gestendo ANCHE la SQLException.
     * @param <T> Il tipo del bean a cui verrà chiamato il setter appropriato (Es. Post)
     */
    @FunctionalInterface
    protected interface SQL_TriConsumer<T>{
        void accept(T t, String string, ResultSet rs) throws SQLException;
    }


    private final Map<String, SQL_TriConsumer<T>> map;

    /**
     * Una classe che estende AbstractMapper deve fornire una mappa per poter effettuare il mapping
     * @param map La mappa contenente coppie (Colonna, Metodo) che il mapper controllerà per settare i campi giusti.
     *            **NOTA**: L'alias sovrascrive il nome della colonna
     */
    protected AbstractMapper(Map<String, SQL_TriConsumer<T>> map) {
        this.map = map;
    }


    /**
     * @return Il bean appena istanziato (Se sono presenti campi che hanno altri bean necessari al mapping è necessario
     * istanziare anche quelli
     */
    protected abstract T instantiate();

    /**
     * Questo metodo legge la mappa passata nel costruttore e procede al mapping. Non &egrave; necessario fare l'override a
     * meno di casi particolari
     * @param rs Il ResultSet che punta alla riga da mappare in Bean
     * @return Il bean mappato
     * @throws SQLException In caso di errore SQL
     */
    public T toBean(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        T bean = instantiate();

        for(int i=1; i<=rsmd.getColumnCount(); i++){
            String column = rsmd.getColumnLabel(i);

            SQL_TriConsumer<T> setter = map.get(column);
            if(setter != null){
                Object o = rs.getObject(i);
                setter.accept(bean, column, rs);
            }
        }
        return bean;
    }


}
