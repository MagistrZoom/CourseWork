package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;

import java.sql.*;
import java.util.ArrayList;

/**
 * The class {@link Luggage} is mapping to table AIRSHIP from DB
 * @Author Vladimir Gubarev
 * @Version 0.1
 */

public class Luggage {
    OracleConnection m_connection;

    public static class LuggageUnit {
        private Integer m_luggage_id;
        private Integer m_weight;
        private Integer m_volume;
        private Integer m_people_flight_id;
        private Integer m_flight_id;

        public LuggageUnit(Integer luggage_id,
                           Integer weight,
                           Integer volume,
                           Integer people_flight_id,
                           Integer flight_id) {
            m_luggage_id = luggage_id;
            m_weight = weight;
            m_volume = volume;
            m_people_flight_id = people_flight_id;
            m_flight_id = flight_id;
        }

        public LuggageUnit(Integer weight,
                           Integer volume,
                           Integer people_flight_id,
                           Integer flight_id) {
            this(-1, weight, volume, people_flight_id, flight_id);
        }

        public Integer GetLuggageID() { return m_luggage_id; }
        public Integer GetWeight() { return m_weight; }
        public Integer GetVolume() { return m_volume; }
        public Integer GetPeopleFlightID() { return m_people_flight_id;  }
        public Integer GetFlightID() { return m_flight_id; }
    }

    public Luggage(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertLuggageUnit(LuggageUnit luggageUnit) throws SQLException {
        CallableStatement proc = m_connection.GetConnection().prepareCall("{ ? = call AIRPORT.ADD_LUGGAGE(?,?,?) }");
        proc.registerOutParameter(1, Types.INTEGER);
        proc.setInt(1, luggageUnit.GetPeopleFlightID());
        proc.setInt(2, luggageUnit.GetWeight());
        proc.setInt(3, luggageUnit.GetVolume());
        proc.execute();
        int result = proc.getInt(1);
        proc.close();
        return result;
    }

    public Integer UpdateLuggage(LuggageUnit luggageUnit,
                                 Predicate<Integer> luggage_id,
                                 Predicate<Integer> weight,
                                 Predicate<Integer> volume,
                                 Predicate<Integer> people_flight_id) throws SQLException {
        String query = "UPDATE LUGGAGE SET LUGGAGE_ID = ?, WEIGHT = ?, VOLUME = ?, PEOPLE_FLIGHT_ID = ? ";
        String subquery1 = (luggage_id != null)?luggage_id.SelectWhereStatement("luggage_id", true) + " ":"";
        String subquery2 = (weight != null)?weight.SelectWhereStatement("weight", false) + " ":"";
        String subquery3 = (volume != null)?volume.SelectWhereStatement("volume", false) + " ":"";
        String subquery4 = (people_flight_id != null)?people_flight_id.SelectWhereStatement("people_flight_id", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);

        statement.setInt(1, luggageUnit.GetLuggageID());
        statement.setInt(2, luggageUnit.GetWeight());
        statement.setInt(3, luggageUnit.GetVolume());
        statement.setInt(4, luggageUnit.GetPeopleFlightID());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<LuggageUnit> GetLuggage(Predicate<Integer> luggage_id,
                                              Predicate<Integer> weight,
                                              Predicate<Integer> volume,
                                              Predicate<Integer> people_flight_id) throws SQLException {
        String query = "SELECT LUGGAGE_ID, WEIGHT, VOLUME, PEOPLE_FLIGHT_ID FROM LUGGAGE ";
        String subquery1 = (luggage_id != null)?luggage_id.SelectWhereStatement("luggage_id", true) + " ":"";
        String subquery2 = (weight != null)?weight.SelectWhereStatement("weight", false) + " ":"";
        String subquery3 = (volume != null)?volume.SelectWhereStatement("volume", false) + " ":"";
        String subquery4 = (people_flight_id != null)?people_flight_id.SelectWhereStatement("people_flight_id", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<LuggageUnit> luggageUnits = new ArrayList<>();
        while(result.next()) {
            luggageUnits.add(new LuggageUnit(result.getInt("luggage_id"),
                                     result.getInt("weight"),
                                     result.getInt("volume"),
                                     result.getInt("people_flight_id")));
        }

        return luggageUnits;
    }

    public void DeleteLuggage(Predicate<Integer> luggage_id,
                               Predicate<Integer> weight,
                               Predicate<Integer> volume,
                               Predicate<Integer> people_flight_id) throws SQLException {
        String query = "DELETE FROM LUGGAGE ";
        String subquery1 = (luggage_id != null)?luggage_id.SelectWhereStatement("luggage_id", true) + " ":"";
        String subquery2 = (weight != null)?weight.SelectWhereStatement("weight", false) + " ":"";
        String subquery3 = (volume != null)?volume.SelectWhereStatement("volume", false) + " ":"";
        String subquery4 = (people_flight_id != null)?people_flight_id.SelectWhereStatement("people_flight_id", false):"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        Statement statement = m_connection.GetConnection().createStatement();
        statement.executeQuery(query);
    }


}