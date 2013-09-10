package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.commons.*;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.TWOPCProcessedSample;
import eu.cloudtm.autonomicManager.statistics.WPMSample;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/24/13
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestFakeOracle {

    public static void main(String[] args) throws OracleException {

        int imin = 2, imax = 10;

        FakeAnalyticalOracle fake = new FakeAnalyticalOracle();


        for(int i = 0; i<=20;i++){
            System.out.println("Call #" + i);

            int finalNumNodes = 0;
            int finalRepDegree = 0;
            ReplicationProtocol finalRepProt = null;
            boolean found = false;
            double maxThroughput = 0;

            int numNodes=imin;


            while( numNodes<=imax ){
                int repDegree=imin;
                while( repDegree<=numNodes ){
                    for(ReplicationProtocol protocol : ReplicationProtocol.values()){

                        Map<ForecastParam, Object> forecastParams = new HashMap<ForecastParam, Object>();
                        forecastParams.put(ForecastParam.ReplicationProtocol, protocol);
                        forecastParams.put(ForecastParam.ReplicationDegree, repDegree);
                        forecastParams.put(ForecastParam.NumNodes, numNodes);


                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Param.PercentageWriteTransactions.getKey(), 1000);

                        WPMSample wpmSample = new WPMSample(0, map);
                        ProcessedSample twopcprocessed = new TWOPCProcessedSample(wpmSample);


                        InputOracleWPM inputOracle = new InputOracleWPM(twopcprocessed, forecastParams);

//                        System.out.println("Forecasting with: " +
//                                "nodes " + numNodes + ", " +
//                                "repDegree " + repDegree + ", " +
//                                "repProt " + protocol
//                        );
                        OutputOracle outputOracle = fake.forecast( inputOracle );


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

            System.out.println("CONFIGURAZIONE DA ATTUARE: " + configuration);

        }

    }
}
