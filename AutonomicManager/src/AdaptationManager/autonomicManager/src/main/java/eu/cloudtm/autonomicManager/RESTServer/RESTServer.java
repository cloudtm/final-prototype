package eu.cloudtm.autonomicManager.RESTServer;

import eu.cloudtm.autonomicManager.AutonomicManager;
import eu.cloudtm.autonomicManager.ControllerLogger;
import eu.cloudtm.autonomicManager.statistics.StatsManager;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class RESTServer {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:1515/";

    private final ResourceConfig rc;

    private HttpServer httpServer;

    public RESTServer(AutonomicManager autonomicManager){
        rc = new RESTApplication(autonomicManager);
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public void startServer() throws IOException {
        // create a resource config that scans for JAX-RS resources and providers
        // in it.fperfetti package




        // uncomment the following line if you want to enable
        // support for JSON on the service (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml)
        // --
        // rc.addBinder(org.glassfish.jersey.media.json.JsonJaxbBinder);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        httpServer.start();

        ControllerLogger.log.info("Web Server + REST Interface started");
    }

}

