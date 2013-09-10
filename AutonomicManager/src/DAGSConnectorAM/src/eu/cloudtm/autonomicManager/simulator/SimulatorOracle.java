package eu.cloudtm.autonomicManager.simulator;

import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Sebastiano Peluso
 *
 */
public class SimulatorOracle implements Oracle {

   private Properties props = null;

   private SimulatorConf simulatorConf = null;

   public SimulatorOracle() throws OracleException {

      try {
         FileReader confOracle = new FileReader("conf/simulator/simulatorOracle.properties");

         props = new Properties();

         props.load(confOracle);


      } catch (FileNotFoundException e1) {
         throw new OracleException(e1);
      } catch (IOException e2) {
         throw new OracleException(e2);
      }

   }

   public OutputOracle forecast(InputOracle input) throws OracleException {

      String dir = props.getProperty("directory");
      String exec = props.getProperty("exec");

      SimulatorConf simulatorConf = new SimulatorConf(input);


      File fileSimulatorConfDir = new File(dir);
      if(fileSimulatorConfDir.exists()){

         File fileSimulatorConf = new File(fileSimulatorConfDir, "simulation.conf");
         FileWriter fw = null;
         PrintWriter out = null;
         try {
            fw = new FileWriter(fileSimulatorConf);
            out = new PrintWriter(fw);

            out.print(simulatorConf);
            out.flush();


            ProcessBuilder pb = new ProcessBuilder(exec);
            pb.directory(fileSimulatorConfDir);
            Process p;

            p = pb.start();
            InputStream shellIn = p.getInputStream(); // this captures the output from the command
            int shellExitStatus = p.waitFor(); // wait for the shell to finish and get the return code
            int c;
            BufferedReader br = new BufferedReader(new InputStreamReader(shellIn));
            String line = null;
            SimulatorOutputOracle outputOracle = new SimulatorOutputOracle();
            /*
            while ((c = shellIn.read()) != -1) {
               System.out.write(c);



            }*/

            while((line = br.readLine()) != null){
               //System.out.println(line);
               if(line.charAt(0) == '$'){
                   populateOutput(line, outputOracle, simulatorConf.getNumberOfClients());

               }

            }

            for(int i = 0; ; i++){
               if(!outputOracle.exists(i)){
                  break;
               }
               System.out.println("Class "+i);
               System.out.println("Throughput: "+outputOracle.throughput(i));
               System.out.println("ResponseTime: "+outputOracle.responseTime(i));
               System.out.println("Abort Rate: "+outputOracle.abortRate(i));
            }

            shellIn.close();

            return outputOracle;

         }catch (IOException e1) {
            throw new OracleException(e1);
         }
         catch (InterruptedException e2) {
            throw new OracleException(e2);
         }
         finally {
            if(out != null){
               out.close();
            }
            if(fw != null){
               try {
                  fw.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }






      }
      else{
         throw new OracleException(dir+"does not exist");
      }




   }

   private static void populateOutput(String line, SimulatorOutputOracle outputOracle, int numberOfClients){

      StringTokenizer strTok = null;
      String token;

      strTok = new StringTokenizer(line, ";");
      int offset = 0;
      int classId = 0;
      double responseTime = 0.0D;
      double throughput = 0.0D;
      double abortRate = 0.0D;
      while(strTok.hasMoreTokens()){
         token = strTok.nextToken();

         if(offset == 1){
             classId = Integer.parseInt(token);
         }
         else if(offset == 5){
             responseTime = Double.parseDouble(token);
         }
         else if(offset == 6){
             throughput = Double.parseDouble(token) * 1000000.0D;
         }
         else if(offset == 7){
             abortRate = Double.parseDouble(token);
         }

         offset++;
      }

      throughput *= numberOfClients;

      outputOracle.addThroughput(classId, throughput);
      outputOracle.addAbortRate(classId, abortRate);
      outputOracle.addResponseTime(classId, responseTime);

   }
}
