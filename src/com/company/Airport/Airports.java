package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class {@link Airports} is mapping to table AIRPORTS from DB
 * @Author Vladimir Gubarev
 * @Version 0.1
 */

public class Airports {
    OracleConnection m_connection;

    public static class Airport {
        private Integer m_airport_id;
        private String m_code;
        private String m_city;

        public Airport(Integer airport_id, String code, String city) {
            if (code.isEmpty() || city.isEmpty()) {
                throw new IllegalArgumentException();
            }

            m_airport_id = airport_id;
            m_code = code;
            m_city = city;
        }

        public Airport(String code, String city) {
            this(-1, code, city);
        }

        public String GetCode() {
            return m_code;
        }
        public String GetCity() { return m_city; }
        public Integer GetAircraftID() { return m_airport_id; }
    }

    public Airports(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertAirport(Airport airport) throws SQLException {
        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO AIRPORTS(CODE, CITY) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, airport.GetCode());
        statement.setString(2, airport.GetCity());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new airport failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("airport_id");
            } else {
                throw new SQLException("Creating new airport failed, no airport_id obtained");
            }
        }
    }

    public Integer UpdateAirports(Airport airport,
                                  Predicate<Integer> id,
                                  Predicate<String> code,
                                  Predicate<String> city) throws SQLException {
        String query = "UPDATE airports SET airport_id = ?, code = ?, city = ? ";
        String subquery1 = (id != null)?id.SelectWhereStatement("airport_id", true) + " ":"";
        String subquery2 = (code != null)?code.SelectWhereStatement("code", false) + " ":"";
        String subquery3 = (city != null)?city.SelectWhereStatement("city", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, airport.GetAircraftID());
        statement.setString(2, airport.GetCode());
        statement.setString(3, airport.GetCity());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Airport> GetAirports(Predicate<Integer> id,
                                          Predicate<String> code,
                                          Predicate<String> city) throws SQLException {
        String query = "SELECT airport_id, code, city FROM airports ";
        String subquery1 = (id != null)?id.SelectWhereStatement("airport_id", true) + " ":"";
        String subquery2 = (code != null)?code.SelectWhereStatement("code", false) + " ":"";
        String subquery3 = (city != null)?city.SelectWhereStatement("city", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<Airport> airports = new ArrayList<>();
        while(result.next()) {
            airports.add(new Airport(result.getInt("airport_id"),
                                     result.getString("code"),
                                     result.getString("city")));
        }

        return airports;
    }

    public void DeleteAirports(Predicate<Integer> id,
                               Predicate<String> code,
                               Predicate<String> city) throws SQLException {
        String query = "DELETE FROM airport ";
        String subquery1 = (id != null)?id.SelectWhereStatement("airport_id", true) + " ":"";
        String subquery2 = (code != null)?code.SelectWhereStatement("code", false) + " ":"";
        String subquery3 = (city != null)?city.SelectWhereStatement("city", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3;

        Statement statement = m_connection.GetConnection().createStatement();
        statement.executeQuery(query);
    }


}