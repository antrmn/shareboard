package persistence;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

public class ConPool {
	private static DataSource dataSource;
	public static Connection getConnection() throws SQLException {

//		Gson gson = new Gson();

		// 1. JSON file to Java object
//		Object object = gson.fromJson(new FileReader(System.getProperty("user.dir") + "/config.json"), Object.class);
//		System.out.println(object);
		if (dataSource == null) {
			PoolProperties p = new PoolProperties();
			p.setUrl("jdbc:mysql://localhost:3306/shareboard?serverTimezone=" + TimeZone.getDefault().getID());
			p.setDriverClassName("com.mysql.cj.jdbc.Driver");
			p.setUsername("root");
			p.setPassword("1234");
			p.setMaxActive(100);
			p.setInitialSize(10);
			p.setMinIdle(10);
			p.setRemoveAbandonedTimeout(60);
			p.setRemoveAbandoned(true);
			dataSource = new DataSource();
			dataSource.setPoolProperties(p);
		}
		return dataSource.getConnection();
	}
}
