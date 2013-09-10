package eu.cloudtm.autonomicManager.simulator;

import eu.cloudtm.autonomicManager.oracles.OutputOracle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class SimulatorOutputOracle implements OutputOracle {

   private ConcurrentHashMap<Integer, Double> throughput;
   private ConcurrentHashMap<Integer, Double> abortRate;
   private ConcurrentHashMap<Integer, Double> responseTime;

   public SimulatorOutputOracle(){
      throughput = new ConcurrentHashMap<Integer, Double>();
      abortRate = new ConcurrentHashMap<Integer, Double>();
      responseTime = new ConcurrentHashMap<Integer, Double>();
   }

   public void addThroughput(int txClassId, double value){
      throughput.put(txClassId, value);
   }

   public void addAbortRate(int txClassId, double value){
      abortRate.put(txClassId, value);
   }

   public void addResponseTime(int txClassId, double value){
      responseTime.put(txClassId, value);
   }

   public double throughput(int txClassId) {
      Double value = throughput.get(txClassId);

      if(value != null){
         return value;
      }

      return 0;
   }

   public double abortRate(int txClassId) {
      Double value = abortRate.get(txClassId);

      if(value != null){
         return value;
      }

      return 0;
   }

   public double responseTime(int txClassId) {
      Double value = responseTime.get(txClassId);

      if(value != null){
         return value;
      }

      return 0;
   }

   public boolean exists(int txClassId){
      Double value1 = throughput.get(txClassId);
      Double value2 = abortRate.get(txClassId);
      Double value3 = responseTime.get(txClassId);

      return value1 != null && value2 != null && value3 != null;

   }

   public double getConfidenceThroughput(int txClassId) {
      return 0;
   }

   public double getConfidenceAbortRate(int txClassId) {
      return 0;
   }

   public double getConfidenceResponseTime(int txClassId) {
      return 0;
   }
}
