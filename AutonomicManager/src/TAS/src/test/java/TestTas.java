import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import org.apache.log4j.PropertyConfigurator;
import tasOracle.TasOracle;

import java.io.File;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt
 *         Date: 27/08/13
 */
public class TestTas {

   // private static final String testPath = "/Users/diego/Software/Validation/data/130724";
   private static final String testPath = "/Users/diego/Software/Validation/data/130724TO";
   //private static final String testPath = "/Users/diego/Software/Validation/data/130804PB";

   public static void main(String[] args) {
      if (true)
         throw new RuntimeException("Only 4 Diego :P");
      PropertyConfigurator.configure("conf/log4j.properties");
      File dir = new File(testPath);
      for (File dir_2 : dir.listFiles()) {
         if (dir_2.isDirectory()) {
            for (File csv : dir_2.listFiles()) {
               if (!csv(csv)) {
                  continue;
               }
               CsvInputOracle csvI = new CsvInputOracle(csv);
               TasOracle t = new TasOracle();
               try {
                  OutputOracle out = t.forecast(csvI);
                  System.out.println(csv.getPath() + "readR " + out.responseTime(0) + " writeR " + out.responseTime(1) + " readT " + out.throughput(0) + " writeT " + out.throughput(1) + " readA " + out.abortRate(0) + " writeA " + out.abortRate(1));
               } catch (OracleException e) {
                  System.out.println(csv.getPath() + " KO " + e.getMessage());
               }
            }
         }
      }
   }

   private static boolean csv(File f) {
      return f.toString().endsWith("csv");
   }
}
