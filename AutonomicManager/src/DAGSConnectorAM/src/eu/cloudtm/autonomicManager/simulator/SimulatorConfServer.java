package eu.cloudtm.autonomicManager.simulator;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;

/**
 * @author Sebastiano Peluso
 */
public class SimulatorConfServer {

   private String concurrencyControlType = "GMU";
   private Long lockingTimeout = 0L;

   //private Integer maxServentsPerCpu = 2;
   private Integer maxServentsPerCpu;

   private Boolean deadlockDetectionEnabled = false;

   //private Long localTxGetCpuServiceDemand = 10L;
   private Long localTxGetCpuServiceDemand;

   //private Long localTxPutCpuServiceDemand = 10L;
   private Long localTxPutCpuServiceDemand;

   //private Long localTxGetFromRemoteCpuServiceDemand = 10L;
   private Long localTxGetFromRemoteCpuServiceDemand;

   //private Long txSendRemoteTxGetCpuServiceDemand = 10L;
   private Long txSendRemoteTxGetCpuServiceDemand;

   private Long remoteTxPutCpuServiceDemand = 0L;

   private Long txBeginCpuServiceDemand = 0L;

   //private Long txAbortCpuServiceDemand = 236L;
   private Long txAbortCpuServiceDemand;

   private Long remoteTxGetReturnCpuServiceDemand = 0L;
   private Long updateCpuServiceDemand = 0L;

   //private Long localPrepareSuccessedCpuServiceDemand = 449L;
   private Long localPrepareSuccessedCpuServiceDemand;

   //private Long localPrepareFailedCpuServiceDemand = 449L;
   private Long localPrepareFailedCpuServiceDemand;

   //private Long localTxFinalCommitCpuServiceDemand = 236L;
   private Long localTxFinalCommitCpuServiceDemand;

   //private Long txPrepareCpuServiceDemand = 0L;
   private Long txPrepareCpuServiceDemand;

   //private Long txPrepareFailedCpuServiceDemand = 0L;
   private Long txPrepareFailedCpuServiceDemand;

   //private Long distributedFinalTxCommitCpuServiceDemand = 3L;
   private Long distributedFinalTxCommitCpuServiceDemand;

   private Boolean ccPrintStat = false;
   private Boolean printMaxBlockedTransactions = false;
   private Boolean serverVerbose = false;
   private Boolean ccVerbose = false;


   SimulatorConfServer(InputOracle inputOracle){


      maxServentsPerCpu = (Integer) inputOracle.getEvaluatedParam(EvaluatedParam.CORE_PER_CPU);

      localTxGetCpuServiceDemand = (Long) inputOracle.getParam(Param.AvgLocalGetTime);

      localTxPutCpuServiceDemand = (Long) inputOracle.getParam(Param.AverageWriteTime);

      localTxGetFromRemoteCpuServiceDemand = (Long) inputOracle.getParam(Param.AvgLocalGetTime);

      txSendRemoteTxGetCpuServiceDemand = (Long) inputOracle.getParam(Param.AvgLocalGetTime);

      txAbortCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxLocalRollbackServiceTime);

      localPrepareSuccessedCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxPrepareServiceTime);
      localPrepareFailedCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxPrepareServiceTime);

      localTxFinalCommitCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxCommitServiceTime);

      txPrepareCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxPrepareResponseTime);

      txPrepareFailedCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxPrepareResponseTime);

      distributedFinalTxCommitCpuServiceDemand = (Long) inputOracle.getParam(Param.LocalUpdateTxLocalResponseTime);
   }

   @Override
   public String toString() {

      return "[Server]\n\n"+
             "concurrency_control_type = "+concurrencyControlType+"\n"+
             "locking_timeout = "+lockingTimeout+"\n"+
             "max_servents_per_cpu = "+maxServentsPerCpu+"\n"+
             "deadlock_detection_enabled = "+deadlockDetectionEnabled+"\n"+
             "local_tx_get_cpu_service_demand = "+localTxGetCpuServiceDemand+"\n"+
             "local_tx_put_cpu_service_demand = "+localTxPutCpuServiceDemand+"\n"+
             "local_tx_get_from_remote_cpu_service_demand = "+localTxGetFromRemoteCpuServiceDemand+"\n"+
             "tx_send_remote_tx_get_cpu_service_demand = "+txSendRemoteTxGetCpuServiceDemand+"\n"+
             "remote_tx_put_cpu_service_demand = "+remoteTxPutCpuServiceDemand+"\n"+
             "tx_begin_cpu_service_demand = "+txBeginCpuServiceDemand+"\n"+
             "tx_abort_cpu_service_demand = "+txAbortCpuServiceDemand+"\n"+
             "remote_tx_get_return_cpu_service_demand = "+remoteTxGetReturnCpuServiceDemand+"\n"+
             "update_cpu_service_demand = "+updateCpuServiceDemand+"\n"+
             "local_prepare_successed_cpu_service_demand = "+localPrepareSuccessedCpuServiceDemand+"\n"+
             "local_prepare_failed_cpu_service_demand = "+localPrepareFailedCpuServiceDemand+"\n"+
             "local_tx_final_commit_cpu_service_demand = "+localTxFinalCommitCpuServiceDemand+"\n"+
             "tx_prepare_cpu_service_demand = "+txPrepareCpuServiceDemand+"\n"+
             "tx_prepare_failed_cpu_service_demand = "+txPrepareFailedCpuServiceDemand+"\n"+
             "distributed_final_tx_commit_cpu_service_demand = "+distributedFinalTxCommitCpuServiceDemand+"\n"+
             "cc_print_stat = "+ccPrintStat+"\n"+
             "print_max_blocked_transactions = "+printMaxBlockedTransactions+"\n"+
             "server_verbose = "+serverVerbose+"\n"+
             "cc_verbose = "+ccVerbose;
   }


}
