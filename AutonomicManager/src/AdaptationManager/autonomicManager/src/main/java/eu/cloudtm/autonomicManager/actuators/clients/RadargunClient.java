package eu.cloudtm.autonomicManager.actuators.clients;

import eu.cloudtm.autonomicManager.actuators.excepions.RadargunException;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/26/13
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RadargunClient {

    public void stop(String slaveHost, int jmxPort) throws RadargunException;

}
