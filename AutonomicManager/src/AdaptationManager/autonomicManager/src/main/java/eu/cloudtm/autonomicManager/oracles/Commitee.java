package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.commons.Forecaster;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/15/13
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Commitee implements IOracleService {

    private static Log log = LogFactory.getLog(Commitee.class);

    private Oracle analytical = new OracleTAS();
    private Oracle simulator = new Simulator();
    private Oracle morpher = new FakeMLOracle();

    private ReplicationProtocol defaultProt = ReplicationProtocol.TWOPC;

    private Set<Forecaster> forecasters = new HashSet<Forecaster>(){{
        add(Forecaster.ANALYTICAL);
        add(Forecaster.SIMULATOR);
        add(Forecaster.MACHINE_LEARNING);
    }};


    @Override
    public PlatformConfiguration minimizeCosts(ProcessedSample sample, double arrivalRateToGuarantee, double abortRateToGuarantee, double responseTimeToGuarantee) throws OracleException {

        Map<Forecaster, PlatformConfiguration> forecaster2conf = new HashMap<Forecaster, PlatformConfiguration>();

        for (Forecaster forecaster : forecasters){

            OracleService oracleService = OracleService.getInstance(forecaster.getOracleClass());
            PlatformConfiguration forecastedConfig = null;
            try {
                forecastedConfig = oracleService.minimizeCosts(sample,
                        arrivalRateToGuarantee,
                        abortRateToGuarantee,
                        responseTimeToGuarantee );
            } catch (OracleException e) {
                log.info(forecaster + " thrown an exception...don't using it during the voting...");
            }
            if( forecastedConfig != null)
                forecaster2conf.put(forecaster, forecastedConfig);
        }
        return doVoting(forecaster2conf);
    }

    @Override
    public Map<PlatformConfiguration, OutputOracle> whatIf(ProcessedSample sample, ReplicationProtocol repProtocol, int repDegree){
        throw new RuntimeException("TO IMPLEMENT");
    }

    @Override
    public PlatformConfiguration maximizeThroughput(ProcessedSample sample) throws OracleException {
        throw new RuntimeException("TO IMPLEMENT");
    }

    private PlatformConfiguration doVoting(Map<Forecaster, PlatformConfiguration> forecaster2conf){

        int size = 0;
        int degree = 0;
        ReplicationProtocol replicationProtocol = null;
        Map<ReplicationProtocol, Integer> repProtocol = new HashMap<ReplicationProtocol, Integer>();

        for (Map.Entry<Forecaster, PlatformConfiguration> conf : forecaster2conf.entrySet()){
            size += conf.getValue().platformSize();
            degree += conf.getValue().replicationDegree();

            Integer currVal = repProtocol.get(conf.getValue().replicationProtocol());
            int val = (currVal==null) ? 1 : currVal+1;
            repProtocol.put(conf.getValue().replicationProtocol(), val);
        }

        size = size / forecaster2conf.size();
        degree = degree / forecaster2conf.size();
        for (Map.Entry<ReplicationProtocol, Integer> int2repProt : repProtocol.entrySet()){
            if(int2repProt.getValue() > repProtocol.size()/2.0 ) {
                replicationProtocol = int2repProt.getKey();
                break;
            }

        }
        if(replicationProtocol == null){
            replicationProtocol = defaultProt;
        }

        log.info("Configurazione scelta:");
        log.info("runningInstancesSize: " + size);
        log.info("degree: " + degree);
        log.info("repProt: " + replicationProtocol);

        return new PlatformConfiguration(size, degree, replicationProtocol);
    }

}
