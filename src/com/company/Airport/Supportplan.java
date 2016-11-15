package com.company.Airport;
        import com.company.OracleConnection;
        import com.company.Predicate;

        import java.sql.*;
        import java.util.ArrayList;

        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;


public class Supportplan {
    OracleConnection m_connection;

    public static class Plan {


        private Integer m_plan_id;
        private Integer m_aircraft_id;
        private Timestamp m_start_time;
        private Timestamp m_end_time;


        public Integer GetPlan_id() {
            return m_plan_id;
        }

        public Integer GetAircraft_id() {
            return m_aircraft_id;
        }

        public Timestamp GetStart_time() {
            return m_start_time;
        }

        public Timestamp GetEnd_time() {
            return m_end_time;
        }

        public Plan(Integer plan_id,
                    Integer aircraft_id,
                    Timestamp start_time,
                    Timestamp end_time) {
            this.m_plan_id = plan_id;
            this.m_aircraft_id = aircraft_id;
            this.m_start_time = start_time;
            this.m_end_time = end_time;
        }

        public Plan(Integer aircraft_id,
                    Timestamp start_time,
                    Timestamp end_time) {

            this.m_aircraft_id = aircraft_id;
            this.m_start_time = start_time;
            this.m_end_time = end_time;
        }
    }

    public Supportplan(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertSupportplan(Plan plan,
                                     Integer leader_id,
                                     String leader_role ) throws SQLException {
        Connection con = m_connection.GetConnection();

        CallableStatement proc = con.
                prepareCall("{ ? = call AIRPORT.Create_PLAN_WITH_LEADER(?,?,?,?,?) }");

        proc.registerOutParameter(1, Types.INTEGER);

        proc.setInt(2, plan.GetAircraft_id());
        proc.setTimestamp(3, plan.GetStart_time());
        proc.setTimestamp(4, plan.GetEnd_time());
        proc.setInt(5, leader_id);
        proc.setString(6, leader_role);

        proc.execute();
        int result = proc.getInt(1);
        proc.close();
        return result;
    }

    public Integer UpdateSupportplan(Plan plan,
                                    Predicate<Integer> plan_id,
                                    Predicate<Integer> aircraft_id,
                                    Predicate<Timestamp> start_time,
                                    Predicate<Timestamp> end_time) throws SQLException {

        String query = "UPDATE supportplan SET plan_id = ?, aircraft_id = ?, start_time = ?, end_time = ? ";

        String subquery1 = (plan_id != null)?plan_id.SelectWhereStatement("plan_id", true) + " ":"";
        String subquery2 = (aircraft_id != null)?aircraft_id .SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery3 = (start_time != null)?start_time.SelectWhereStatement("start_time", false):"";
        String subquery4 = (end_time != null)?end_time.SelectWhereStatement("end_time", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, plan.GetPlan_id());
        statement.setInt(2, plan.GetAircraft_id());
        statement.setTimestamp(3, plan.GetStart_time());
        statement.setTimestamp(4, plan.GetEnd_time());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Plan> GetPeople(Predicate<Integer> plan_id,
                                     Predicate<Integer> aircraft_id,
                                     Predicate<Timestamp> start_time,
                                     Predicate<Timestamp> end_time
                                    ) throws SQLException {
        String query = "SELECT plan_id, aircraft_id, start_time, end_time  FROM supportplan ";


        String subquery1 = (plan_id != null)?plan_id.SelectWhereStatement("plan_id", true) + " ":"";
        String subquery2 = (aircraft_id != null)?aircraft_id .SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery3 = (start_time != null)?start_time.SelectWhereStatement("start_time", false):"";
        String subquery4 = (end_time != null)?end_time.SelectWhereStatement("end_time", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty() ) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<Plan> people = new ArrayList<>();

        while(result.next()) {
            people.add(new Plan(
                    result.getInt("plan_id"),
                    result.getInt("aircraft_id"),
                    result.getTimestamp("start_time"),
                    result.getTimestamp("end_time")));
        }

        return people;
    }

    public void DeleteSupportPlan(Predicate<Integer> plan_id,
                             Predicate<Integer> aircraft_id,
                             Predicate<Timestamp> start_time,
                             Predicate<Timestamp> end_time
                            ) throws SQLException {

        String query = "DELETE FROM SUPPORTPLAN ";

        String subquery1 = (plan_id != null)?plan_id.SelectWhereStatement("plan_id", true) + " ":"";
        String subquery2 = (aircraft_id != null)?aircraft_id .SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery3 = (start_time != null)?start_time.SelectWhereStatement("start_time", false):"";
        String subquery4 = (end_time != null)?end_time.SelectWhereStatement("end_time", false):"";

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
