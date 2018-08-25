package Database;

import java.sql.*;
/**
 * [...]
 * This class used to enter details of database and other connection info
 *
 * @author  Priyanka Awaraddi
 */

public class DB_management
{	
	private Connection connection = null;
	
	public DB_management()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/ooad","root","password");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		}

	public void openConn() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver"); 
		connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/ooad","root","password");
	}
	
	protected ResultSet execute(String psSQL) throws SQLException
	{
		Statement statement = connection.createStatement();
		
		System.out.println("execute:  ");
		System.out.println(psSQL);
		return statement.executeQuery(psSQL);
	}

	protected void insert(String sql) throws SQLException
	{
        Statement statement = connection.createStatement();
		statement.execute(sql);
	}
	
	protected void updateOrDelete(String sql) throws SQLException
	{
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate(sql);
	}
}
