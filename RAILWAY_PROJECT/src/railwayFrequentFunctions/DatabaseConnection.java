package railwayFrequentFunctions;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	private static Connection con=null;
	public final String getDriver(){
		return "com.mysql.jdbc.Driver";
	}
	public final String getURL(){
		return "jdbc:mysql://localhost:3306/railway_database";
	}
	public final String getUsername(){
		return "railway_project";
	}
	public final String getPassword(){
		return "railway";
	}
	public Connection dbConnect()
	{
		try {
			Class.forName(getDriver());
			con = DriverManager.getConnection(
					getURL(), getUsername(),
					getPassword());
			} catch (Exception e) {
			e.printStackTrace();
			}
	return con;
	}
}
