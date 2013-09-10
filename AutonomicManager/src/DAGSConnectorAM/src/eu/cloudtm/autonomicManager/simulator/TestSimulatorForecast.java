package eu.cloudtm.autonomicManager.simulator;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sebastiano Peluso
 */
public final class TestSimulatorForecast {

    private TestSimulatorForecast(){

    }

   public static void main(String[] args){

      try {
         SimulatorOracle simulatorOracle = new SimulatorOracle();

         simulatorOracle.forecast(new TestInputOracle());


      } catch (OracleException e) {
         e.printStackTrace();
      }

   }




}

class TestInputOracle implements InputOracle{

      private ConcurrentHashMap<Param, Object> params;
      private ConcurrentHashMap<EvaluatedParam, Object> evaluatedParams;
      private ConcurrentHashMap<ForecastParam, Object> forecastParams;

      public TestInputOracle(){
         params = new ConcurrentHashMap<Param, Object>();
         evaluatedParams = new ConcurrentHashMap<EvaluatedParam, Object>();
         forecastParams = new ConcurrentHashMap<ForecastParam, Object>();


         params.put(Param.NumberOfEntries, 265000);

         forecastParams.put(ForecastParam.NumNodes, 3);
         evaluatedParams.put(EvaluatedParam.MAX_ACTIVE_THREADS, 6);
         forecastParams.put(ForecastParam.ReplicationDegree, 2);

         params.put(Param.AvgPutsPerWrTransaction, 14D);
         params.put(Param.AvgGetsPerWrTransaction, 86L);

         params.put(Param.AvgGetsPerROTransaction, 500L);

         params.put(Param.PercentageWriteTransactions, 0.5D);

         params.put(Param.AvgTxArrivalRate, 0.1D);
         params.put(Param.AvgNTCBTime, 40000L);



         evaluatedParams.put(EvaluatedParam.CORE_PER_CPU, 2);

         params.put(Param.AvgLocalGetTime, 10L);
         params.put(Param.AverageWriteTime, 10L);

         params.put(Param.LocalUpdateTxLocalRollbackServiceTime, 236L);

         params.put(Param.LocalUpdateTxPrepareServiceTime, 449L);

         params.put(Param.LocalUpdateTxCommitServiceTime, 236L);

         params.put(Param.LocalUpdateTxPrepareResponseTime, 0L);

         params.put(Param.LocalUpdateTxLocalResponseTime, 3L);



      }

      public Object getParam(Param param) {
         return params.get(param);
      }

      public Object getEvaluatedParam(EvaluatedParam param) {
         return evaluatedParams.get(param);
      }

      public Object getForecastParam(ForecastParam param) {
         return forecastParams.get(param);
      }
}
