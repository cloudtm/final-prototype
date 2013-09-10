package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.wpm.connector.WPMConnector;
import eu.cloudtm.wpm.logService.remote.events.SubscribeEvent;
import eu.cloudtm.wpm.logService.remote.observables.Handle;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/10/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class WPMStatisticsRemoteListernerFactory {

    private StatsManager dispatcher;
    private WPMConnector connector;
    private PlatformConfiguration currentConfig;

    public WPMStatisticsRemoteListernerFactory(WPMConnector connector, StatsManager dispatcher, PlatformConfiguration currentConfig){
        this.connector = connector;
        this.dispatcher = dispatcher;
        this.currentConfig = currentConfig;
    }

    public Handle build(SubscribeEvent subscribeEvent){
        Processor processor = new StatsProcessor(currentConfig);
        return new WPMStatisticsRemoteListenerImpl(connector, dispatcher, subscribeEvent, processor).getHandle();
    }

}
