package com.cache.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


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
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);;
        } catch (ClassNotFoundException e) {
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
