package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class {@link Airships} is mapping to table AIRSHIP from DB
 * @Author Vladimir Gubarev
 * @Version 0.1
 */

public class Airships {
    OracleConnection m_connection;

    public static class Airship {
        private Integer m_aircraft_id;
        private Integer m_aircrafttype_id;
        private String m_manufacturer;
        private String m_serialnumer;
        private Integer m_owner_id;

        public Airship(Integer aircraft_id,
                       Integer aircrafttype_id,
                       String  manufacturer,
                       String  serialnumber,
                       Integer owner_id) {
            if (manufacturer.isEmpty() || serialnumber.isEmpty()) {
                throw new IllegalArgumentException();
            }

            m_aircraft_id = aircraft_id;
            m_aircrafttype_id = aircrafttype_id;
            m_manufacturer = manufacturer;
            m_serialnumer = serialnumber;
            m_owner_id = owner_id;
        }

        public Airship(Integer aircrafttype_id,
                       String  manufacturer,
                       String  serialnumber,
                       Integer owner_id) {
            this(-1, aircrafttype_id, manufacturer, serialnumber, owner_id);
        }

        public Integer GetAircraftID() { return m_aircraft_id; }
        public Integer GetAircraftTypeID() { return m_aircrafttype_id; }
        public String  GetManufacturer() { return m_manufacturer; }
        public String  GetSerialnumber() { return m_serialnumer;  }
        public Integer GetOwnerID() { return m_owner_id; }
    }

    public Airships(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertAirship(Airship airship) throws SQLException {
        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO AIRSHIP(AIRCRAFTTYPE_ID, MANUFACTURER, SERIALNUMBER, OWNER_ID) " +
                        "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, airship.GetAircraftTypeID());
        statement.setString(2, airship.GetManufacturer());
        statement.setString(3, airship.GetSerialnumber());
        statement.setInt(4, airship.GetOwnerID());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new airship failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("aircraft_id");
            } else {
                throw new SQLException("Creating new airship failed, no aircraft_id obtained");
            }
        }
    }

    public Integer UpdateAirships(Airship airship,
                                  Predicate<Integer> id,
                                  Predicate<Integer> aircrafttype_id,
                                  Predicate<String> manufacturer,
                                  Predicate<String> serialnumber,
                                  Predicate<Integer> owner_id) throws SQLException {
        String query = "UPDATE AIRSHIP SET AIRCRAFT_ID = ?, AIRCRAFTTYPE_ID = ?, MANUFACTURER = ?, SERIALNUMBER = ?, OWNER_ID = ? ";
        String subquery1 = (id != null)?id.SelectWhereStatement("aircraft_id", true) + " ":"";
        String subquery2 = (aircrafttype_id != null)?aircrafttype_id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery3 = (manufacturer != null)?manufacturer.SelectWhereStatement("manufacturer", false) + " ":"";
        String subquery4 = (serialnumber != null)?serialnumber.SelectWhereStatement("serialnumber", false):"";
        String subquery5 = (owner_id != null)?owner_id.SelectWhereStatement("owner_id", false) + " ":"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty() ||
            !subquery5.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);

        statement.setInt(1, airship.GetAircraftID());
        statement.setInt(2, airship.GetAircraftTypeID());
        statement.setString(3, airship.GetManufacturer());
        statement.setString(4, airship.GetSerialnumber());
        statement.setInt(5, airship.GetOwnerID());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Airship> GetAirships(Predicate<Integer> id,
                                          Predicate<Integer> aircrafttype_id,
                                          Predicate<String> manufacturer,
                                          Predicate<String> serialnumber,
                                          Predicate<Integer> owner_id) throws SQLException {
        String query = "SELECT AIRCRAFT_ID, AIRCRAFTTYPE_ID, MANUFACTURER, SERIALNUMBER, OWNER_ID FROM AIRSHIP ";
        String subquery1 = (id != null)?id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery2 = (aircrafttype_id != null)?aircrafttype_id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery3 = (manufacturer != null)?manufacturer.SelectWhereStatement("manufacturer", false) + " ":"";
        String subquery4 = (serialnumber != null)?serialnumber.SelectWhereStatement("serialnumber", false):"";
        String subquery5 = (owner_id != null)?owner_id.SelectWhereStatement("owner_id", false) + " ":"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty() ||
            !subquery5.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<Airship> airships = new ArrayList<>();
        while(result.next()) {
            airships.add(new Airship(result.getInt("aircraft_id"),
                                     result.getInt("aircrafttype_id"),
                                     result.getString("manufacturer"),
                                     result.getString("serialnumber"),
                                     result.getInt("owner_id")));
        }

        return airships;
    }

    public void DeleteAirships(Predicate<Integer> id,
                               Predicate<Integer> aircrafttype_id,
                               Predicate<String> manufacturer,
                               Predicate<String> serialnumber,
                               Predicate<Integer> owner_id) throws SQLException {
        String query = "DELETE FROM AIRSHIP ";
        String subquery1 = (id != null)?id.SelectWhereStatement("aircraft_id", true) + " ":"";
        String subquery2 = (aircrafttype_id != null)?aircrafttype_id.SelectWhereStatement("aircrafttype_id", true) + " ":"";
        String subquery3 = (manufacturer != null)?manufacturer.SelectWhereStatement("manufacturer", false) + " ":"";
        String subquery4 = (serialnumber != null)?serialnumber.SelectWhereStatement("serialnumber", false):"";
        String subquery5 = (owner_id != null)?owner_id.SelectWhereStatement("owner_id", false) + " ":"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty() ||
            !subquery5.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        Statement statement = m_connection.GetConnection().createStatement();
        statement.executeQuery(query);
    }


}