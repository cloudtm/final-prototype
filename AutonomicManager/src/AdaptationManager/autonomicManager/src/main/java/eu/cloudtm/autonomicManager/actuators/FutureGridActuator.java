package eu.cloudtm.autonomicManager.actuators;

import com.jcraft.jsch.*;
import eu.cloudtm.InfinispanClient.InfinispanClient;
import eu.cloudtm.InfinispanClient.InfinispanClientImpl;
import eu.cloudtm.InfinispanClient.InfinispanMachine;
import eu.cloudtm.InfinispanClient.exception.InvocationException;
import eu.cloudtm.InfinispanClient.exception.NoJmxProtocolRegisterException;
import eu.cloudtm.autonomicManager.ControllerLogger;
import eu.cloudtm.autonomicManager.IActuator;
import eu.cloudtm.autonomicManager.actuators.clients.RadargunClient;
import eu.cloudtm.autonomicManager.actuators.excepions.ActuatorException;
import eu.cloudtm.autonomicManager.actuators.excepions.RadargunException;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.configs.Config;
import eu.cloudtm.autonomicManager.configs.KeyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Author: Fabio Perfetti (perfabio87 [at] gmail.com)
 * Date: 8/6/13
 * Time: 10:54 AM
 */
public class FutureGridActuator implements IActuator {

    private static Log log = LogFactory.getLog(FutureGridActuator.class);

    private JSch jsch=new JSch();

    private final String user = "root";

    private final int sshPort = 22;

    private final String privateKey = "~/.ssh/id_rsa";

    private Session session;

    /* FUTUREGRID */

    private final String rg_master = "149.165.148.113";

    private final Set<String> availableMachines = Collections.synchronizedSet ( new HashSet<String>() );

    private Set<String> runningMachines = Collections.synchronizedSet(new HashSet<String>());


    /* RADARGUN CLIENT */
    private final boolean isRadargun;

    private final RadargunClient radargunClient;

    protected final int jmxPort;


    /* INFINISPAN CLIENT */
    private final String ispnDomain;

    private final String ispnCacheName;



    public FutureGridActuator(int jmxPort,
                              String ispnDomain,
                              String ispnCacheName,
                              Set<String> availableMachines){
        this.isRadargun = false;
        this.radargunClient = null;
        this.jmxPort = jmxPort;
        this.ispnDomain = ispnDomain;
        this.ispnCacheName = ispnCacheName;

        this.availableMachines.addAll(availableMachines);
        log.info("AvailableMachines: " + this.availableMachines.size());

    }

    public FutureGridActuator(RadargunClient radargunClient,
                              int jmxPort,
                              String ispnDomain,
                              String ispnCacheName,
                              Set<String> availableMachines){
        this.isRadargun = true;
        this.radargunClient = radargunClient;
        this.jmxPort = jmxPort;
        this.ispnDomain = ispnDomain;
        this.ispnCacheName = ispnCacheName;

        this.availableMachines.addAll(availableMachines);
        log.info("AvailableMachines: " + this.availableMachines.size());
    }



    @Override
    public synchronized void stopInstance() throws ActuatorException {

        final String command = Config.getInstance().getString( KeyConfig.FUTUREGRID_STOP_SCRIPT.key() );
        final String machine = runningMachines.iterator().next();

        if(isRadargun){
            ControllerLogger.log.info(" * Stopping radargun..." );

            try {
                radargunClient.stop(machine, jmxPort);
            } catch (RadargunException e) {
                throw new ActuatorException(e);
            }
        }

        try {
            Session session = createSession( machine );
            executeCommand(session, command);

            if(runningMachines.remove(machine) ){
                availableMachines.add(machine);
            }

            log.info("VM " + machine + " stopped!");

        } catch (JSchException e) {
            throw new ActuatorException(e);
        } catch (IOException e) {
            throw new ActuatorException(e);
        }

        log.info("available: " + availableMachines.size());
    }

    @Override
    public synchronized void startInstance() throws ActuatorException {

        final String command = Config.getInstance().getString( KeyConfig.FUTUREGRID_START_SCRIPT.key() );
        final String machine = availableMachines.iterator().next();

        try {
            Session session = createSession( machine );
            executeCommand(session, command);

            if(availableMachines.remove(machine) ){
                runningMachines.add(machine);
            }
            log.info("VM " + machine + " started!");


        } catch (JSchException e) {
            throw new ActuatorException(e);
        } catch (IOException e) {
            throw new ActuatorException(e);
        } finally {
            session.disconnect();
        }

        log.info("available: " + availableMachines.size());
    }

