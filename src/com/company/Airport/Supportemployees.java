package com.company.Airport;

    import com.company.OracleConnection;
    import com.company.Predicate;

    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.util.ArrayList;


public class Supportemployees {
    OracleConnection m_connection;

    public static class Supportemployee {

        private Integer m_supportemployee_id;
        private Integer m_employee_id;
        private Integer m_plan_id;
        private Integer m_aircraft_id;
        private String m_role;

        public Integer GetSupportemployee_id() {
            return m_supportemployee_id;
        }

        public Integer GetEmployee_id() {
            return m_employee_id;
        }

        public Integer GetPlan_id() {
            return m_plan_id;
        }

        public Integer GetAircraft_id() {
            return m_aircraft_id;
        }

        public String GetRole() {
            return m_role;
        }

        public Supportemployee(Integer supportemployee_id,
                               Integer employee_id,
                               Integer plan_id,
                               Integer aircraft_id,
                               String role) {
            if (role.isEmpty()) {
                throw new IllegalArgumentException();
            }
            this.m_supportemployee_id = supportemployee_id;
            this.m_employee_id = employee_id;
            this.m_plan_id = plan_id;
            this.m_aircraft_id = aircraft_id;
            this.m_role = role;
        }

        public Supportemployee(Integer employee_id,
                               Integer plan_id,
                               Integer aircraft_id,
                               String role) {
            if (role.isEmpty()) {
                throw new IllegalArgumentException();
            }
            this.m_employee_id = employee_id;
            this.m_plan_id = plan_id;
            this.m_aircraft_id = aircraft_id;
            this.m_role = role;
        }
    }

    public Supportemployees(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertSupportemployees(Supportemployee supportemployee) throws SQLException {

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO Supportemployee(employee_id, plan_id, aircraft_id, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, supportemployee.GetEmployee_id());
        statement.setInt(2, supportemployee.GetPlan_id());
        statement.setInt(3, supportemployee.GetAircraft_id());
        statement.setString(4, supportemployee.GetRole());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new supportemployee failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("supportemployee_id");
            } else {
                throw new SQLException("Creating new supportemployee failed, no supportemployee_id obtained");
            }
        }
    }

    public Integer UpdateSupportemployees(Supportemployee supportemployee,
                                            Predicate<Integer> supportemployee_id,
                                            Predicate<Integer> employee_id,
                                            Predicate<Integer> plan_id,
                                            Predicate<Integer> aircraft_id,
                                            Predicate<String> role) throws SQLException {

        String query = "UPDATE supportemployee SET supportemployee_id = ?, employee_id = ?, " +
                "plan_id = ?, aircraft_id = ?, role = ?";

        String subquery1 = (supportemployee_id != null)?supportemployee_id.SelectWhereStatement("supportemployee_id", true) + " ":"";
        String subquery2 = (employee_id != null)?employee_id.SelectWhereStatement("employee_id", false) + " ":"";
        String subquery3 = (plan_id != null)?plan_id.SelectWhereStatement("plan_id", false) + " ":"";
        String subquery4 = (aircraft_id!= null)?aircraft_id.SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery5 = (role != null)?role.SelectWhereStatement("role", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty() ||
                !subquery5.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, supportemployee.GetSupportemployee_id());
        statement.setInt(2, supportemployee.GetEmployee_id());
        statement.setInt(3, supportemployee.GetPlan_id());
        statement.setInt(4, supportemployee.GetAircraft_id());
        statement.setString(5, supportemployee.GetRole());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Supportemployee> GetSupportemployees(Predicate<Integer> supportemployee_id,
                                                          Predicate<Integer> employee_id,
                                                          Predicate<Integer> plan_id,
                                                          Predicate<Integer> aircraft_id,
                                                          Predicate<String> role) throws SQLException {

        String query = "SELECT supportemployee_id, employee_id, plan_id, aircraft_id, role FROM supportemployee ";


        String subquery1 = (supportemployee_id != null)?supportemployee_id.SelectWhereStatement("supportemployee_id", true) + " ":"";
        String subquery2 = (employee_id != null)?employee_id.SelectWhereStatement("employee_id", false) + " ":"";
        String subquery3 = (plan_id != null)?plan_id.SelectWhereStatement("plan_id", false) + " ":"";
        String subquery4 = (aircraft_id!= null)?aircraft_id.SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery5 = (role != null)?role.SelectWhereStatement("role", false):"";

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

        ArrayList<Supportemployee> supportemployees = new ArrayList<>();
        while(result.next()) {
            supportemployees.add(new Supportemployee(
                    result.getInt("supportemployee_id"),
                    result.getInt("employee_id"),
                    result.getInt("plan_id"),
                    result.getInt("aircraft_id"),
                    result.getString("role")));
        }

        return supportemployees;
    }

    public void DeleteSupportemployees(Predicate<Integer> supportemployee_id,
                                       Predicate<Integer> employee_id,
                                       Predicate<Integer> plan_id,
                                       Predicate<Integer> aircraft_id,
                                       Predicate<String> role) throws SQLException {
        String query = "DELETE FROM supportemployee ";

        String subquery1 = (supportemployee_id != null)?supportemployee_id.SelectWhereStatement("supportemployee_id", true) + " ":"";
        String subquery2 = (employee_id != null)?employee_id.SelectWhereStatement("employee_id", false) + " ":"";
        String subquery3 = (plan_id != null)?plan_id.SelectWhereStatement("plan_id", false) + " ":"";
        String subquery4 = (aircraft_id!= null)?aircraft_id.SelectWhereStatement("aircraft_id", false) + " ":"";
        String subquery5 = (role != null)?role.SelectWhereStatement("role", false):"";

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

