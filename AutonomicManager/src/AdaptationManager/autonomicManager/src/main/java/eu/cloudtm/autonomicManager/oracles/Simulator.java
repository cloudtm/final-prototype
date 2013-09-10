package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

import java.util.Random;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/24/13
 */
public class Simulator implements Oracle {

    private static final String directory = "/home/fabio/Desktop/simulatore/DAGSwithCubist/Debug";
    private static final String script = "/home/fabio/Desktop/simulatore/DAGSwithCubist/Debug/DAGSwithCubist";

    Process p = null;

    public Simulator(){}

    @Override
    public OutputOracle forecast(InputOracle input) throws OracleException {

//        ProcessBuilder pb = new ProcessBuilder(script, "");
//        pb.directory(new File(directory));
//
//        try {
//            p = pb.start();
//            InputStream shellIn = p.getInputStream(); // this captures the output from the command
//            int shellExitStatus = p.waitFor(); // wait for the shell to finish and get the return code
//            int c;
//            while ((c = shellIn.read()) != -1) {
//                System.out.write(c);
//            }
//            shellIn.close();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        Random rnd = new Random();

        return new OutputOracleImpl(rnd.nextInt(3000), rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble());

    }
}
