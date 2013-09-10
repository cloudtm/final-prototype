package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.Sample;
import eu.cloudtm.autonomicManager.statistics.WPMSample;

import java.util.Map;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 7/10/13
 */
public class InputOracleWPM implements InputOracle {

   private ProcessedSample processedSample;
   private Map<ForecastParam, Object> forecastParam;

   public InputOracleWPM(ProcessedSample processedSample, Map<ForecastParam, Object> forecastParam) {
      this.processedSample = processedSample;
      this.forecastParam = forecastParam;
   }

   @Override
   public Object getParam(Param param) {
      return processedSample.getParam(param);
   }

   @Override
   public Object getForecastParam(ForecastParam param) {
      return forecastParam.get(param);
   }

   @Override
   public Object getEvaluatedParam(EvaluatedParam param) {
      return processedSample.getEvaluatedParam(param);
   }

   public Map<ForecastParam, Object> getForecastParamMap() {
      return forecastParam;
   }

   public Map<EvaluatedParam, Object> getEvaluatedParamMap() {
      return processedSample.getEvaluatedParams();
   }

   public Map<String, Object> getParamMap() {
      Sample s = processedSample.getInnerSample();
      return s.getParams();
   }


}
