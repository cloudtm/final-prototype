package tasOracle;

import common.exceptions.TasException;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import ispn_53.gmu.tpc.core.Tas_QCCN_GMU_TPC;
import ispn_53.gmu.tpc.result.ISPN_53_D_TPC_GMU_Result;
import ispn_53.input.ISPN_52_TPC_GMU_Workload;
import ispn_53.input.physical.GmuCpuServiceTimesImpl;
import ispn_53.input.physical.ServiceTimes_CpuNet_QueueCubist_GMU_TPC;
import ispn_53.input.physical.ServiceTimes_Net_Cubist_GMU_TPC;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * // TODO: Document this
 *
 * @author diego
 * @since 4.0
 */
public class TasOracle_GMU_TPC_QUEUE_CPU_CUBIST_NET_MULE extends CommonTasOracle {

   private static final Log log = LogFactory.getLog(TasOracle_GMU_TPC_QUEUE_CPU_CUBIST_NET_MULE.class);

   @Override
   public OutputOracle forecast(InputOracle input) throws OracleException {
      ServiceTimes_CpuNet_QueueCubist_GMU_TPC serviceTimes = buildServiceTimes(input);
      Tas_QCCN_GMU_TPC tas = new Tas_QCCN_GMU_TPC();
      ISPN_52_TPC_GMU_Workload workload = buildWorkload(input);
      ISPN_53_D_TPC_GMU_Result result;
      try {
         result = tas.solve(workload, serviceTimes);
         return new TasOutputOracle(result);
      } catch (TasException e) {
         throw new OracleException(e.getMessage());
      }
   }

   private GmuCpuServiceTimesImpl buildCpuS(InputOracle input) {
      double updateTxBusinessLogicS = getDoubleParam(input, Param.LocalUpdateTxLocalServiceTime);
      double updateTxPrepareS = getDoubleParam(input, Param.LocalUpdateTxPrepareServiceTime);
      double updateTxCommitS = getDoubleParam(input, Param.LocalUpdateTxCommitServiceTime);
      double updateTxLocalLocalRollbackS = getDoubleParam(input, Param.LocalUpdateTxLocalRollbackServiceTime);
      double updateTxLocalRemoteRollbackS = getDoubleParam(input, Param.LocalUpdateTxRemoteRollbackServiceTime);

      double localRemoteGetS = getDoubleParam(input, Param.RemoteGetServiceTime);
      double localGetS = 0;
      double remoteRemoteGetS = getDoubleParam(input, Param.GMUClusteredGetCommandServiceTime);
      double putS = 0;

      double updateTxRemoteExecutionS = getDoubleParam(input, Param.RemoteUpdateTxPrepareServiceTime);
      double updateTxRemoteCommitS = getDoubleParam(input, Param.RemoteUpdateTxCommitServiceTime);
      double updateTxRemoteRollbackS = getDoubleParam(input, Param.RemoteUpdateTxRollbackServiceTime);

      double readOnlyTxBusinessLogicS = getDoubleParam(input, Param.ReadOnlyTxTotalCpuTime);
      double readOnlyTxPrepareS = 0;
      double readOnlyTxCommitS = 0;
      GmuCpuServiceTimesImpl cpu = new GmuCpuServiceTimesImpl();

      cpu.setUpdateTxBusinessLogicS(updateTxBusinessLogicS);
      cpu.setUpdateTxPrepareS(updateTxPrepareS);
      cpu.setUpdateTxCommitS(updateTxCommitS);
      cpu.setUpdateTxLocalLocalRollbackS(updateTxLocalLocalRollbackS);
      cpu.setUpdateTxLocalRemoteRollbackS(updateTxLocalRemoteRollbackS);
      cpu.setLocalRemoteGetS(localRemoteGetS);
      cpu.setLocalGetS(localGetS);
      cpu.setRemoteRemoteGetS(remoteRemoteGetS);
      cpu.setPutS(putS);
      cpu.setUpdateTxRemoteExecutionS(updateTxRemoteExecutionS);
      cpu.setUpdateTxRemoteCommitS(updateTxRemoteCommitS);
      cpu.setUpdateTxRemoteRollbackS(updateTxRemoteRollbackS);
      cpu.setReadOnlyTxBusinessLogicS(readOnlyTxBusinessLogicS);
      cpu.setReadOnlyTxPrepareS(readOnlyTxPrepareS);
      cpu.setReadOnlyTxCommitS(readOnlyTxCommitS);
      if (log.isTraceEnabled())
         log.trace(cpu.toString());
      return cpu;
   }

   private ServiceTimes_CpuNet_QueueCubist_GMU_TPC buildServiceTimes(InputOracle input) {
      GmuCpuServiceTimesImpl cpuS = buildCpuS(input);
      ServiceTimes_Net_Cubist_GMU_TPC netS = buildNetS(input);
      return new ServiceTimes_CpuNet_QueueCubist_GMU_TPC(cpuS, netS);
   }

   private ServiceTimes_Net_Cubist_GMU_TPC buildNetS(InputOracle input) {
      return new ServiceTimes_Net_Cubist_GMU_TPC();
   }

}
