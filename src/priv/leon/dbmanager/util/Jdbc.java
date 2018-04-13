package priv.leon.dbmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbc {

    public static Connection connect(String url, String driverClassName, String userName, String password) throws SQLException {
        if(url == null) {
            throw new SQLException("The url must be not null !");
        }

        if(driverClassName == null) {
            throw new SQLException("The driverClass must be not null !");
        }

        try {
        	System.out.println(driverClassName);
            Class.forName(driverClassName);
        }
        catch(ClassNotFoundException e) {
        	System.out.println("driver not found");
            throw new SQLException(e);
        }
        System.out.println(url);
        System.out.println(userName);
        System.out.println(password);
        return DriverManager.getConnection(url, userName, password);
    }

    
    public static void close(Connection connection) {
        if(connection != null) {
            boolean closed = false;

            try {
                closed = connection.isClosed();
            }
            catch(SQLException e) {
            }

            if(closed == false) {
                try {
                    connection.close();
                }
                catch(SQLException e) {
                }
            }
        }
    }


    public static void close(Statement statement) {
        if(statement != null) {
            try {
                statement.close();
            }
            catch(SQLException e) {
            }
        }
    }


    public static void close(ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            }
            catch(SQLException e) {
            }
        }
    }
}
