package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/15/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IOracleService {

    public PlatformConfiguration minimizeCosts(ProcessedSample sample,
                                               double arrivalRateToGuarantee,
                                               double abortRateToGuarantee,
                                               double responseTimeToGuarantee) throws OracleException;


    public Map<PlatformConfiguration, OutputOracle> whatIf(ProcessedSample sample, ReplicationProtocol repProtocol, int repDegree);


    public PlatformConfiguration maximizeThroughput(ProcessedSample sample) throws OracleException;

}
