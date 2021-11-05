package defaultjdbc;
import java.sql.*;

public class JdbcConnection {
 
	static Connection con;
	
	public static Connection createCon() throws ClassNotFoundException {
		//loading the driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
		//Creating the connection
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/studentrating","root","1Password*");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return con;
	}
	
}
