package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class {@link AircraftTypes} is mapping to table AIRCRAFTTYPES from DB
 * @Author Vladimir Gubarev
 * @Version 0.1
 */

public class AircraftTypes {
    OracleConnection m_connection;

    public static class AircraftType {
        private Integer m_aircrafttype_id;
        private String m_icao;
        private String m_description;

        public AircraftType(Integer aircrafttype_id, String icao, String description) {
            if (icao.isEmpty() || description.isEmpty()) {
                throw new IllegalArgumentException();
            }

            m_aircrafttype_id = aircrafttype_id;
            m_icao = icao;
            m_description = description;
        }

        public AircraftType(String icao, String description) {
            if (icao.isEmpty() || description.isEmpty()) {
                throw new IllegalArgumentException();
            }

            m_icao = icao;
            m_description = description;
        }

        public String GetICAO() { return m_icao; }
        public String GetDescription() { return m_description; }
        public Integer GetAircraftTypeID() { return m_aircrafttype_id; }
    }

    public AircraftTypes(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertAircraftType(AircraftType aircraftType) throws SQLException {
        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO AIRCRAFTTYPES(ICAO, DESCRIPTION) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, aircraftType.GetICAO());
        statement.setString(2, aircraftType.GetDescription());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new aircraftType failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("aircrafttype_id");
            } else {
                throw new SQLException("Creating new aircraftType failed, no aircrafttype_id obtained");
            }
        }
    }

    public Integer UpdateAircraftTypes(AircraftType aircraftType,
                                       Predicate<Integer> id,
                                       Predicate<String> icao,
                                       Predicate<String> description) throws SQLException {
        String query = "UPDATE airports SET airport_id = ?, code = ?, city = ? ";
        String subquery1 = (id != null)?id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery2 = (icao != null)?icao.SelectWhereStatement("icao", false) + " ":"";
        String subquery3 = (description != null)?description.SelectWhereStatement("description", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, aircraftType.GetAircraftTypeID());
        statement.setString(2, aircraftType.GetICAO());
        statement.setString(3, aircraftType.GetDescription());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<AircraftType> GetAircraftTypes(Predicate<Integer> id,
                                                    Predicate<String> icao,
                                                    Predicate<String> description) throws SQLException {
        String query = "SELECT AIRCRAFTTYPE_ID, ICAO, DESCRIPTION FROM AIRCRAFTTYPES ";
        String subquery1 = (id != null)?id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery2 = (icao != null)?icao.SelectWhereStatement("icao", false) + " ":"";
        String subquery3 = (description != null)?description.SelectWhereStatement("description", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<AircraftType> aircraftTypes = new ArrayList<>();
        while(result.next()) {
            aircraftTypes.add(new AircraftType(result.getInt("aircrafttype_id"),
                                     result.getString("icao"),
                                     result.getString("description")));
        }

        return aircraftTypes;
    }

    public void DeleteAircraftTypes(Predicate<Integer> id,
                                    Predicate<String> icao,
                                    Predicate<String> description) throws SQLException {
        String query = "DELETE FROM AIRCRAFTTYPES ";
        String subquery1 = (id != null)?id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery2 = (icao != null)?icao.SelectWhereStatement("icao", false) + " ":"";
        String subquery3 = (description != null)?description.SelectWhereStatement("description", false):"";

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