package tasOracle;/*
 * INESC-ID, Instituto de Engenharia de Sistemas e Computadores Investigação e Desevolvimento em Lisboa
 * Copyright 2013 INESC-ID and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3.0 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import common.exceptions.TasException;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import ispn_53.gmu.pb.core.Tas_QCCN_GMU_PB;
import ispn_53.gmu.pb.input.cpu.ServiceTimes_Cpu_GMU_PB;
import ispn_53.gmu.pb.input.cpuNet.ServiceTimes_CpuNet_QueueCubist_GMU_PB;
import ispn_53.gmu.pb.input.net.ServiceTimes_Net_Cubist_GMU_PB;
import ispn_53.gmu.pb.result.Result_53_PB_GMU;
import ispn_53.input.ISPN_52_TPC_GMU_Workload;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt
 *         Date: 25/08/13
 */
public class TasOracle_GMU_PB_QUEUE_CPU_CUBIST_NET_MULE extends CommonTasOracle {

   @Override
   public OutputOracle forecast(InputOracle input) throws OracleException {
      ServiceTimes_CpuNet_QueueCubist_GMU_PB serviceTimes = buildServiceTimes(input);
      Tas_QCCN_GMU_PB tas = new Tas_QCCN_GMU_PB();
      ISPN_52_TPC_GMU_Workload workload = buildWorkload(input);
      Result_53_PB_GMU result;
      try {
         result = tas.solve(workload, serviceTimes);
         return new TasOutputOracle(result);
      } catch (TasException e) {
         throw new OracleException(e.getMessage());
      }
   }

   private ServiceTimes_Cpu_GMU_PB buildCpuS(InputOracle input) {
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
      ServiceTimes_Cpu_GMU_PB cpu = new ServiceTimes_Cpu_GMU_PB();

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

   private ServiceTimes_CpuNet_QueueCubist_GMU_PB buildServiceTimes(InputOracle input) {
      ServiceTimes_Cpu_GMU_PB cpuS = buildCpuS(input);
      ServiceTimes_Net_Cubist_GMU_PB netS = buildNetS(input);
      return new ServiceTimes_CpuNet_QueueCubist_GMU_PB(cpuS, netS);
   }

   private ServiceTimes_Net_Cubist_GMU_PB buildNetS(InputOracle input) {
      return new ServiceTimes_Net_Cubist_GMU_PB();
   }

}
