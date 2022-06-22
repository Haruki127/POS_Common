package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconfig {
	
	private final String CONNECTION = "jdbc:mysql://localhost:3306/employee";
	private final String PASSWORD = "toor";
	private final String USERNAME = "root";
	private static Connection con = null;
	
	/*
	 * static{}: static initializer block, which is used to initialize static
	 * members of the class. It is executed when the class is initialized.
	 */
	static {
		try {
			/*
			 * java.lang.Class.forName(String name, boolean initialize, ClassLoader loader)
			 * returns the Class object associated with the class or interface
			 */ 
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			/*
			 * prints the throwable along with other details like the line number and class
			 * name where the exception occurred. printStackTrace() is very useful in
			 * diagnosing exceptions.
			 */
			e.printStackTrace();
		}
	}

	//override the getConnection()
	public Connection getConnection() throws SQLException{
		if(con == null) {
			con = DriverManager.getConnection(CONNECTION,USERNAME,PASSWORD); 
		}
		return con;
	}

}
