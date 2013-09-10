package eu.cloudtm.autonomicManager.actuators;

import eu.cloudtm.autonomicManager.IActuator;
import eu.cloudtm.autonomicManager.actuators.clients.RadargunClient;
import eu.cloudtm.autonomicManager.actuators.clients.RadargunClientJMX;
import eu.cloudtm.autonomicManager.configs.Config;
import eu.cloudtm.autonomicManager.configs.KeyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.deltacloud.client.DeltaCloudClient;
import org.apache.deltacloud.client.DeltaCloudClientException;
import org.apache.deltacloud.client.DeltaCloudClientImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Author: Fabio Perfetti (perfabio87 [at] gmail.com)
 * Date: 8/6/13
 * Time: 2:52 PM
 */
public class ActuatorFactory {

    private Log log = LogFactory.getLog(ActuatorFactory.class);

    private final boolean isRadargun = Config.getInstance().getBoolean(KeyConfig.ACTUATOR_IS_RADARGUN.key());

    private int jmxPort = Config.getInstance().getInt( KeyConfig.ISPN_JMX_PORT.key() );
    private String imageId = Config.getInstance().getString( KeyConfig.DELTACLOUD_IMAGE.key() );
    private String flavorId = Config.getInstance().getString( KeyConfig.DELTACLOUD_FLAVOR.key() );

    private String domain = Config.getInstance().getString( KeyConfig.ISPN_DOMAIN.key() );
    private String cacheName = Config.getInstance().getString( KeyConfig.ISPN_CACHE_NAME.key() );

    private ActuatorType type = ActuatorType.valueOf(Config.getInstance().getString(KeyConfig.ACTUATOR_TYPE.key()));

    public ActuatorFactory(){
    }

    public IActuator build(){

        switch (type){
            case CLOUD_TM:
                return createCloudTM();
            case FUTURE_GRID:
                return createFutureGrid();
            default:
                throw new RuntimeException("Type not available");
        }
    }

    private IActuator createCloudTM(){
        log.info("Creating CloudTM actuator...");
        if(isRadargun){
            return new CloudTMActuator( createDeltaCloudClient(), createRadargunClient(), jmxPort, imageId, flavorId, domain, cacheName ); // with radargun
        } else {
            return new CloudTMActuator( createDeltaCloudClient(), jmxPort, imageId, flavorId, domain, cacheName );
        }
    }

    private IActuator createFutureGrid(){
        log.info("Creating FutureGrid actuator...");
        if(isRadargun){
            return  new FutureGridActuator(createRadargunClient(), jmxPort, domain, cacheName, availableMachines() ); // with radargun
        } else {
            return  new FutureGridActuator( jmxPort, domain, cacheName, availableMachines() );
        }
    }

    private Set<String> availableMachines(){
        Set<String> availableMachines = new HashSet<String>();
        Scanner sc = null;
        try {
            sc = new Scanner(new File( Config.getInstance().getString( KeyConfig.FUTUREGRID_FILE_NODES.key() ) ) );
        } catch (FileNotFoundException e) {
            log.warn(e);
            throw new RuntimeException(e);
        }
        while (sc.hasNextLine()) {
            String aLine = sc.nextLine();
            if(aLine.length()<=0){
                continue;
            }

            if( aLine.startsWith("SLAVE:") ){
                String machine = aLine.split(":")[1];
                log.trace("Found available machine: " + machine);
                availableMachines.add(machine);
            } else {
                log.trace("Not valid slave machine (" + aLine + ")" );
            }
        }
        return availableMachines;
    }

    private DeltaCloudClient createDeltaCloudClient(){
        String hostname = Config.getInstance().getString( KeyConfig.DELTACLOUD_URL.key() );
        String username = Config.getInstance().getString( KeyConfig.DELTACLOUD_USER.key() );
        String password = Config.getInstance().getString( KeyConfig.DELTACLOUD_PASSWORD.key() );
        DeltaCloudClient deltaCloudClient;
        try {
            deltaCloudClient = new DeltaCloudClientImpl(hostname, username, password);
            return deltaCloudClient;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (DeltaCloudClientException e) {
            throw new RuntimeException(e);
        }
    }

    private RadargunClient createRadargunClient(){

        String actuator = Config.getInstance().getString( KeyConfig.RADARGUN_CLIENT.key() );

        if(actuator.equals("JMX")){
            return new RadargunClientJMX( Config.getInstance().getString( KeyConfig.RADARGUN_COMPONENT.key() ) );
        } else {
            // TO IMPLEMENT SLAVEKILLER CLIENT
            throw new RuntimeException("TO IMPLEMENT");
        }
    }

}
