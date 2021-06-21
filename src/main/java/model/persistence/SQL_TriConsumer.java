package model.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Un'interfaccia funzionale che, presi in input un bean, un ResultSet e una stringa (che indica una colonna
 * del ResultSet), procede a invocare il setter apposito del bean, gestendo ANCHE la SQLException.
 * @param <T> Il tipo del bean a cui verrà chiamato il setter appropriato (Es. Post)
 */
@FunctionalInterface
public interface SQL_TriConsumer<T>{
    void accept(T t, String string, ResultSet rs) throws SQLException;
}
