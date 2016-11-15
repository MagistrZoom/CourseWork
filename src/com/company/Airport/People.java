package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;

import java.sql.*;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class People {
    OracleConnection m_connection;

    public static class Man {

        public class Passport_T {
            private Integer series;
            private Integer numbr;

            public Passport_T(Integer series, Integer number) {
                this.series = series;
                this.numbr = number;
            }

            public Integer get_Series(){
                return series;
            }

            public Integer get_Number(){
                return numbr;
            }
            public Passport_T ECHO(){
                return this;
            }

        }

        private Integer m_id;
        private String m_firstname;
        private String m_lastname;
        private Passport_T m_passportT;


        public Integer GetId() {
            return m_id;
        }

        public String GetFirstname() {
            return m_firstname;
        }

        public Passport_T GetPassport() {
            return m_passportT;
        }

        public String GetLastname() {
            return m_lastname;
        }

        public Man(Integer id, String firstname, String lastname, Passport_T passportT) {
            if (firstname.isEmpty() || lastname.isEmpty() || passportT == null) {
                throw new IllegalArgumentException();
            }
            this.m_id = id;
            this.m_firstname = firstname;
            this.m_lastname = lastname;
            this.m_passportT = passportT;
        }

        public Man(String firstname, String lastname, Passport_T passportT) {
            if (firstname.isEmpty() || lastname.isEmpty() || passportT == null) {
                throw new IllegalArgumentException();
            }
            this.m_firstname = firstname;
            this.m_lastname = lastname;
            this.m_passportT = passportT;
        }
    }

    public People(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertPeople(Man man) throws SQLException {
        CallableStatement proc = m_connection.GetConnection().
                prepareCall("{ ? = call AIRPORT.ADD_PEOPLE(?,?,?) }");

        proc.registerOutParameter(1, Types.INTEGER);

        proc.setString(1, man.GetFirstname());
        proc.setString(2, man.GetLastname());
        proc.setObject(3, man.GetPassport());

        proc.execute();
        int result = proc.getInt(1);
        proc.close();
        return result;
    }

    public Integer UpdatePeople(Man man,
                                Predicate<Integer> id,
                                Predicate<String> firstname,
                                Predicate<String> lastname,
                                Predicate<Man.Passport_T> passport) throws SQLException {
        String query = "UPDATE people SET id = ?, firstname = ?, lastname = ?, passport = ? ";
        String subquery1 = (id != null)?id.SelectWhereStatement("id", true) + " ":"";
        String subquery2 = (firstname != null)?firstname.SelectWhereStatement("firstname", false) + " ":"";
        String subquery3 = (lastname != null)?lastname.SelectWhereStatement("lastname", false):"";
        String subquery4 = (passport != null)?passport.SelectWhereStatement("passport", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty()) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        PreparedStatement statement = m_connection.GetConnection().prepareStatement(query);
        statement.setInt(1, man.GetId());
        statement.setString(2, man.GetFirstname());
        statement.setString(3, man.GetLastname());
        statement.setObject(4, man.GetPassport());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Man> GetPeople(Predicate<Integer> id,
                                    Predicate<String> firstname,
                                    Predicate<String> lastname,
                                    Predicate<Man.Passport_T> passport) throws SQLException {
        String query = "SELECT id, firstname, lastname FROM people ";
        String subquery1 = (id != null)?id.SelectWhereStatement("id", true) + " ":"";
        String subquery2 = (firstname != null)?firstname.SelectWhereStatement("firstname", false) + " ":"";
        String subquery3 = (lastname != null)?lastname.SelectWhereStatement("lastname", false):"";
        String subquery4 = (passport != null)?passport.SelectWhereStatement("passport", false):"";

        if (!subquery1.isEmpty() ||
                !subquery2.isEmpty() ||
                !subquery3.isEmpty() ||
                !subquery4.isEmpty() ) {
            query += "WHERE ";
        }

        query += subquery1 + subquery2 + subquery3 + subquery4;

        Statement statement = m_connection.GetConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        ArrayList<Man> people = new ArrayList<>();

        while(result.next()) {
            people.add(new Man(
                    result.getInt("id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getObject("passport", Man.Passport_T.class)));
        }

        return people;
    }

    public void DeletePeople(Predicate<Integer> id,
                             Predicate<String> firstname,
                             Predicate<String> lastname,
                             Predicate<Man.Passport_T> passport) throws SQLException {
        String query = "DELETE FROM man ";
        String subquery1 = (id != null)?id.SelectWhereStatement("id", true) + " ":"";
        String subquery2 = (firstname != null)?firstname.SelectWhereStatement("firstname", false) + " ":"";
        String subquery3 = (lastname != null)?lastname.SelectWhereStatement("lastname", false):"";
        String subquery4 = (passport != null)?passport.SelectWhereStatement("passport", false):"";

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
