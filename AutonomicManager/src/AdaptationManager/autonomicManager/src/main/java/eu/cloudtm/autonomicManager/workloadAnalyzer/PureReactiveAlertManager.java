package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.AbstractPlatformOptimizer;
import eu.cloudtm.autonomicManager.ControllerLogger;
import eu.cloudtm.autonomicManager.Optimizer;
import eu.cloudtm.autonomicManager.Reconfigurator;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/19/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PureReactiveAlertManager extends AbstractAlertManager {

    private static Log log = LogFactory.getLog(PureReactiveAlertManager.class);

    private ReentrantLock reconfigurationLock = new ReentrantLock();

    public PureReactiveAlertManager(Optimizer optimizer,
                                    Reconfigurator reconfigurator) {
        super(optimizer, reconfigurator);
    }


    @Override
    public void workloadEventPerformed(WorkloadEvent event) {

        if( !event.getEventType().equals(WorkloadEvent.WorkloadEventType.WORKLOAD_CHANGED) ){
            return;
        }

        if( !reconfigurator.isReconfiguring() ){
            if( reconfigurationLock.tryLock() ){
                ControllerLogger.log.info("Lock successfully acquired");
                try{
                    optimizer.optimize( event.getSample() );
                } finally {
                    ControllerLogger.log.info("releasing lock");
                    reconfigurationLock.unlock();
                }
            }
        } else {
            ControllerLogger.log.info("ReconfiguratorImpl busy! Skipping new reconf...");
        }
    }
}
