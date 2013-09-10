import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.InputOracleWPM;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.TWOPCProcessedSample;
import eu.cloudtm.autonomicManager.statistics.WPMSample;
import morphr.MorphR;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt
 *         Date: 27/08/13
 */
public class MariaTest {

   public static void main(String[] args) throws OracleException {

      Map<ForecastParam, Object> forecastParams = new HashMap<ForecastParam, Object>();
      forecastParams.put(ForecastParam.ReplicationProtocol, ReplicationProtocol.TWOPC);
      forecastParams.put(ForecastParam.ReplicationDegree, 2);
      forecastParams.put(ForecastParam.NumNodes, 10);

      Map<String, Object> map = new HashMap<String, Object>();
      map.put(Param.PercentageWriteTransactions.getKey(), 10);

      map.put(Param.MemoryInfo_used.getKey(), 10);
      map.put(Param.AvgGetsPerROTransaction.getKey(), 10);
      map.put(Param.AvgGetsPerWrTransaction.getKey(), 10);
      map.put(Param.AvgPutsPerWrTransaction.getKey(), 10);
      map.put(Param.LocalUpdateTxLocalServiceTime.getKey(), 10);
      map.put(Param.ReplicationDegree.getKey(), 10);
      map.put(Param.AvgClusteredGetCommandReplySize.getKey(), 10);
      map.put(Param.AvgPrepareCommandSize.getKey(), 10);
      map.put(Param.PercentageWriteTransactions.getKey(), 10);


      WPMSample wpmSample = new WPMSample(0, map);
      ProcessedSample twopcprocessed = new TWOPCProcessedSample(wpmSample);

      InputOracle inputOracle = new InputOracleWPM(twopcprocessed, forecastParams);

      MorphR morphr = new MorphR();

      OutputOracle o = morphr.forecast(inputOracle);

      System.out.println(o.abortRate(1));
   }
}
