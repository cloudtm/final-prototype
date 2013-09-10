package eu.cloudtm.autonomicManager.simulator;

import eu.cloudtm.autonomicManager.oracles.InputOracle;

/**
 * @author Sebastiano Peluso
 */
public class SimulatorConfNetwork {

   private Boolean netVerbose = false;

   SimulatorConfNetwork(InputOracle inputOracle){

   }

   @Override
   public String toString(){
      return "[Network]\n\n"+
             "net_verbose = "+netVerbose;
   }

}
