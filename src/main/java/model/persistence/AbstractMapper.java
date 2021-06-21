package model.persistence;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public interface AbstractMapper<T> {
    List<T> toBeans(ResultSet rs) throws SQLException;
}
