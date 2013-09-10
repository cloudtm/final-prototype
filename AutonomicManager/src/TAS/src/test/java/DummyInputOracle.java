import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;

import java.util.HashMap;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt
 *         Date: 27/08/13
 */
public abstract class DummyInputOracle implements TestInputOracle {

   private HashMap<ForecastParam, Object> fMap = new HashMap<ForecastParam, Object>();
   private HashMap<EvaluatedParam, Object> eMap = new HashMap<EvaluatedParam, Object>();
   private HashMap<Param, Object> pMap = new HashMap<Param, Object>();

   @Override
   public void setParam(Param param, Object value) {
      this.pMap.put(param, value);
   }

   @Override
   public void setEvaluatedParam(EvaluatedParam param, Object value) {
      this.eMap.put(param, value);
   }

   @Override
   public void setForecastParam(ForecastParam param, Object value) {
      this.fMap.put(param, value);
   }

   @Override
   public Object getParam(Param param) {
      return pMap.get(param);
   }

   @Override
   public Object getEvaluatedParam(EvaluatedParam evaluatedParam) {
      return eMap.get(evaluatedParam);
   }

   @Override
   public Object getForecastParam(ForecastParam forecastParam) {
      return fMap.get(forecastParam);
   }
}
