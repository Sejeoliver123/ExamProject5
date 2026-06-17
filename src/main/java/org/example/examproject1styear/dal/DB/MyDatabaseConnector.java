package org.example.examproject1styear.dal.DB;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class MyDatabaseConnector{

    private SQLServerDataSource dataSource;

    public MyDatabaseConnector()
    {
        dataSource= new SQLServerDataSource();
        dataSource.setServerName("127.0.0.1");
        dataSource.setDatabaseName("WeblagerDB");
        dataSource.setUser("sa");
        dataSource.setPassword("0209991039");
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws SQLException {

        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();
        Connection connection = databaseConnector.getConnection();

        System.out.println("Is it open? " + !connection.isClosed());

        connection.close();
    }

}


