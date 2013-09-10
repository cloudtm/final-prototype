package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.WPMInputOracleDumper;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.InstanceConfig;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/14/13
 */
public class OracleService implements IOracleService {

    private static Log log = LogFactory.getLog(OracleService.class);

    private Oracle oracle;

    private int nodesMin = 2, nodesMax = 10; // TODO: da rendere parametrizzabili

    private int degreeMin = 2;

    private boolean dump = false;

    public OracleService(Oracle oracle){
        this.oracle = oracle;
    }

    public static OracleService getInstance(String oracleName) {
        if (oracleName.indexOf('.') < 0) {
            oracleName = "eu.cloudtm.autonomicManager.oracles." + oracleName;
        }


        try {
            Oracle obj;
            Constructor c = Class.forName(oracleName).getConstructor();
            obj = (Oracle) c.newInstance();
            OracleService oracleService = new OracleService(obj);
            return oracleService;
        } catch (ClassNotFoundException e) {
            log.error(e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            log.error(e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error(e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            log.error(e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private boolean evaluateKPI(OutputOracle outputOracle,
                                double abortRateToGuarantee,
                                double responseTimeToGuarantee ) {

        if(outputOracle.abortRate(0) < abortRateToGuarantee){
            return false;
        }

        log.warn("WARN WARN WARN WARN WARN WARN WARN WARN WARN WARN WARN WARN");
        log.warn("EDITO RESPONSE TIME");
        log.warn("WARN WARN WARN WARN WARN WARN WARN WARN WARN WARN WARN WARN");
        if(responseTimeToGuarantee < responseTimeToGuarantee){
            return false;
        }
        return true;
    }


    public PlatformConfiguration minimizeCosts(ProcessedSample sample,
                                               double arrivalRateToGuarantee,
                                               double abortRateToGuarantee,
                                               double responseTimeToGuarantee)
            throws OracleException {

        PlatformConfiguration configuration = exploreAllCases(sample, arrivalRateToGuarantee, abortRateToGuarantee, responseTimeToGuarantee);
        return configuration;
    }

    /**
     * What-if with Protocols on X-axis
     * @param sample
     * @param fixedNodes
     * @param fixedDegree
     * @return
     */
    public TreeMap<PlatformConfiguration, OutputOracle> whatIf(ProcessedSample sample, int fixedNodes, int fixedDegree) {

        TreeMap<PlatformConfiguration, OutputOracle> result = new TreeMap<PlatformConfiguration, OutputOracle>();
        if(fixedNodes <= 1)
            throw new IllegalArgumentException("fixedDegree must be > 1");
        if(fixedDegree > fixedNodes)
            throw new IllegalArgumentException("fixedDegree must be >= fixedNodes");

        for( ReplicationProtocol protocol : ReplicationProtocol.values() ){

            log.info("Querying with <" + fixedNodes + "," + fixedDegree + "," + protocol + ">");

            PlatformConfiguration currConf = new PlatformConfiguration(fixedNodes, fixedDegree, protocol);

            Map<ForecastParam, Object> forecastParam = new HashMap<ForecastParam, Object>();
            forecastParam.put(ForecastParam.NumNodes, fixedNodes );
            forecastParam.put(ForecastParam.ReplicationDegree, fixedDegree );
            forecastParam.put(ForecastParam.ReplicationProtocol, protocol );

            InputOracleWPM inputOracle = new InputOracleWPM(sample, forecastParam);

            if(dump){
                WPMInputOracleDumper dumper = new WPMInputOracleDumper(inputOracle);
                try {
                    dumper.dump("dump_protocol_" + protocol);
                } catch (ParserConfigurationException e) {
                    log.warn(e,e);
                    throw new RuntimeException(e);
                } catch (TransformerException e) {
                    log.warn(e,e);
                    throw new RuntimeException(e);
                }
            }

            OutputOracle currOutputOracle = null;
            try {
                currOutputOracle = oracle.forecast(inputOracle);
            } catch (OracleException e) {
                log.warn("Oracle exception during what if analysis...skipping that configuration");
                log.warn(e,e);
            }
            result.put(currConf, currOutputOracle);
        }
        return result;
    }

    /**
     * What-if with Degree on X-axis
     * @param sample
     * @param fixedNodes
     * @param fixedProtocol
     * @return
     */
    public TreeMap<PlatformConfiguration, OutputOracle> whatIf(ProcessedSample sample, int fixedNodes, ReplicationProtocol fixedProtocol) {

        TreeMap<PlatformConfiguration, OutputOracle> result = new TreeMap<PlatformConfiguration, OutputOracle>();
        if(fixedNodes <= 1)
            throw new IllegalArgumentException("fixedDegree must be > 0");
        if(fixedProtocol == null)
            throw new IllegalArgumentException("fixedProtocol must be not null");

        for( int degree = degreeMin; degree<=fixedNodes; degree++){

            log.info("Querying with <" + fixedNodes + "," + degree + "," + fixedProtocol + ">");
            PlatformConfiguration currConf = new PlatformConfiguration(fixedNodes, degree, fixedProtocol);

            Map<ForecastParam, Object> forecastParam = new HashMap<ForecastParam, Object>();
            forecastParam.put(ForecastParam.NumNodes, fixedNodes );
            forecastParam.put(ForecastParam.ReplicationDegree, degree );
            forecastParam.put(ForecastParam.ReplicationProtocol, fixedProtocol );

            InputOracleWPM inputOracle = new InputOracleWPM(sample, forecastParam);

            if(dump){
                WPMInputOracleDumper dumper = new WPMInputOracleDumper(inputOracle);
                try {
                    dumper.dump("dump_degree_" + degree);
                } catch (ParserConfigurationException e) {
                    log.warn(e,e);
                    throw new RuntimeException(e);
                } catch (TransformerException e) {
                    log.warn(e,e);
                    throw new RuntimeException(e);
                }
            }

            OutputOracle currOutputOracle = null;
            try {
                currOutputOracle = oracle.forecast(inputOracle);
            } catch (OracleException e) {
                log.warn("Oracle exception during what if analysis...skipping that configuration");
                log.warn(e,e);
            }
            if(currOutputOracle != null){
                log.info("currConf: " + currConf + " - currOutputOracle: " + currOutputOracle);
            } else {
                log.info("NULL returned!!");
            }

            result.put(currConf, currOutputOracle);
        }
        return result;
    }

    /**
     * What-if with Nodes on X-axis
     * @param sample
     * @param fixedProtocol
     * @param fixedDegree
     * @return
     */
    @Override
    public TreeMap<PlatformConfiguration, OutputOracle> whatIf(ProcessedSample sample, ReplicationProtocol fixedProtocol, int fixedDegree) {

        TreeMap<PlatformConfiguration, OutputOracle> result = new TreeMap<PlatformConfiguration, OutputOracle>();
        if(fixedDegree <= 0)
            throw new IllegalArgumentException("fixedDegree must be > 0");
        if(fixedProtocol == null)
            throw new IllegalArgumentException("fixedDegree must be not null");


        for( int nodes = nodesMin; nodes<= nodesMax; nodes++){

            log.info("Querying with <" + nodes + "," + fixedDegree + "," + fixedProtocol + ">");

            PlatformConfiguration currConf = new PlatformConfiguration(nodes, fixedDegree, fixedProtocol);

            Map<ForecastParam, Object> forecastParam = new HashMap<ForecastParam, Object>();
            forecastParam.put(ForecastParam.NumNodes, nodes );
            forecastParam.put(ForecastParam.ReplicationDegree, fixedDegree );
            forecastParam.put(ForecastParam.ReplicationProtocol, fixedProtocol );

            InputOracleWPM inputOracle = new InputOracleWPM(sample, forecastParam);

            if(dump){
                WPMInputOracleDumper dumper = new WPMInputOracleDumper(inputOracle);
                try {
                    dumper.dump("dump_nodes_" + nodes);
                } catch (ParserConfigurationException e) {
                    log.warn(e,e);
                    throw new RuntimeException(e);
                } catch (TransformerException e) {
                    log.warn(e,e);
                    throw new RuntimeException(e);
                }
            }

            OutputOracle currOutputOracle = null;
            try {
                currOutputOracle = oracle.forecast(inputOracle);
            } catch (OracleException e) {
                log.warn("Oracle exception during what if analysis...skipping that configuration");
                log.warn(e,e);
            }
            result.put(currConf, currOutputOracle);
        }
        return result;
    }

    @Override
    public PlatformConfiguration maximizeThroughput(ProcessedSample sample) throws OracleException {

        int finalNumNodes = 0;
        int finalRepDegree = 0;
        ReplicationProtocol finalRepProt = null;
        boolean found = false;
        double maxThroughput = 0;

        int numNodes= nodesMin;

        while( numNodes<= nodesMax){
            int repDegree= nodesMin;
            while( repDegree<=numNodes ){
                for(ReplicationProtocol protocol : ReplicationProtocol.values()){

                    Map<ForecastParam, Object> forecastParams = new HashMap<ForecastParam, Object>();
                    forecastParams.put(ForecastParam.ReplicationProtocol, protocol);
                    forecastParams.put(ForecastParam.ReplicationDegree, repDegree);
                    forecastParams.put(ForecastParam.NumNodes, numNodes);

                    InputOracleWPM inputOracle = new InputOracleWPM(sample, forecastParams);

//                    ControllerLogger.log.info("Forecasting with: " +
//                            "nodes "      + numNodes + ", " +
//                            "repDegree "    + repDegree + ", " +
//                            "repProt "      + protocol
//                    );
                    OutputOracle outputOracle = oracle.forecast( inputOracle );


                    if( outputOracle.throughput(0) > maxThroughput ){
                        finalNumNodes = numNodes;
                        finalRepDegree = repDegree;
                        finalRepProt = protocol;
                    }
                }
                repDegree++;
            }
            numNodes++;
        }

        PlatformConfiguration configuration = null;

        configuration = new PlatformConfiguration();
        configuration.setPlatformScale(finalNumNodes, InstanceConfig.MEDIUM);
        configuration.setRepDegree(finalRepDegree);
        configuration.setRepProtocol(finalRepProt);

        return configuration;

    }




    private PlatformConfiguration exploreAllCases(ProcessedSample sample,
                                                  double arrivalRateToGuarantee,
                                                  double abortRateToGuarantee,
                                                  double responseTimeToGuarantee) throws OracleException{

        int finalNumNodes = 0;
        int finalRepDegree = 0;
        ReplicationProtocol finalRepProt = null;
        boolean found = false;

        int numNodes= nodesMin;

        while(numNodes<= nodesMax && !found){
            int repDegree= nodesMin;
            while(repDegree<=numNodes && !found){
                for(ReplicationProtocol protocol : ReplicationProtocol.values()){

                    Map<ForecastParam, Object> forecastParams = new HashMap<ForecastParam, Object>();
                    forecastParams.put(ForecastParam.ReplicationProtocol, protocol);
                    forecastParams.put(ForecastParam.ReplicationDegree, repDegree);
                    forecastParams.put(ForecastParam.NumNodes, numNodes);

                    InputOracleWPM inputOracle = new InputOracleWPM(sample, forecastParams);

//                    ControllerLogger.log.info("Forecasting with: " +
//                            "nodes "      + numNodes + ", " +
//                            "repDegree "    + repDegree + ", " +
//                            "repProt "      + protocol
//                    );
                    OutputOracle outputOracle = oracle.forecast( inputOracle );

                    if( outputOracle.throughput(0) >= arrivalRateToGuarantee && outputOracle.abortRate(0) <= abortRateToGuarantee ){
                        found = true;
                        finalNumNodes = numNodes;
                        finalRepDegree = repDegree;
                        finalRepProt = protocol;
                        break;
                    }
                }
                repDegree++;
            }
            numNodes++;
        }

        PlatformConfiguration configuration = null;
        if(found){
            configuration = new PlatformConfiguration();
            configuration.setPlatformScale(finalNumNodes, InstanceConfig.MEDIUM);
            configuration.setRepDegree(finalRepDegree);
            configuration.setRepProtocol(finalRepProt);
        }
        return configuration;
    }

}


//    private OutputOracle binarySearch(AbstractProcessedSample sample,
//                             double arrivalRateToGuarantee,
//                             double abortRateToGuarantee,
//                             double responseTimeToGuarantee)
//            throws OracleException {
//
//        double throughputForecasted;
//
//        OutputOracle subOptKPI = null;
//        while(nodesMax>=nodesMin){
//            log.info("current metrics: " +
//                    "[ arrivalRateToGuarantee:" + arrivalRateToGuarantee +"]" +
//                    "[ SLA_abortRate:" + abortRateToGuarantee +"]" +
//                    "[ SLA_responseTime:" + responseTimeToGuarantee +"]" );
//
//            int imid = (int) ( Math.floor(nodesMin+nodesMax)/2 );
//
//
//            InputOracleWPM inputOracle = new InputOracleWPM(sample, null);
//            // TODO CARICARE l'inputORACLE
//
//            OutputOracle kpi = forecast( inputOracle );
//            //log.info(kpi.getPlatformConfiguration().platformSize() + " throughput = " + kpi.getThroughput() * 1e9 + " txs/sec, rtt = " + kpi.getRtt() + " msec, abortProb = " + kpi.getAbortProbability());
//
//            throughputForecasted = kpi.throughput();
//            log.info("Throughput sopportabile con " + imid + ": " + throughputForecasted);
//
//            if( throughputForecasted < arrivalRateToGuarantee ){
//                nodesMin = imid+1; // questa configurazione non mi da garanzie sufficienti
//            } else if( throughputForecasted > arrivalRateToGuarantee ){
//                nodesMax = imid-1;
//                if( evaluateKPI(kpi, abortRateToGuarantee, responseTimeToGuarantee ) ){
//                    if(subOptKPI!=null){
//                        if( subOptKPI.getPlatformConfiguration().platformSize() > kpi.getPlatformConfiguration().platformSize() ){
//                            subOptKPI = kpi;
//                        }
//                    } else {
//                        subOptKPI = kpi;
//                    }
//                }
//            } else {
//                if( evaluateKPI(kpi, abortRateToGuarantee, responseTimeToGuarantee ) ){
//                    log.info("Opt Config: throughput=" + kpi.throughput());
//                    return kpi;
//                }
//            }
//        }
//        if(subOptKPI != null) log.info("SubOpt Config: throughput=" + subOptKPI.throughput());
//        else log.info("NO SOLUTION");
//        return subOptKPI;
//    }