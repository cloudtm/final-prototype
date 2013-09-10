package morphr;
/*
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

import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import utils.PropertyReader;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt
 *         Date: 27/08/13
 */
public class OutputOracleMorphR implements OutputOracle {

   //TODO clean up
   private static String modelFilenameAbort2PC = PropertyReader.getString("modelFilenameAbort2PC", "/config/MorphR/MorphR.properties");
   private static String modelFilenameThroughput2PC = PropertyReader.getString("modelFilenameThroughput2PC", "/config/MorphR/MorphR.properties");
   private static String modelFilenameReadOnly2PC = PropertyReader.getString("modelFilenameReadOnly2PC", "/config/MorphR/MorphR.properties");
   private static String modelFilenameWrite2PC = PropertyReader.getString("modelFilenameWrite2PC", "/config/MorphR/MorphR.properties");
   private static String modelFilenameAbortPB = PropertyReader.getString("modelFilenameAbortPB", "/config/MorphR/MorphR.properties");
   private static String modelFilenameThroughputPB = PropertyReader.getString("modelFilenameThroughputPB", "/config/MorphR/MorphR.properties");
   private static String modelFilenameReadOnlyPB = PropertyReader.getString("modelFilenameReadOnlyPB", "/config/MorphR/MorphR.properties");
   private static String modelFilenameWritePB = PropertyReader.getString("modelFilenameWritePB", "/config/MorphR/MorphR.properties");
   private static String modelFilenameAbortTO = PropertyReader.getString("modelFilenameAbortTO", "/config/MorphR/MorphR.properties");
   private static String modelFilenameThroughputTO = PropertyReader.getString("modelFilenameThroughputTO", "/config/MorphR/MorphR.properties");
   private static String modelFilenameReadOnlyTO = PropertyReader.getString("modelFilenameReadOnlyTO", "/config/MorphR/MorphR.properties");
   private static String modelFilenameWriteTO = PropertyReader.getString("modelFilenameWriteTO", "/config/MorphR/MorphR.properties");
   private MorphR morphr;
   private ReplicationProtocol replicationProtocol;
   private String query;
   private InputOracle input;

   public OutputOracleMorphR(MorphR m, String q, InputOracle inputO) {
      morphr = m;
      replicationProtocol = extractReplicationProtocol(input);
      query = q;
      input = inputO;
   }

   @Override
   public double responseTime(int transactionalClass) {
      if (transactionalClass == 0) { 
         if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
            morphr.initiateCubist(modelFilenameReadOnly2PC);
         else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
            morphr.initiateCubist(modelFilenameReadOnlyPB);
         else
            morphr.initiateCubist(modelFilenameReadOnlyTO);
      } else { 
         if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
            morphr.initiateCubist(modelFilenameWrite2PC);
         else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
            morphr.initiateCubist(modelFilenameWritePB);
         else
            morphr.initiateCubist(modelFilenameWriteTO);
      }
      return morphr.getPrediction(query);
   }

   @Override
   public double throughput(int i) {
      double txPercentage = txClassPercentage(i);
      if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
         morphr.initiateCubist(modelFilenameThroughput2PC);
      else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
         morphr.initiateCubist(modelFilenameThroughputPB);
      else
         morphr.initiateCubist(modelFilenameThroughputTO);
      //This works only if retry-on-abort is enabled
      return morphr.getPrediction(query) * txPercentage;
   }

   @Override
   public double abortRate(int i) {
      if (i == 0) {
         return 0.0;
      }
      if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
         morphr.initiateCubist(modelFilenameAbort2PC);
      else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
         morphr.initiateCubist(modelFilenameAbortPB);
      else
         morphr.initiateCubist(modelFilenameAbortTO);
      return morphr.getPrediction(query);
   }

   @Override
   public double getConfidenceThroughput(int i) {
	      double txPercentage = txClassPercentage(i);
	      if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
	         morphr.initiateCubist(modelFilenameThroughput2PC);
	      else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
	         morphr.initiateCubist(modelFilenameThroughputPB);
	      else
	         morphr.initiateCubist(modelFilenameThroughputTO);
	      //This works only if retry-on-abort is enabled
	      return morphr.getPredictionAndError(query)[1] * txPercentage;
   }

   @Override
   public double getConfidenceAbortRate(int i) {
	      if (i == 0) {
	          return 0.0;
	       }
	       if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
	          morphr.initiateCubist(modelFilenameAbort2PC);
	       else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
	          morphr.initiateCubist(modelFilenameAbortPB);
	       else
	          morphr.initiateCubist(modelFilenameAbortTO);
	       return morphr.getPredictionAndError(query)[1];
   }

   @Override
   public double getConfidenceResponseTime(int transactionalClass) {
	      if (transactionalClass == 0) { 
	          if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
	             morphr.initiateCubist(modelFilenameReadOnly2PC);
	          else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
	             morphr.initiateCubist(modelFilenameReadOnlyPB);
	          else
	             morphr.initiateCubist(modelFilenameReadOnlyTO);
	       } else { 
	          if (replicationProtocol.compareTo(ReplicationProtocol.TWOPC) == 0)
	             morphr.initiateCubist(modelFilenameWrite2PC);
	          else if (replicationProtocol.compareTo(ReplicationProtocol.PB) == 0)
	             morphr.initiateCubist(modelFilenameWritePB);
	          else
	             morphr.initiateCubist(modelFilenameWriteTO);
	       }
	       return morphr.getPredictionAndError(query)[1];
   }

   private ReplicationProtocol extractReplicationProtocol(InputOracle input) {
      return ((ReplicationProtocol) input.getForecastParam(ForecastParam.ReplicationProtocol));
   }

   private double txClassPercentage(int clazz) {
      if (clazz == 0)
         return (1 - (Double) input.getParam(Param.PercentageWriteTransactions));
      if (clazz == 1)
         return (Double) input.getParam(Param.PercentageWriteTransactions);
      throw new IllegalArgumentException("Percentage for transactional class with id " + clazz + " is not available");
   }
}
