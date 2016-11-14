package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;

import java.sql.*;
import java.util.ArrayList;

/**
 * The class {@link Flights} is mapping to table FLIGHT from DB
 * @Author Vladimir Gubarev
 * @Version 0.1
 */

public class Flights {
    OracleConnection m_connection;

    public static class Flight {
        private Integer m_flight_id;
        private Timestamp m_departure;
        private Timestamp m_arrival;
        private Integer m_destination_point;
        private Integer m_departure_point;
        private Integer m_aircraft_id;

        public Flight(Integer flight_id,
                      Timestamp departure,
                      Timestamp arrival,
                      Integer destination_point,
                      Integer departure_point,
                      Integer aircraft_id ) {

            m_flight_id = flight_id;
            m_aircraft_id = aircraft_id;
            m_departure = departure;
            m_arrival = arrival;
            m_destination_point = destination_point;
            m_departure_point = departure_point;
        }


        public Flight(Timestamp departure,
                      Timestamp arrival,
                      Integer destination_point,
                      Integer departure_point,
                      Integer aircraft_id) {
            this(-1, departure, arrival, destination_point, departure_point, aircraft_id);
        }

        public Integer GetAircraftID() { return m_aircraft_id; }
        public Timestamp GetDeparture() { return m_departure; }
        public Timestamp GetArrival() { return m_arrival; }
        public Integer GetDestinationPoint() { return m_destination_point; }
        public Integer GetDeparturePoint() { return m_departure_point; }
        public Integer GetFlightID() { return m_flight_id; }
    }

    public Flights(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertFlight(Flight flight) throws SQLException {
        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO FLIGHT (DEPARTURE,ARRIVAL,DESTINATION_POINT,AIRCRAFT_ID,DEPARTURE_POINT) " +
                        "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        statement.setTimestamp(1, flight.GetDeparture());
        statement.setTimestamp(2, flight.GetArrival());
        statement.setInt(3, flight.GetDestinationPoint());
        statement.setInt(4, flight.GetAircraftID());
        statement.setInt(5, flight.GetDeparturePoint());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new flight failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("flight_id");
            } else {
                throw new SQLException("Creating new flight failed, no flight_id obtained");
            }
        }
    }

    public Integer UpdateFlight(Flight flight,
                                Predicate<Integer> flight_id,
                                Predicate<Timestamp> departure,
                                Predicate<Timestamp> arrival,
                                Predicate<Integer> destination_point,
                                Predicate<Integer> aircraft_id,
                                Predicate<Integer> departure_point) throws SQLException {
        String query = "UPDATE Flight SET FLIGHT_ID = ?, DEPARTURE = ?, ARRIVAL = ?, DESTINATION_POINT = ?, AIRCRAFT_ID = ?, DEPARTURE_POINT = ? ";
        String subquery1 = (flight_id != null)?flight_id.SelectWhereStatement("flight_id", true) + " ":"";
        String subquery2 = (departure != null)?departure.SelectWhereStatement("departure", false) + " ":"";
        String subquery3 = (arrival != null)?arrival.SelectWhereStatement("arrival", false) + " ":"";
        String subquery4 = (destination_point != null)?destination_point.SelectWhereStatement("destination_point", false) + " ":"";
        String subquery5 = (aircraft_id != null)?aircraft_id.SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery6 = (departure_point != null)?departure_point.SelectWhereStatement("departure_point", false) + " ":"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty() ||
            !subquery5.isEmpty() ||
            !subquery6.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5 + subquery6;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);

        statement.setInt(1, flight.GetFlightID());
        statement.setTimestamp(2, flight.GetDeparture());
        statement.setTimestamp(3, flight.GetArrival());
        statement.setInt(4, flight.GetDestinationPoint());
        statement.setInt(5, flight.GetAircraftID());
        statement.setInt(6, flight.GetDeparturePoint());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Flight> GetFlights(Predicate<Integer> flight_id,
                                         Predicate<Timestamp> departure,
                                         Predicate<Timestamp> arrival,
                                         Predicate<Integer> destination_point,
                                         Predicate<Integer> aircraft_id,
                                         Predicate<Integer> departure_point) throws SQLException {
        String query = "SELECT FLIGHT_ID, DEPARTURE, ARRIVAL, DESTINATION_POINT, AIRCRAFT_ID, DEPARTURE_POINT FROM FLIGHT ";

        String subquery1 = (flight_id != null)?flight_id.SelectWhereStatement("flight_id", true) + " ":"";
        String subquery2 = (departure != null)?departure.SelectWhereStatement("departure", false) + " ":"";
        String subquery3 = (arrival != null)?arrival.SelectWhereStatement("arrival", false) + " ":"";
        String subquery4 = (destination_point != null)?destination_point.SelectWhereStatement("destination_point", false) + " ":"";
        String subquery5 = (aircraft_id != null)?aircraft_id.SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery6 = (departure_point != null)?departure_point.SelectWhereStatement("departure_point", false) + " ":"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty() ||
            !subquery5.isEmpty() ||
            !subquery6.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5 + subquery6;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<Flight> flights = new ArrayList<>();
        while(result.next()) {
            flights.add(new Flight(result.getInt("aircraft_id"),
                                   result.getTimestamp("departure"),
                                   result.getTimestamp("arrival"),
                                   result.getInt("destination_point"),
                                   result.getInt("aircraft_id"),
                                   result.getInt("departure_point")));
        }

        return flights;
    }

    public void DeleteFlights(Predicate<Integer> flight_id,
                             Predicate<Timestamp> departure,
                             Predicate<Timestamp> arrival,
                             Predicate<Integer> destination_point,
                             Predicate<Integer> aircraft_id,
                             Predicate<Integer> departure_point) throws SQLException {
        String query = "DELETE FROM FLIGHT ";
        String subquery1 = (flight_id != null)?flight_id.SelectWhereStatement("flight_id", true) + " ":"";
        String subquery2 = (departure != null)?departure.SelectWhereStatement("departure", false) + " ":"";
        String subquery3 = (arrival != null)?arrival.SelectWhereStatement("arrival", false) + " ":"";
        String subquery4 = (destination_point != null)?destination_point.SelectWhereStatement("destination_point", false) + " ":"";
        String subquery5 = (aircraft_id != null)?aircraft_id.SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery6 = (departure_point != null)?departure_point.SelectWhereStatement("departure_point", false) + " ":"";

        if (!subquery1.isEmpty() ||
            !subquery2.isEmpty() ||
            !subquery3.isEmpty() ||
            !subquery4.isEmpty() ||
            !subquery5.isEmpty() ||
            !subquery6.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5 + subquery6;

        Statement statement = m_connection.GetConnection().createStatement();
        statement.executeQuery(query);
    }


}