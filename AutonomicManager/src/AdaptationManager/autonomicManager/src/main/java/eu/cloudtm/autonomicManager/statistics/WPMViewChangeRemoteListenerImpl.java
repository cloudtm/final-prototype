package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.wpm.connector.WPMConnector;
import eu.cloudtm.wpm.logService.remote.events.PublishViewChangeEvent;
import eu.cloudtm.wpm.logService.remote.events.SubscribeEvent;
import eu.cloudtm.wpm.logService.remote.listeners.WPMViewChangeRemoteListener;
import eu.cloudtm.wpm.logService.remote.observables.Handle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/10/13
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class WPMViewChangeRemoteListenerImpl implements WPMViewChangeRemoteListener {

    private static Log log = LogFactory.getLog(WPMViewChangeRemoteListenerImpl.class);

    private WPMConnector connector;

    private String[] currentVMs;

    private WPMStatisticsRemoteListernerFactory statisticsListernerFactory;

    private Handle lastHandle;

    public WPMViewChangeRemoteListenerImpl(WPMConnector connector, WPMStatisticsRemoteListernerFactory statisticsListernerFactory){
        this.connector = connector;
        this.statisticsListernerFactory = statisticsListernerFactory;
        registerViewChangeListener();
    }

    private void registerViewChangeListener(){
        try {
            connector.registerViewChangeRemoteListener(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onViewChange(PublishViewChangeEvent event) throws RemoteException {

        if(lastHandle != null){
            log.trace("Removing previous StatisticsListener");
            connector.removeStatisticsRemoteListener(lastHandle);
        }

        currentVMs = event.getCurrentVMs();

        if (currentVMs == null) {
            log.info("The set of VMs is empty. No-op");
            return;
        }

        log.info("New set of VMs " + Arrays.toString(currentVMs));

        lastHandle = statisticsListernerFactory.build(new SubscribeEvent(currentVMs));
    }
}
