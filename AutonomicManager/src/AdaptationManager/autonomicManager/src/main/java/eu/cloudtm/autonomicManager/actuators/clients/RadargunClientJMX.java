package eu.cloudtm.autonomicManager.actuators.clients;

import eu.cloudtm.autonomicManager.actuators.excepions.RadargunException;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/26/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */

public class RadargunClientJMX implements RadargunClient {

    private final String component;

    private static final String COMPONENT_PREFIX = "org.radargun:stage=";

    private static ObjectName benchmarkComponent;
    private static MBeanServerConnection mBeanServerConnection;

    public RadargunClientJMX(String component){
        this.component = component;
    }

    @Override
    public void stop(String slaveHost, int jmxPort) throws RadargunException {

        String connectionUrl = "service:jmx:rmi:///jndi/rmi://" +
                slaveHost + ":" + jmxPort + "/jmxrmi";

        JMXConnector connector = null;
        try {
            connector = JMXConnectorFactory.connect(new JMXServiceURL(connectionUrl));
            mBeanServerConnection = connector.getMBeanServerConnection();
            benchmarkComponent = new ObjectName(COMPONENT_PREFIX + component);
        } catch (MalformedURLException e) {
            throw new RadargunException(e);
        } catch (MalformedObjectNameException e) {
            throw new RadargunException(e);
        } catch (IOException e) {
            throw new RadargunException(e);
        }


        if (benchmarkComponent == null) {
            throw new NullPointerException("Component does not exists");
        }

        try {
            mBeanServerConnection.invoke(benchmarkComponent, "stopBenchmark", new Object[0], new String[0]);
        } catch (InstanceNotFoundException e) {
            throw new RadargunException(e);
        } catch (MBeanException e) {
            throw new RadargunException(e);
        } catch (ReflectionException e) {
            throw new RadargunException(e);
        } catch (IOException e) {
            throw new RadargunException(e);
        }
    }
}