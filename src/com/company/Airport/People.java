package com.company.Airport;
import com.company.OracleConnection;
import com.company.Predicate;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.*;

import java.sql.*;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class People {
    OracleConnection m_connection;

    public static class Man {

        public static class Passport implements CustomDatum, CustomDatumFactory {
            public static final String _SQL_NAME = "S191941.PASSPORT_T";
            public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

            MutableStruct _struct;

            static int[] _sqlType = { Types.INTEGER, Types.INTEGER };

            static CustomDatumFactory[] _factory = new CustomDatumFactory[2];

            static final Passport __PASSPORT___FACTORY = new Passport();
            public static CustomDatumFactory getFactory()
            {
                return __PASSPORT___FACTORY;
            }

            private Passport() {
                _struct = new MutableStruct(new Object[2], _sqlType, _factory);
            }

            public Passport(Integer series, Integer number) throws SQLException {
                _struct = new MutableStruct(new Object[2], _sqlType, _factory);
                _struct.setAttribute(0, series);
                _struct.setAttribute(1, number);
            }

            public Integer GetSeries() throws SQLException {
                return (Integer) _struct.getAttribute(0);
            }

            public Integer GetNumber() throws SQLException {
                return (Integer) _struct.getAttribute(1);
            }
            public void ECHO(){
                return;
            }

            @Override
            public Datum toDatum(oracle.jdbc.driver.OracleConnection oracleConnection) throws SQLException {
                return _struct.toDatum(oracleConnection, _SQL_NAME);
            }

            @Override
            public CustomDatum create(Datum datum, int i) throws SQLException {
                if (datum == null) return null;
                Passport o = new Passport();
                o._struct = new MutableStruct((STRUCT) datum, _sqlType, _factory);
                return o;
            }
        }

        private Integer m_id;
        private String m_firstname;
        private String m_lastname;
        private Passport m_passportT;


        public Integer GetId() {
            return m_id;
        }

        public String GetFirstname() {
            return m_firstname;
        }

        public Passport GetPassport() {
            return m_passportT;
        }

        public String GetLastname() {
            return m_lastname;
        }

        public Man(Integer id, String firstname, String lastname, Passport passportT) {
            if (firstname.isEmpty() || lastname.isEmpty() || passportT == null) {
                throw new IllegalArgumentException();
            }
            this.m_id = id;
            this.m_firstname = firstname;
            this.m_lastname = lastname;
            this.m_passportT = passportT;
        }

        public Man(String firstname, String lastname, Passport passportT) {
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

        proc.registerOutParameter(1, Types.INTEGER);

        proc.setString(2, man.GetFirstname());
        proc.setString(3, man.GetLastname());

        proc.setObject(4, man.GetPassport(), OracleTypes.STRUCT);

        //proc.setObject("pass", man.GetPassport().create(
        //        man.GetPassport().toDatum(m_connection.GetConnection()), 1), );

        proc.execute();
        int result = proc.getInt(1);
        proc.close();
        return result;
    }

    public Integer UpdatePeople(Man man,
                                Predicate<Integer> id,
                                Predicate<String> firstname,
                                Predicate<String> lastname,
                                Predicate<Man.Passport> passport) throws SQLException {
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
                                    Predicate<Man.Passport> passport) throws SQLException {
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
                    result.getObject("passport", Man.Passport.class)));
        }

        return people;
    }

    public void DeletePeople(Predicate<Integer> id,
                             Predicate<String> firstname,
                             Predicate<String> lastname,
                             Predicate<Man.Passport> passport) throws SQLException {
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
