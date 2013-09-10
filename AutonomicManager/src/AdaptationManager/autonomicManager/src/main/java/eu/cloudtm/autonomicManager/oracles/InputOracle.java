package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/10/13
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InputOracle {

    public Object getParam(Param param);

    public Object getEvaluatedParam(EvaluatedParam param);

    public Object getForecastParam(ForecastParam param);

}
