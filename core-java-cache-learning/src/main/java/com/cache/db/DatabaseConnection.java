package com.cache.db;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {

    /*
    The single instance of the class (volatile ensures visibility across threads)
     */
    private static volatile DatabaseConnection instance;
    private Connection connection;

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=CacheDemo;encrypt=false";

    private static final String USER = "sa";
    private static final String PASSWORD = "mkrmkrmk";

    //1. Private constructor
    private DatabaseConnection () throws SQLException {
        Properties props = new Properties();
        try(InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
           if(input ==null )
           {
               throw new RuntimeException("Unable to find db.properties");
           }
           props.load(input);

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));;
        } catch (Exception e) {
            throw new RuntimeException("SQL Server Driver not found", e);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException{
        if(instance == null)
        {
            synchronized (DatabaseConnection.class)
            {
                if (instance == null)
                {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public  Connection getConnection() {
        return connection;
    }
}
