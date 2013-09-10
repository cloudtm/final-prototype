import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt
 *         Date: 27/08/13
 */
public interface TestInputOracle extends InputOracle {

   public void setParam(Param param, Object value);

   public void setEvaluatedParam(EvaluatedParam param, Object value);

   public void setForecastParam(ForecastParam param, Object value);
}
