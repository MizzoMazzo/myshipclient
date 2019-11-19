package main;

import commands.*;
import connection.ClientConnection;
import controller.Server;
import events.ActNowEvent;
import events.Event;
import events.OwnEventFactory;
import model.Map;
import connection.ServerConnection;

import java.io.IOException;

import model.Ship;
import model.Tile;
import org.apache.commons.cli.*;

public final class Main {

    /**
     * Main method which is given the command line arguments and serves as an entry point for the whole application.
     *
     * @param args array of command line arguments
     */
    public static void main(String[] args) throws IOException, org.apache.commons.cli.ParseException {
        //prepare commandline options
        Options options = new Options()
                .addOption(Option.builder("port")
                        .desc("port the server runs on")
                        .hasArg(true)
                        .numberOfArgs(1)
                        .required(true)
                        .type(Integer.TYPE)
                        .build())
                .addOption(Option.builder("host")
                        .desc("host for connection")
                        .hasArg(true)
                        .numberOfArgs(1)
                        .required(true)
                        .type(String.class)
                        .build());

        //parse the commandline
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        //extract options
        int port = Integer.parseInt(cmd.getOptionValue("port"));
        String host = cmd.getOptionValue("host");

        //create needed server components
        OwnEventFactory eventFactory = new OwnEventFactory();
        ClientConnection<Event> clientConnection = new ClientConnection<>(host, port, -1, eventFactory);

        try{
            clientConnection.sendRegister("Hello", Ship.ActorType.BARQUE);

            while(!clientConnection.nextEvent().getClass().equals(ActNowEvent.class)){
                System.out.println("Success!");
            }
            clientConnection.sendMove(Tile.Direction.EAST);
        }
        catch (Exception e){
            return;
        }
    }

}
