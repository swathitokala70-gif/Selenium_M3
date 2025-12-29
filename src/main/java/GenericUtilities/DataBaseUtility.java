package GenericUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver;

public class DataBaseUtility {

	public Connection con;

	public Connection connectToDatabase(String url, String username, String password) throws SQLException {
		Driver dobj = new Driver();
		DriverManager.registerDriver(dobj);
		con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public Connection connectToDatabase() throws SQLException {
		Driver dobj = new Driver();
		DriverManager.registerDriver(dobj);
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "root");
		return con;
	}

	public ResultSet fetchDataFromDatabase(String query) throws SQLException {
		Statement stat = con.createStatement();
		ResultSet res = stat.executeQuery(query);
		return res;

	}

	public int updateDataToDatabase(String query) throws Exception {
		Statement stat = con.createStatement();
		int res = stat.executeUpdate(query);
		return res;
	}

	public void closeDatabaseConnection() throws SQLException {
		con.close();
	}

}
