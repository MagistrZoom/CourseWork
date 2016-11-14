package com.company;

import com.company.Airport.Airports;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        OracleConnection connection = new OracleConnection();
        try {
            connection.SetConnection("localhost", 1521, "orbis", "S191941", "ooi602");

            Airports airports = new Airports(connection);

            PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Operation.EQ, 31);

            PredicateList<String> codes = new PredicateList<>(PredicateList.Operation.OR, new ArrayList<>());
            codes.AddValue("KRO");
            codes.AddValue("VKO");

            PredicateList<String> cities = new PredicateList<>(PredicateList.Operation.OR, new ArrayList<>());
            cities.AddValue("Kemerovo");

            ArrayList<Airports.Airport> air = airports.GetAirports(id, codes, cities);

            air.forEach((a) -> System.out.println(a.GetID() + " " + a.GetCity() + " " + a.GetCode()));
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
    }
}
