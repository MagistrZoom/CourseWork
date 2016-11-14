package com.company;

import java.sql.*;

public class OracleConnection {
    Connection m_connection;

    public OracleConnection() {
        m_connection = null;
    }

    public Connection SetConnection(String host, int port, String service, String user, String password)
            throws ClassNotFoundException, SQLException {

        System.err.println("-------- Oracle JDBC Connection Testing ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

        System.err.println("Oracle JDBC Driver Registered!");
        try {
            m_connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//" + host +
                    ":" + String.valueOf(port) +
                    "/" + service,
                user,
                password);
        } catch (SQLException e) {
            System.err.println("Connection Failed! Check output console");
            e.printStackTrace();
            throw e;
        }

        if (m_connection != null) {
            System.err.println("You made it, take control your database now!");
        } else {
            System.err.println("Failed to make m_connection!");
        }

        return m_connection;
    }

    public Connection GetConnection() {
        return m_connection;
    }
}
