package eu.cloudtm.autonomicManager.simulator;

import eu.cloudtm.autonomicManager.oracles.InputOracle;

/**
 * @author Sebastiano Peluso
 */
class SimulatorConf {

   private SimulatorConfGlobal simulatorConfGlobal;
   private SimulatorConfClient simulatorConfClient;
   private SimulatorConfServer simulatorConfServer;
   private SimulatorConfNetwork simulatorConfNetwork;

   SimulatorConf(InputOracle inputOracle){

      this.simulatorConfGlobal = new SimulatorConfGlobal(inputOracle);
      this.simulatorConfClient = new SimulatorConfClient(inputOracle);
      this.simulatorConfServer = new SimulatorConfServer(inputOracle);
      this.simulatorConfNetwork = new SimulatorConfNetwork(inputOracle);


   }

   int getNumberOfClients(){
      return this.simulatorConfGlobal.getNumberOfClients();
   }


   @Override
   public String toString(){

      return this.simulatorConfGlobal+"\n"+
            "#-------------------#\n"+
            this.simulatorConfClient+"\n"+
            "#-------------------#\n"+
            this.simulatorConfServer+"\n"+
            "#-------------------#\n"+
            this.simulatorConfNetwork;
   }

}
