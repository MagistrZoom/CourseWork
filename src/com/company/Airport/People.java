package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;
import oracle.jdbc.proxy.oracle$1jdbc$1replay$1driver$1NonTxnReplayableBase$2java$1sql$1SQLData$$$Proxy;
import oracle.sql.*;

import java.sql.SQLData;

import java.sql.*;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;


public class People {
    OracleConnection m_connection;

    public static class Man {

        public static class Passport_T implements ORAData, ORADataFactory {
            private NUMBER series;
            private NUMBER number;

            public Passport_T(NUMBER series, NUMBER number) {
                this.series = series;
                this.number = number;
            }

            public NUMBER get_Series(){
                return series;
            }

            public NUMBER get_Number(){
                return number;
            }
            public Passport_T ECHO(){
                return this;
            }

            @Override
            public Datum toDatum(Connection c) throws SQLException {
                StructDescriptor sd =
                        StructDescriptor.createDescriptor("PASSPORT_T", c);
                Object [] attributes = { series, number};
                return new STRUCT(sd, c, attributes);
            }

            @Override
            public ORAData create(Datum d, int i) throws SQLException {
                if (d == null) return null;
                Object [] attributes = ((STRUCT) d).getOracleAttributes();
                return new Passport_T(
                        (NUMBER) attributes[0],
                        (NUMBER) attributes[1]);
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


        /*
        StructDescriptor structDesc =
                StructDescriptor.createDescriptor("PASSPORT_T", m_connection.GetConnection());
        Object[] itemAtributes = new Object[] {new Integer(1), new Integer(222)};
        STRUCT itemObject1 = new STRUCT(structDesc,m_connection.GetConnection(),itemAtributes);
        Object ob1 = itemObject1;
        */
        Connection con = m_connection.GetConnection();

        CallableStatement proc = con.
                prepareCall("{ ? = call AIRPORT.ADD_PEOPLE(?,?,?) }");

        proc.registerOutParameter("ret_Pe_id", Types.INTEGER);

        proc.setString("fname", man.GetFirstname());
        proc.setString("lname", man.GetLastname());
         
        proc.setObject("pass", man.GetPassport().create(
                man.GetPassport().toDatum(m_connection.GetConnection()), 1), );

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
