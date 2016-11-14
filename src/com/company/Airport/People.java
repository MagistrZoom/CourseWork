package com.company.Airport;

import com.company.OracleConnection;
import com.company.Predicate;
import com.sun.deploy.net.proxy.pac.PACFunctions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class People {
    OracleConnection m_connection;

    public static class Man {

        public class Passport{
            private Integer m_series;
            private Integer m_number;

            public Passport(Integer series, Integer number) {
                this.m_series = series;
                this.m_number = number;
            }

            public Integer getSeries(){
                return m_series;
            }

            public Integer getNumber(){
                return m_number;
            }

        }

        private Integer m_man_id;
        private String m_firstname;
        private String m_lastname;
        private Passport m_passport;


        public Integer GetId() {
            return m_man_id;
        }

        public String GetFirstname() {
            return m_firstname;
        }

        public Passport GetPassport() {
            return m_passport;
        }

        public String GetLastname() {
            return m_lastname;
        }

        public Man(Integer man_id, String firstname, String lastname, Passport passport) {
            if (firstname.isEmpty() || lastname.isEmpty() || passport == null) {
                throw new IllegalArgumentException();
            }
            this.m_man_id = man_id;
            this.m_firstname = firstname;
            this.m_lastname = lastname;
            this.m_passport = passport;
        }

        public Man(String firstname, String lastname, Passport passport) {
            if (firstname.isEmpty() || lastname.isEmpty() || passport == null) {
                throw new IllegalArgumentException();
            }
            this.m_firstname = firstname;
            this.m_lastname = lastname;
            this.m_passport = passport;
        }
    }

    public People(OracleConnection connection) {
        m_connection = connection;
    }

    public Integer InsertPeople(Man man) throws SQLException {

        /*TODO FUNC INSERT*/
        /*
        PreparedStatement statement = m_connection.GetConnection().prepareStatement(
                "INSERT INTO AIRPORTS(CODE, CITY) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, man.GetFirstname());
        statement.setString(2, man.GetLastname());

        int rows = statement.executeUpdate();

        if(rows == 0) {
            throw new SQLException("Creating new man failed, no rows affected");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt("man_id");
            } else {
                throw new SQLException("Creating new man failed, no man_id obtained");
            }
        }*/
        return 0;
    }

    public Integer UpdatePeople(Man man,
                                Predicate<Integer> id,
                                Predicate<String> firstname,
                                Predicate<String> lastname,
                                Predicate<Man.Passport> passport) throws SQLException {
        String query = "UPDATE people SET man_id = ?, firstname = ?, lastname = ?, passport = ? ";
        String subquery1 = (id != null)?id.SelectWhereStatement("man_id", true) + " ":"";
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
        //statement.setString(4, man.GetPassport());

        Integer result = statement.executeUpdate();

        return result;
    }

    public ArrayList<Man> GetPeople(Predicate<Integer> id,
                                    Predicate<String> firstname,
                                    Predicate<String> lastname,
                                    Predicate<Man.Passport> passport) throws SQLException {
        String query = "SELECT man_id, firstname, lastname FROM people ";
        String subquery1 = (id != null)?id.SelectWhereStatement("man_id", true) + " ":"";
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
        /*TODO !!!!!result get....passport*/
        /*
        while(result.next()) {
            people.add(new Man(
                    result.getInt("man_id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getS));
        }*/

        return people;
    }

    public void DeletePeople(Predicate<Integer> id,
                             Predicate<String> firstname,
                             Predicate<String> lastname,
                             Predicate<Man.Passport> passport) throws SQLException {
        String query = "DELETE FROM man ";
        String subquery1 = (id != null)?id.SelectWhereStatement("man_id", true) + " ":"";
        String subquery2 = (firstname != null)?firstname.SelectWhereStatement("firstname", false) + " ":"";
        String subquery3 = (lastname != null)?lastname.SelectWhereStatement("lastname", false):"";

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
