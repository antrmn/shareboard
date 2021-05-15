package persistence;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public interface AbstractMapper<T> {
    public List<T> toBeans(ResultSet rs) throws SQLException;
}
