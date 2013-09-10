package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Random;


/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/12/13
 */
public class OracleTAS implements Oracle {

    private static Log log = LogFactory.getLog(OracleTAS.class);

    @Override
    public OutputOracle forecast(InputOracle inputOracle) throws OracleException {
        Random rnd = new Random();
        return new OutputOracleImpl(rnd.nextInt(3000), rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble());
    }
}



//
//    public OracleTAS(){
//
//    }
//
//    public OracleTAS(ControllerOld _controller) {
//        super(_controller);
//    }
//
//    @Override
//    public OutputOracleImpl forecast(AbstractProcessedSample input) throws OracleException {
//        DSTMScenarioTas2 scenario;
//
//        try {
//            scenario = DSTMScenarioFactory.buildScenario(input, ControllerOld.TIME_WINDOW);
//        } catch (PublishAttributeException e) {
//            throw new RuntimeException(e);
//        } catch (Tas2Exception e) {
//            throw new OracleException(e);
//        }
//
//        try {
//            return realForecast(scenario, numNodes, numThreads);
//        } catch (Tas2Exception e) {
//            throw new OracleException(e);
//        }
//    }

//    @Override
//    public OutputOracleImpl forecastWithCustomParam(WPMSample sample, WhatIfCustomParamDTO customParam, int numNodes, int numThreads) throws OracleException {
//        DSTMScenarioTas2 scenario = null;
//
//        try {
//            scenario = DSTMScenarioFactory.buildCustomScenario(sample.getJmx(),
//                    sample.getMem(),
//                    customParam,
//                    numNodes,
//                    numThreads,
//                    ControllerOld.TIME_WINDOW);
//
//        } catch (PublishAttributeException e) {
//            throw new RuntimeException(e);
//        } catch (Tas2Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            return realForecast(scenario,numNodes, numThreads);
//        } catch (Tas2Exception e) {
//            throw new OracleException(e);
//        }
//    }

//    private OutputOracleImpl realForecast(DSTMScenarioTas2 scenario, int numNodes, int numThreads) throws Tas2Exception {
//        ModelResult result;
//        try {
//            log.info("calling tas");
//            result = new Tas2().solve(scenario);
//            log.info("called tas");
//        } catch (Tas2Exception e) {
//            throw e;
//        }
//
//        double throughput, abortP, rtt;
//        throughput = result.getMetrics().getThroughput() * 1e9;
//        rtt = result.getMetrics().getPrepareRtt();
//        abortP = (1.0D - result.getProbabilities().getPrepareProbability() * result.getProbabilities().getCoherProbability());
//
//        PlatformConfiguration config = new PlatformConfiguration(numNodes, numThreads,
//                InstanceConfig.MEDIUM,
//                ReplicationProtocol.TWOPC,
//                2,
//                false);
//
//        OutputOracleImpl ret = new OutputOracleImpl(config, throughput, abortP, rtt);
//
//        return ret;
//    }
//
//    @Override
//    public String toString(){
//        return "OracleTAS";
//    }
