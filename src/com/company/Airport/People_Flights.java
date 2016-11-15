package com.company.Airport;

        import com.company.OracleConnection;
        import com.company.Predicate;

        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.ArrayList;


public class People_Flights {
    OracleConnection m_connection;

    public static class People_Flight {

        private Integer m_people_flight_id;
        private Integer m_id;
        private Integer m_flight_id;

        public Integer GetPeople_Flight_id() {
            return m_people_flight_id;
        }

        public Integer GetId() {
            return m_id;
        }

        public Integer GetFlight_id() {
            return m_flight_id;
        }


        public People_Flight(Integer people_flight_id,
                               Integer id,
                               Integer flight_id) {

            this.m_people_flight_id = people_flight_id;
            this.m_id = id;
            this.m_flight_id = flight_id;
        }

        public People_Flight(Integer id,
                             Integer flight_id) {
            this.m_id = id;
            this.m_flight_id = flight_id;
        }
    }

    public People_Flights(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertPeople_Flights(People_Flight people_flight) throws SQLException {

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO People_Flight(id, flight_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, people_flight.GetId());
        statement.setInt(2, people_flight.GetFlight_id());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new people_flight failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("people_flight_id");
            } else {
                throw new SQLException("Creating new people_flight failed, no people_flight_id obtained");
            }
        }
    }

    public Integer UpdatePeople_Flights(People_Flight people_flight,
                                          Predicate<Integer> people_flight_id,
                                          Predicate<Integer> id,
                                          Predicate<Integer> flight_id) throws SQLException {

        String query = "UPDATE people_flight SET people_flight_id = ?, id = ?, " +
                "flight_id = ?";

        String subquery1 = (people_flight_id != null)?people_flight_id.SelectWhereStatement("people_flight_id", true) + " ":"";
        String subquery2 = (id != null)?id.SelectWhereStatement("id", false) + " ":"";
        String subquery3 = (flight_id != null)?flight_id.SelectWhereStatement("flight_id", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 ;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, people_flight.GetPeople_Flight_id());
        statement.setInt(2, people_flight.GetId());
        statement.setInt(3, people_flight.GetFlight_id());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<People_Flight> GetPeople_Flights(Predicate<Integer> people_flight_id,
                                                      Predicate<Integer> id,
                                                      Predicate<Integer> flight_id
                                                          ) throws SQLException {

        String query = "SELECT people_flight_id, id, flight_id FROM people_flight ";


        String subquery1 = (people_flight_id != null)?people_flight_id.SelectWhereStatement("people_flight_id", true) + " ":"";
        String subquery2 = (id != null)?id.SelectWhereStatement("id", false) + " ":"";
        String subquery3 = (flight_id != null)?flight_id.SelectWhereStatement("flight_id", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<People_Flight> people_flights = new ArrayList<>();
        while(result.next()) {
            people_flights.add(new People_Flight(
                    result.getInt("people_flight_id"),
                    result.getInt("id"),
                    result.getInt("flight_id")
                    ));
        }

        return people_flights;
    }

    public void DeletePeople_Flights(Predicate<Integer> people_flight_id,
                                       Predicate<Integer> id,
                                       Predicate<Integer> flight_id
                                       ) throws SQLException {
        String query = "DELETE FROM people_flight ";

        String subquery1 = (people_flight_id != null)?people_flight_id.SelectWhereStatement("people_flight_id", true) + " ":"";
        String subquery2 = (id != null)?id.SelectWhereStatement("id", false) + " ":"";
        String subquery3 = (flight_id != null)?flight_id.SelectWhereStatement("flight_id", false) + " ":"";

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

