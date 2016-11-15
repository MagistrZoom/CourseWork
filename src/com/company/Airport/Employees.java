package com.company.Airport;

import com.company.OracleConnection;
import com.company.Predicate;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;


public class Employees extends People {

    public static class Employee extends Man {

        private Integer m_employee_id;
        private String m_position;
        private Date m_recruitment;
        private Date m_dismissal;
        private Blob m_photo;

        public Integer GetEmployeeID() { return m_employee_id; }
        public String GetPosition() { return m_position; }
        public Date GetRecruitment() { return m_recruitment; }
        public Date GetDismissal() { return m_dismissal; }
        public Blob GetPhoto() { return m_photo; }

        public Employee(Integer id, Integer employee_id,
                        String fname, String lname, Passport passport,
                        String position, Date recruitment,
                        Date dismissal, Blob photo) {
            super(id, fname, lname, passport);

            m_employee_id = employee_id;
            m_position = position;
            m_recruitment = recruitment;
            m_dismissal = dismissal;
            m_photo = photo;
        }

        public Employee(Integer integer, int employee_id, String fname, String lname, Passport passport,
                        String position, Date recruitment,
                        Date dismissal, int id, Blob photo) {
            this(-1, -1, fname, lname, passport, position, recruitment,
                    dismissal, photo);
        }
    }

    public Employees(OracleConnection connection) {
        super(connection);
    }

    public Integer InsertEmployee(Employee employee) throws SQLException {

        Connection con = m_connection.GetConnection();

        CallableStatement proc = con.
                prepareCall("{ ? = call AIRPORT.ADD_EMPLOYEE(?,?,?,?,?,?,?) }");

        proc.registerOutParameter(1, Types.INTEGER);

        proc.setString(2, employee.GetFirstname());
        proc.setString(3, employee.GetLastname());
        proc.setObject(4, employee.GetPassport(), OracleTypes.STRUCT);
        proc.setString(5, employee.GetPosition());
        proc.setBlob(6, employee.GetPhoto());
        proc.setDate(7, employee.GetRecruitment());
        proc.setDate(8, employee.GetDismissal());

        proc.execute();
        int result = proc.getInt(1);
        proc.close();
        return result;
    }

    public Integer UpdateEmployee(Employee employee,
                                  Predicate<Integer> id,
                                  Predicate<Integer> employee_id,
                                  Predicate<String> firstname,
                                  Predicate<String> lastname,
                                  Predicate<Man.Passport> passport,
                                  Predicate<String> position,
                                  Predicate<Blob> photo,
                                  Predicate<Date> recruitment,
                                  Predicate<Date> dismissal) throws SQLException {
        super.UpdatePeople(employee, id, firstname, lastname, passport);

        String query = "UPDATE EMPLOYEES SET EMPLOYEE_ID = ?, POSITION = ?, PHOTO = ?, RECRUITMENT = ?, DISMISSAL = ? ";
        String subquery1 = (employee_id != null)?employee_id.SelectWhereStatement("employee_id", true) + " ":"";
        String subquery2 = (position != null)?position.SelectWhereStatement("position", false) + " ":"";
        String subquery3 = (photo != null)?photo.SelectWhereStatement("photo", false):"";
        String subquery4 = (recruitment != null)?recruitment.SelectWhereStatement("recruitment", false):"";
        String subquery5 = (dismissal != null)?dismissal.SelectWhereStatement("dismissal", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty() ||
                !subquery5.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, employee.GetEmployeeID());
        statement.setString(2, employee.GetPosition());
        statement.setBlob(3, employee.GetPhoto());
        statement.setDate(4, employee.GetRecruitment());
        statement.setDate(5, employee.GetDismissal());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Man> GetEmployees(Predicate<Integer> id,
                                  Predicate<Integer> employee_id,
                                  Predicate<String> firstname,
                                  Predicate<String> lastname,
                                  Predicate<Man.Passport> passport,
                                  Predicate<String> position,
                                  Predicate<Blob> photo,
                                  Predicate<Date> recruitment,
                                  Predicate<Date> dismissal) throws SQLException {
        ArrayList<Man> people = super.GetPeople(id, firstname, lastname, passport);

        String query = "SELECT FROM EMPLOYEES ";
        String subquery1 = (employee_id != null)?employee_id.SelectWhereStatement("employee_id", true) + " ":"";
        String subquery2 = (position != null)?position.SelectWhereStatement("position", false) + " ":"";
        String subquery3 = (photo != null)?photo.SelectWhereStatement("photo", false):"";
        String subquery4 = (recruitment != null)?recruitment.SelectWhereStatement("recruitment", false):"";
        String subquery5 = (dismissal != null)?dismissal.SelectWhereStatement("dismissal", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty() ||
                !subquery5.isEmpty() ) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<Employee> employees = new ArrayList<>();

        while(result.next()) {
            for(Man it : people) {
                if (it.GetId() == result.getInt("id")) {
                    employees.add(new Employee(
                            it.GetId(),
                            result.getInt("employee_id"),
                            it.GetFirstname(),
                            it.GetLastname(),
                            it.GetPassport(),
                            result.getString("position"),
                            result.getDate("recruitment"),
                            result.getDate("dismissal"),
                            result.getInt("id"),
                            result.getBlob("photo")
                    ));
                }
            }
        }

        return people;
    }

    public void DeleteEmployee(Predicate<Integer> employee_id,
                                  Predicate<String> position,
                                  Predicate<Blob> photo,
                                  Predicate<Date> recruitment,
                                  Predicate<Date> dismissal) throws SQLException {

        String query = "DELETE FROM EMPLOYEES ";
        String subquery1 = (employee_id != null)?employee_id.SelectWhereStatement("employee_id", true) + " ":"";
        String subquery2 = (position != null)?position.SelectWhereStatement("position", false) + " ":"";
        String subquery3 = (photo != null)?photo.SelectWhereStatement("photo", false):"";
        String subquery4 = (recruitment != null)?recruitment.SelectWhereStatement("recruitment", false):"";
        String subquery5 = (dismissal != null)?dismissal.SelectWhereStatement("dismissal", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty() ||
                !subquery5.isEmpty() ) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4 + subquery5;

        Statement statement = m_connection.GetConnection().createStatement();
        statement.executeQuery(query);
    }

}
