package com.company;
import com.company.Airport.*;
import org.apache.commons.cli.*;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {
        OracleConnection connection = new OracleConnection();
        try {
            connection.SetConnection("localhost", 1521, "orbis", "S191941", "ooi602");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }

        Options options = new Options();

        Option help = new Option("h", "help", false, "Print help for specified table and operation");
        Option table = new Option("t", "table", true, "Target table: list of tables");
        table.setRequired(true);
        Option operation = new Option("o", "operation", true, "Operation to be performed: create, update, get, delete");
        operation.setRequired(true);
        Option data = new Option("d", "data", true, "data for query: <column1> <column2> ... <columnN>");
        data.setArgs(Option.UNLIMITED_VALUES);
        Option where = new Option("w", "where", true, "where: <column1> <column2> ... <columnN>");
        where.setArgs(Option.UNLIMITED_VALUES);
        where.setRequired(false);

        options.addOption(help);
        options.addOption(table);
        options.addOption(operation);
        options.addOption(data);
        options.addOption(where);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("airport_crud", options);

            System.exit(1);
            return;
        }

        String tbl = cmd.getOptionValue("table");
        String op = cmd.getOptionValue("operation");
        String[] d = cmd.getOptionValues("data");
        String[] wh = cmd.getOptionValues("where");
        if (wh == null) wh = new String[]{};

        switch (tbl) {
            case "Airports":
                Airports airports1 = new Airports(connection);
                switch (op) {
                    case "create":
                        if (d.length != 2) {
                            System.out.println("Not enough arguments.\n <code> <city> expected");
                            System.exit(1);
                        }

                        Airports.Airport at = new Airports.Airport(d[0], d[1]);
                        Integer r = airports1.InsertAirport(at);
                        System.out.println("Inserted id: " + r.toString());
                        break;
                    case "get":
                        if (wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <code> <city> expected");
                            System.exit(1);
                        }

                        PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        PredicateSingle<String> code = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[1]);
                        PredicateSingle<String> city = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[2]);

                        ArrayList<Airports.Airport> airports = airports1.GetAirports(id, code, city);
                        if (airports.isEmpty()) {
                            System.out.println("No matched entries");
                        }

                        airports.forEach((x) -> System.out.println(x.GetAirportID() + " " + x.GetCode() + " " + x.GetCity()));

                        break;
                    case "update":
                        if (d.length != 2) {
                            System.out.println("Not enough data arguments.\n <code> <city> expected");
                            System.exit(1);
                        }

                        at = new Airports.Airport(d[0], d[1]);

                        if(wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <code> <city> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        code = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[1]);
                        city = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);

                        airports1.UpdateAirports(at, id, code, city);
                        break;
                    case "delete":
                        if (wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <code> <city> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        code = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[1]);
                        city = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);

                        airports1.DeleteAirports(id, code, city);

                        break;
                }
            break;

            case "Airship":
                Airships airships = new Airships(connection);
                switch (op) {
                    case "create":
                        if (d.length != 4) {
                            System.out.println("Not enough arguments.\n <aircrafttype_id> <manufacturer> <serialnumber> <owner_id> expected");
                            System.exit(1);
                        }

                        Airships.Airship ship = new Airships.Airship(Integer.valueOf(d[0]), d[1], d[2], Integer.valueOf(d[3]));
                        Integer r = airships.InsertAirship(ship);
                        System.out.println("Inserted id: " + r.toString());
                        break;
                    case "get":
                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <aircrafttype_id> <manufacturer> <serialnumber> <owner_id> expected");
                            System.exit(1);
                        }

                        PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        PredicateSingle<Integer> aircrafttype_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[1]));
                        PredicateSingle<String> manufacturer = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);
                        PredicateSingle<String> serialnumber = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[3]);
                        PredicateSingle<Integer> owner_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[4]));

                        ArrayList<Airships.Airship> airships1 = airships.GetAirships(id, aircrafttype_id, manufacturer, serialnumber, owner_id);
                        if (airships1.isEmpty()) {
                            System.out.println("No matched entries");
                        }

                        airships1.forEach((x) -> System.out.println(x.GetAircraftID() + "" + x.GetAircraftTypeID() + " " + x.GetManufacturer() + " " + x.GetSerialnumber() +" " + x.GetOwnerID()));

                        break;
                    case "update":
                        if (d.length != 4) {
                            System.out.println("Not enough arguments.\n <aircrafttype_id> <manufacturer> <serialnumber> <owner_id> expected");
                            System.exit(1);
                        }

                        ship = new Airships.Airship(Integer.valueOf(d[0]), d[1], d[2], Integer.valueOf(d[3]));
                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <aircrafttype_id> <manufacturer> <serialnumber> <owner_id> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        aircrafttype_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[1]));
                        manufacturer = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);
                        serialnumber = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[3]);
                        owner_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[4]));

                        airships.UpdateAirships(ship, id, aircrafttype_id, manufacturer, serialnumber, owner_id);

                        break;
                    case "delete":
                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <aircrafttype_id> <manufacturer> <serialnumber> <owner_id> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        aircrafttype_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[1]));
                        manufacturer = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);
                        serialnumber = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[3]);
                        owner_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[4]));

                        airships.DeleteAirships(id, aircrafttype_id, manufacturer, serialnumber, owner_id);

                        break;
                }
            break;

            case "Owners":
                Owners owners = new Owners(connection);
                switch (op) {
                    case "create":
                        if (d.length != 2) {
                            System.out.println("Not enough arguments.\n <shortname> <fullname> expected");
                            System.exit(1);
                        }

                        Owners.Owner owner = new Owners.Owner(d[1], d[2]);
                        Integer r = owners.InsertOwners(owner);
                        System.out.println("Inserted id: " + r.toString());
                        break;
                    case "get":
                        if (wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <shortname> <fullname> expected");
                            System.exit(1);
                        }

                        PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        PredicateSingle<String> shortname = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[1]);
                        PredicateSingle<String> fullname = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[2]);

                        ArrayList<Owners.Owner> owners1 = owners.GetOwners(id, shortname, fullname);
                        if (owners1.isEmpty()) {
                            System.out.println("No matched entries");
                        }

                        owners1.forEach((x) -> System.out.println(x.GetOwner_id() + " " + x.GetShortname() + " " + x.GetFullname()));

                        break;
                    case "update":
                        if (d.length != 2) {
                            System.out.println("Not enough data arguments.\n <shortname> <fullname> expected");
                            System.exit(1);
                        }

                        owner = new Owners.Owner(d[0], d[1]);

                        if(wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <shortname> <fullname> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        shortname = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[1]);
                        fullname = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[2]);

                        owners.UpdateOwners(owner, id, shortname, fullname);
                        break;
                    case "delete":
                        if (wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <shortname> <fullname> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        shortname = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[1]);
                        fullname = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[2]);

                        owners.DeleteOwners(id, shortname, fullname);

                        break;
                }
            break;

            case "AircraftTypes":
                AircraftTypes ats = new AircraftTypes(connection);
                switch (op) {
                    case "create":
                        if (d.length != 2) {
                            System.out.println("Not enough arguments.\n <icao> <description> expected");
                            System.exit(1);
                        }

                        AircraftTypes.AircraftType at = new AircraftTypes.AircraftType(d[0], d[1]);
                        Integer r = ats.InsertAircraftType(at);
                        System.out.println("Inserted id: " + r.toString());
                        break;
                    case "get":
                        if (wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <icao> <description> expected");
                            System.exit(1);
                        }

                        PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        PredicateSingle<String> icao = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[1]);
                        PredicateSingle<String> description = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ,wh[2]);

                        ArrayList<AircraftTypes.AircraftType> aircraftTypes = ats.GetAircraftTypes(id, icao, description);
                        if (aircraftTypes.isEmpty()) {
                            System.out.println("No matched entries");
                        }

                        aircraftTypes.forEach((x) -> System.out.println(x.GetAircraftTypeID() + " " + x.GetICAO() + " " + x.GetDescription()));

                        break;
                    case "update":
                        if (d.length != 2) {
                            System.out.println("Not enough data arguments.\n <icao> <description> expected");
                            System.exit(1);
                        }

                        at = new AircraftTypes.AircraftType(d[0], d[1]);

                        if(wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <icao> <description> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        icao = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[1]);
                        description = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);

                        ats.UpdateAircraftTypes(at, id, icao, description);
                        break;
                    case "delete":
                        if (wh.length != 3) {
                            System.out.println("Not enough where arguments.\n <id> <icao> <description> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        icao = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[1]);
                        description = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, wh[2]);

                        ats.DeleteAircraftTypes(id, icao, description);

                        break;
                }
            break;

            case "Luggage":
                Luggage lug = new Luggage(connection);
                switch (op) {
                    case "create":
                        if (d.length != 4) {
                            System.out.println("Not enough arguments.\n <weight> <volume> <people_flight_id> <flight_id> expected");
                            System.exit(1);
                        }

                        Luggage.LuggageUnit luggageUnit = new Luggage.LuggageUnit(
                                Integer.valueOf(d[0]),
                                Integer.valueOf(d[1]),
                                Integer.valueOf(d[2]),
                                Integer.valueOf(d[3])
                        );

                        Integer r = lug.InsertLuggageUnit(luggageUnit);
                        System.out.println("Inserted id: " + r.toString());
                        break;
                    case "get":
                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <weight> <volume> <people_flight_id>  expected");
                            System.exit(1);
                        }

                        PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        PredicateSingle<Integer> weight = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[1]));
                        PredicateSingle<Integer> volume = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[2]));
                        PredicateSingle<Integer> people_flight_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[3]));

                        ArrayList<Luggage.LuggageUnit> luggageUnits = lug.GetLuggage(id, weight, volume, people_flight_id);
                        if (luggageUnits.isEmpty()) {
                            System.out.println("No matched entries");
                        }

                        luggageUnits.forEach((x) -> System.out.printf(
                                "%s %s %s %s %s",
                                x.GetLuggageID(),
                                x.GetFlightID(),
                                x.GetPeopleFlightID(),
                                x.GetVolume(),
                                x.GetWeight()
                        ));

                        break;
                    case "update":
                        if (d.length != 4) {
                            System.out.println("Not enough arguments.\n <weight> <volume> <people_flight_id> <flight_id> expected");
                            System.exit(1);
                        }

                        luggageUnit = new Luggage.LuggageUnit(Integer.valueOf(d[0]),Integer.valueOf(d[1]),Integer.valueOf(d[2]),Integer.valueOf(d[3]));

                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <weight> <volume> <people_flight_id> expected");
                            System.exit(1);
                        }


                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        weight = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[1]));
                        volume = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[2]));
                        people_flight_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[3]));

                        lug.UpdateLuggage(luggageUnit, id, weight, volume, people_flight_id);

                        break;
                    case "delete":
                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <weight> <volume> <people_flight_id> expected");
                            System.exit(1);
                        }

                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        weight = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[1]));
                        volume = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[2]));
                        people_flight_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[3]));

                        lug.DeleteLuggage(id, weight, volume, people_flight_id);

                        break;
                }
            break;

            case "Flights":
                Flights flights = new Flights(connection);
                switch (op) {
                    case "create":
                        if (d.length != 5) {
                            System.out.println("Not enough arguments.\n <departure> <arrival> <destination> <aircraft_id> <departure_point> expected");
                            System.exit(1);
                        }

                        Flights.Flight flight = new Flights.Flight(Timestamp.valueOf(d[0]), Timestamp.valueOf(d[1]), Integer.valueOf(d[2]), Integer.valueOf(d[3]), Integer.valueOf(d[4]));
                        Integer r = flights.InsertFlight(flight);
                        System.out.println("Inserted id: " + r.toString());
                        break;
                    case "get":
                        if (wh.length != 6) {
                            System.out.println("Not enough where arguments.\n <id> <departure> <arrival> <destination> <aircraft_id> <departure_point> expected");
                            System.exit(1);
                        }

                        PredicateSingle<Integer> id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        PredicateSingle<Timestamp> departure = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Timestamp.valueOf(wh[1]));
                        PredicateSingle<Timestamp> arrival = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Timestamp.valueOf(wh[2]));
                        PredicateSingle<Integer> destination = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[3]));
                        PredicateSingle<Integer> aircraft_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[4]));
                        PredicateSingle<Integer> departure_point = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[5]));


                        ArrayList<Flights.Flight> flights1 = flights.GetFlights(id, departure, arrival, destination, aircraft_id, departure_point);
                        if (flights1.isEmpty()) {
                            System.out.println("No matched entries");
                        }

                        flights1.forEach((x) -> System.out.printf(
                                "%s %s %s %s %s %s",
                                x.GetFlightID(),
                                x.GetDeparture(),
                                x.GetArrival(),
                                x.GetDeparture(),
                                x.GetAircraftID(),
                                x.GetDeparturePoint()
                        ));

                        break;
                    case "update":
                        if (d.length != 4) {
                            System.out.println("Not enough arguments.\n <departure> <arrival> <destination> <aircraft_id> <departure_point> expected");
                            System.exit(1);
                        }

                        flight = new Flights.Flight(Timestamp.valueOf(d[0]), Timestamp.valueOf(d[1]), Integer.valueOf(d[2]), Integer.valueOf(d[3]), Integer.valueOf(d[4]));

                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <departure> <arrival> <destination> <aircraft_id> <departure_point> expected");
                            System.exit(1);
                        }


                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        departure = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Timestamp.valueOf(wh[1]));
                        arrival = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Timestamp.valueOf(wh[2]));
                        destination = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[3]));
                        aircraft_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[4]));
                        departure_point = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[5]));

                        flights.UpdateFlight(flight, id, departure, arrival, destination, aircraft_id, departure_point);

                        break;
                    case "delete":
                        if (wh.length != 5) {
                            System.out.println("Not enough where arguments.\n <id> <aircrafttype_id> <manufacturer> <serialnumber> <owner_id> expected");
                            System.exit(1);
                        }


                        id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[0]));
                        departure = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Timestamp.valueOf(wh[1]));
                        arrival = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Timestamp.valueOf(wh[2]));
                        destination = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[3]));
                        aircraft_id = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[4]));
                        departure_point = new PredicateSingle<>(PredicateSingle.Logic.OR, PredicateSingle.Operation.EQ, Integer.valueOf(wh[5]));

                        flights.DeleteFlights(id, departure, arrival, destination, aircraft_id, departure_point);

                        break;
                }
            break;
        }
    }
}
