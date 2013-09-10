/*
 * INESC-ID, Instituto de Engenharia de Sistemas e Computadores Investiga��o e Desevolvimento em Lisboa
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

/**
 * @author Maria Couceiro <mcouceiro@gsd.inesc-id.pt>
 * @version 1.0
 * @since 2013-07-24
 */


package morphr;


import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import utils.PropertyReader;


public class MorphR implements Oracle {

   private static String cubistLibraryFilename = PropertyReader.getString("cubistLibraryFilename", "/config/MorphR/MorphR.properties");

   public native void initiateCubist(String filename);

   public native double getPrediction(String query);

   public native double[] getPredictionAndError(String query);

   static {
      System.loadLibrary(cubistLibraryFilename);
   }

   @Override
   public OutputOracle forecast(InputOracle input) throws OracleException {

      final String query = buildQueryString(input);

      return new OutputOracleMorphR(this, query, input);

   }

   private String buildQueryString(InputOracle input) {
      return new String(
              (Integer) input.getParam(Param.MemoryInfo_used) + "," +
                      (Integer) input.getParam(Param.AvgGetsPerROTransaction) + "," +
                      (Integer) input.getParam(Param.AvgGetsPerWrTransaction) + "," +
                      (Integer) input.getParam(Param.AvgPutsPerWrTransaction) + "," +
                      (Integer) input.getParam(Param.LocalUpdateTxLocalServiceTime) + "," +
                      (Integer) input.getParam(Param.AvgClusteredGetCommandReplySize) + "," +
                      (Integer) input.getParam(Param.AvgPrepareCommandSize) + "," +
                      (Integer) input.getParam(Param.PercentageWriteTransactions) + "," +
                      (Integer) input.getForecastParam(ForecastParam.NumNodes) + "," +
                      (Integer) input.getForecastParam(ForecastParam.ReplicationDegree) + ",?");
   }


}


