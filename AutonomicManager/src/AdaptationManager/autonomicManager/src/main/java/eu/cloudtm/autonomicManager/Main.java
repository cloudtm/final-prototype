package eu.cloudtm.autonomicManager;

import eu.cloudtm.autonomicManager.RESTServer.RESTServer;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/5/13
 */
public class Main {

    public static void main(String[] args) {

        PropertyConfigurator.configure("config/log4j.properties");

        AutonomicManagerFactory appFactory = new AutonomicManagerFactory();
        AutonomicManager autonomicManager = appFactory.build();

        RESTServer restServer = new RESTServer(autonomicManager);
        try {
            restServer.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Console console = new Console(autonomicManager);
        console.menu();
        System.out.println("Main ended!");
        System.exit(0);

    }
}
