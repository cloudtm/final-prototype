package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Param;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/8/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomSample extends ProcessedSample {

    private static Log log = LogFactory.getLog(CustomSample.class);

    private ProcessedSample sample;
    private final Map<Param, Object> customParam;
    private final Map<EvaluatedParam, Object> customEvaluatedParam;

    public CustomSample(ProcessedSample sample, Map<Param, Object> customParam, Map<EvaluatedParam, Object> customEvaluatedParam ) {
        super(sample);
        this.sample = sample;
        this.customParam = customParam;
        this.customEvaluatedParam = customEvaluatedParam;
    }

    @Override
    public Object getParam(Param param) {
        Object retVal = customParam.get(param);
        if( retVal == null ) {
            retVal = sample.getParam(param);
            log.info("User didn't set " + param + ", using the one measured ( " + retVal + " )");
        } else {
            log.info("Using customParam value for " + param + " ( " + retVal + " )");
        }

        return retVal;
    }


//    @Override
//    public Object getEvaluatedParam(EvaluatedParam param) {
//        Object retVal = customEvaluatedParam.get(param);
//        if( retVal == null ) {
//            retVal = sample.getEvaluatedParam(param);
//            log.info("User didn't set " + param + ", using the one measured ( " + retVal + " )");
//        } else {
//            log.info("Using customParam value for " + param + " ( " + retVal + " )");
//        }
//
//        return retVal;
//    }

    @Override
    protected Double getACF() {
        Object retVal;
        retVal = customEvaluatedParam.get(EvaluatedParam.ACF);

        if(retVal==null) {
            Object retObject = sample.getEvaluatedParam(EvaluatedParam.ACF);
            if(retObject != null){
                retVal =  retObject;
                log.info("User didn't set ACF, using the one measured ( " + retVal + " )");
            }
        }
        if(retVal!=null)
            return (Double) retVal;
        return null;
    }



}
