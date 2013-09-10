package eu.cloudtm.autonomicManager.actuators.clients;

import com.google.gson.Gson;
import eu.cloudtm.autonomicManager.actuators.excepions.RadargunException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/27/13
 */
@Deprecated
public class SlaveKillerClient implements RadargunClient {

    private Log log = LogFactory.getLog(SlaveKillerClient.class);

    private final String host;
    private final String port;
    private final String component;

//    public static SlaveKillerClient getInstance(String slaveHost, int jmxPort, String component) throws MalformedURLException, DeltaCloudClientException {
//
//        String slaveKillerHost = "cloudtm.ist.utl.pt";
//        int slaveKillerPort = 5455;
//        SlaveKillerClient actuator = new SlaveKillerClient(slaveKillerHost, slaveKillerPort);
//
//        return actuator;
//    }

    public SlaveKillerClient(String slaveKillerHost, int slaveKillerPort, String component){
        this.host = slaveKillerHost;
        this.port = String.valueOf(slaveKillerPort);
        this.component = component;
    }

    private URI getBaseURI() {
        URI uri = UriBuilder.fromUri("http://" + host + ":" + port + "/stop").build();
        log.info(uri);
        return uri;
    }

    @Override
    public void stop(String slaveHost, int jmxPort) throws RadargunException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

        String res = target.request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(String.class);

        log.info(res);
        SlaveKillerResponse response = new Gson().fromJson(res, SlaveKillerResponse.class);

        if(response==null || !response.getResult().equals("Success"))
            throw new RadargunException("Problem while stopping " + response.toString());

        //log.info("response result: " + response.getResult());
    }

}