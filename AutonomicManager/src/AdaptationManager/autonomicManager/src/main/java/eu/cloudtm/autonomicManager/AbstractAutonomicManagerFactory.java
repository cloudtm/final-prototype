package eu.cloudtm.autonomicManager;

import eu.cloudtm.autonomicManager.RESTServer.RESTServer;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.PlatformTuning;
import eu.cloudtm.autonomicManager.commons.State;
import eu.cloudtm.autonomicManager.workloadAnalyzer.WorkloadAnalyzer;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/24/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractAutonomicManagerFactory {

    public AutonomicManager build();

    public PlatformConfiguration getPlatformConfiguration();

    public Reconfigurator getReconfigurator();

    public Optimizer getOptimizer();

    public SLAManager getSLAManager();

    public WorkloadAnalyzer getWorkloadAnalyzer();

}
