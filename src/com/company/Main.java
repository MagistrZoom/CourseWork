package com.company;

import com.company.Airport.Airports;
import com.company.Airport.People;
import oracle.sql.NUMBER;

import java.sql.SQLException;
import java.util.ArrayList;

/*
employees ---   RUD
 */

public class Main {

    public static void main(String[] args) {
        OracleConnection connection = new OracleConnection();
        try {
            connection.SetConnection("localhost", 1521, "orbis", "S191941", "ooi602");

            //Airports airports = new Airports(connection);

            PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR,
                                                                PredicateSingle.Operation.EQ,
                                                                31);
            People people = new People(connection);

            People.Man.Passport_T passportT = new People.Man.Passport_T(512, 666);

            People.Man person = new People.Man("Working", "Insert", passportT);

            people.InsertPeople(person);

/*
            PredicateList<String> codes = new PredicateList<>(PredicateList.Logic.OR, false, new ArrayList<>());
            codes.AddValue("KRO");
            codes.AddValue("VKO");

            PredicateList<String> cities = new PredicateList<>(PredicateList.Logic.OR, false, new ArrayList<>());
            cities.AddValue("Kemerovo");

            ArrayList<Airports.Airport> air = airports.GetAirports(id, codes, cities);
            air.forEach((a) -> System.out.println(a.GetAircraftID() + " " + a.GetCity() + " " + a.GetCode()));

            Airports.Airport airport = new Airports.Airport(31, "IKT", "IZHEVSK2");
            Integer result = airports.UpdateAirports(airport, id, null, null);
            System.out.println(result+ " rows written");

            air = airports.GetAirports(id, codes, cities);
            air.forEach((a) -> System.out.println(a.GetAircraftID() + " " + a.GetCity() + " " + a.GetCode()));
*/        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
    }
}