    @Override
    public synchronized List<String> runningInstances() {
        return new ArrayList<String>(runningMachines);
    }

    @Override
    public void switchProtocol(ReplicationProtocol repProtocol) throws ActuatorException {

        Set<InfinispanMachine> ispnMachines = null;
        try {
            ispnMachines = instacesToIspnMachines();
        } catch (UnknownHostException e) {
            throw new ActuatorException(e);
        }

        if(ispnMachines.size()==0){
            log.info("No instances. Skipping...");
            return;
        }

        InfinispanClient infinispanClient = new InfinispanClientImpl(ispnMachines, ispnDomain, ispnCacheName);

        try {
            infinispanClient.triggerBlockingSwitchReplicationProtocol(repProtocol.getWpmValue(), false, false);

        } catch (InvocationException e) {
            throw new ActuatorException(e);
        } catch (NoJmxProtocolRegisterException e) {
            throw new ActuatorException(e);
        }
    }

    @Override
    public void switchDegree(int degree) throws ActuatorException {
        Set<InfinispanMachine> ispnMachines = null;
        try {
            ispnMachines = instacesToIspnMachines();
        } catch (UnknownHostException e) {
            throw new ActuatorException(e);
        }

        if(ispnMachines.size()==0){
            log.info("No instances. Skipping...");
            return;
        }

        InfinispanClient infinispanClient = new InfinispanClientImpl(ispnMachines, ispnDomain, ispnCacheName);

        try {
            infinispanClient.triggerBlockingSwitchReplicationDegree(degree);

        } catch (InvocationException e) {
            ControllerLogger.log.warn(e);
            throw new ActuatorException(e);
        } catch (NoJmxProtocolRegisterException e) {
            ControllerLogger.log.warn(e);
            throw new ActuatorException(e);
        }
    }

    @Override
    public void triggerRebalancing(boolean enabled) throws ActuatorException {
        Set<InfinispanMachine> ispnMachines = null;
        try {
            ispnMachines = instacesToIspnMachines();
        } catch (UnknownHostException e) {
            throw new ActuatorException(e);
        }

        if(ispnMachines.size()==0){
            log.info("No instances. Skipping...");
            return;
        }

        InfinispanClient infinispanClient = new InfinispanClientImpl(ispnMachines, ispnDomain, ispnCacheName);

        try {
            infinispanClient.triggerRebalancing(enabled);

        } catch (InvocationException e) {
            ControllerLogger.log.warn(e);
            throw new ActuatorException(e);
        } catch (NoJmxProtocolRegisterException e) {
            ControllerLogger.log.warn(e);
            throw new ActuatorException(e);
        }
    }

    private Session createSession(String host) throws JSchException {

        jsch.addIdentity(privateKey);
        log.trace("identity added ");
        session = jsch.getSession(user, host, sshPort);
        log.trace("session created.");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
        log.trace("session connected.....");

        return session;
    }


    private void executeCommand(Session session, String command) throws JSchException, IOException {
        Channel channel= null;
        channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        // X Forwarding
        // channel.setXForwarding(true);

        //channel.setInputStream(System.in);
        channel.setInputStream(null);

        //channel.setOutputStream(System.out);

        //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
        //((ChannelExec)channel).setErrStream(fos);
        ((ChannelExec)channel).setErrStream(System.err);

        InputStream in = null;
        in = channel.getInputStream();

        channel.connect();

        byte[] tmp=new byte[1024];
        while(true){
            while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                log.trace(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
                log.trace("exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        channel.disconnect();
    }

    private Set<InfinispanMachine> instacesToIspnMachines() throws UnknownHostException {
        Set<InfinispanMachine> ispnMachines = new HashSet<InfinispanMachine>();
        for( String machine : runningInstances() ){
            String hostname = InetAddress.getByName(machine).getHostName();
            String address = machine;
            log.info("Mapping FutureGrid instances to infinispan machines (" +  hostname + ", " + address + ")" );

            ispnMachines.add( new InfinispanMachine(hostname, jmxPort, address) );
        }
        return ispnMachines;
    }

}
