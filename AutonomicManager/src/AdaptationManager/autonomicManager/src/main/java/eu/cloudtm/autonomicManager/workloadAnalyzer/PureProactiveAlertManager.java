package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.AbstractPlatformOptimizer;
import eu.cloudtm.autonomicManager.Optimizer;
import eu.cloudtm.autonomicManager.ReconfiguratorImpl;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/19/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PureProactiveAlertManager extends AbstractAlertManager {

    public PureProactiveAlertManager(AbstractChangeDetector changeDetector,
                                     Optimizer optimizer,
                                     ReconfiguratorImpl reconfigurator) {
        super(optimizer, reconfigurator);
        throw new RuntimeException("NOT IMPLEMENTED YET");
        // TODO implement this
    }


    @Override
    public void workloadEventPerformed(WorkloadEvent e) {

    }
}
