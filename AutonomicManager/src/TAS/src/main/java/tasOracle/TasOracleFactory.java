package tasOracle;


import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * // TODO: Document this
 *
 * @author diego
 * @since 4.0
 */
public class TasOracleFactory {

   private final static Log log = LogFactory.getLog(TasOracleFactory.class);

   public TasOracle_I buildTasOracle(InputOracle input) {
      final boolean t = log.isTraceEnabled();
      switch (rp(input)) {
         case TWOPC: {
            if (t) log.trace("Going to solve for TPC");
            return new TasOracle_GMU_TPC_QUEUE_CPU_CUBIST_NET_MULE();
         }
         case PB: {
            if (t) log.trace("Going to solve for PB");
            return new TasOracle_GMU_PB_QUEUE_CPU_CUBIST_NET_MULE();
         }
         case TO: {
            if (t) log.trace("Going to solve for TO");
            return new TasOracle_GMU_TO_QUEUE_CPU_CUBIST_NET_MULE();
         }
         default:
            throw new IllegalArgumentException("Unknown replication protocol");
      }

   }

   private ReplicationProtocol rp(InputOracle input) {
      return (ReplicationProtocol) input.getForecastParam(ForecastParam.ReplicationProtocol);
   }


}
