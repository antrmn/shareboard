package model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConPool {
	private static DataSource dataSource;

	public static Connection getConnection() throws SQLException, NamingException {
		if (dataSource == null) {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/shareboardDB");
		}
		return dataSource.getConnection();
	}
}
