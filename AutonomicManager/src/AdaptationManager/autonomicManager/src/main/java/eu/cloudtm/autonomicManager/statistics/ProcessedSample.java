package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.IsolationLevel;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.SystemType;
import eu.cloudtm.autonomicManager.configs.Config;
import eu.cloudtm.autonomicManager.configs.KeyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/8/13
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProcessedSample implements Sample {

    private static Log log = LogFactory.getLog(ProcessedSample.class);

    private static final int CORE_PER_CPU = 8;
    private static final SystemType DEFAULT_SYSTEM_TYPE = SystemType.MULE;

    protected Sample sample;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private Map<EvaluatedParam, Object> evaluatedParams = new HashMap<EvaluatedParam, Object>();

   public Map<EvaluatedParam, Object> getEvaluatedParams(){
      return this.evaluatedParams;
   }

   public Sample getInnerSample(){
      return this.sample;
   }

    public ProcessedSample(Sample sample){
        this.sample = sample;
    }

    @Override
    public long getId() {
        return sample.getId();
    }

    private synchronized void init(){
        // push here all the customizations
        evaluatedParams.put( EvaluatedParam.ACF, getACF() );
        evaluatedParams.put( EvaluatedParam.CORE_PER_CPU, getCoreCPU() );
        evaluatedParams.put( EvaluatedParam.SYSTEM_TYPE, getSystemType() );
        evaluatedParams.put( EvaluatedParam.MAX_ACTIVE_THREADS, 1 );
        evaluatedParams.put( EvaluatedParam.ISOLATION_LEVEL, getIsolationLevel() );

        log.warn("STOYAN's CODE COMMENTED in ProcessedSample! ");
        //evaluatedParams.put( EvaluatedParam.DATA_ACCESS_FREQUENCIES, getDataAccessFrequencies() );
        //evaluatedParams.put( EvaluatedParam.TX_INVOKER_FREQUENCY, getTxInvokeFrequency() );
        //evaluatedParams.put( EvaluatedParam.TX_RESPONSE_TIME, getTxResponseTime() );

    }

    @Override
    public Object getParam(Param param) {
        return sample.getParam(param);
    }

    public Map<String, Object> getParams() {
        return sample.getParams();
    }


    public synchronized Object getEvaluatedParam(EvaluatedParam param) {
        if(initialized.compareAndSet(false, true)){
            init();
        }
        Object ret = evaluatedParams.get(param);
        if(ret != null)
            return ret;
        else
            throw new IllegalArgumentException("param " + param + " is not present" );
    }

    private final IsolationLevel getIsolationLevel(){

        String isolationLevelStr = Config.getInstance().getString(KeyConfig.ENVIRONMENT_ISOLATION_LEVEL.key());
        IsolationLevel isolationLevel = IsolationLevel.valueOf(isolationLevelStr);

        if(isolationLevel==null){
            throw new IllegalArgumentException("Wrong isolation level. Available values: " + IsolationLevel.values() );
        }

        return isolationLevel;
    }

    protected abstract Double getACF();

    protected final int getCoreCPU(){
        return CORE_PER_CPU;
    }

    protected final SystemType getSystemType(){
        return DEFAULT_SYSTEM_TYPE;
    }

    private LinkedHashMap<String,LinkedHashMap<String,Integer>> getDataAccessFrequencies() {
        LinkedHashMap<String,LinkedHashMap<String,Integer>> result = new LinkedHashMap<String,LinkedHashMap<String,Integer>>();
        //String readData = (String)processedSample.getParam(Param.getByName("DAP.DapReadAccessData_0"));
        //String writeData = (String)processedSample.getParam(Param.getByName("DAP.DapWriteAccessData_0"));
        LinkedList<String> paramNameList = new LinkedList<String>();
        LinkedHashMap<String,Integer> contextStats;
        String toParse;
        String[] contexts;
        String currentContext;
        String contextName;
        String[] splitContext;
        String[] tokens;
        String domainAttribute;
        String domainClass;
        String frequency;

        paramNameList.add("DAP.DapReadAccessData_0");
        paramNameList.add("DAP.DapWriteAccessData_0");

        for (String paramName : paramNameList) {
            //toParse = readData;
            toParse = (String) getParam(Param.getByName(paramName));
            contexts = toParse.split("#");

            for (int i = 0; i < contexts.length; i++) {
                splitContext = contexts[i].split(":");
                contextName = splitContext[0].split("_")[0];
                tokens = splitContext[1].split(";");

                if (result.containsKey(contextName)) {
                    contextStats = result.get(contextName);
                } else {
                    contextStats = new LinkedHashMap<String,Integer>();// domainClass - accessFrequency
                    result.put(contextName, contextStats);
                }

                for (int j = 0; j < tokens.length; j++) {
                    //fullyQualifiedDomainClassName.attributeName=accessFrequency
                    domainAttribute = tokens[j].split("=")[0];
                    frequency = tokens[j].split("=")[1];
                    domainClass = domainAttribute.substring(0, domainAttribute.lastIndexOf("."));

                    if (contextStats.containsKey(domainClass))
                        contextStats.put(domainClass, contextStats.get(domainClass) + (new Integer(frequency)));
                    else
                        contextStats.put(domainClass, new Integer(frequency));
                }
            }
        }
        return result;
    }

    private LinkedHashMap<String,Integer> getTxInvokeFrequency() {
        LinkedHashMap<String,Integer> result = new LinkedHashMap<String,Integer>();
        LinkedList<String> txClassList = new LinkedList<String>();
        String attributeName = "AvgTxArrivalRate_0";

        txClassList.add("NBST_CLASS");

        for (String txClass : txClassList) {
            result.put(txClass, ((Long) getParam(Param.getByName(txClass + "." + attributeName))).intValue());
        }

        //result.put("NBSST_CLASS", ((Long)processedSample.getParam(Param.getByName("NBST_CLASS.AvgTxArrivalRate_0"))).intValue());

        return result;
    }

    private LinkedHashMap<String,Float> getTxResponseTime() {
        LinkedHashMap<String,Float> result = new LinkedHashMap<String,Float>();
        LinkedList<String> txClassList = new LinkedList<String>();
        String attributeName = "AvgResponseTime_0";

        txClassList.add("NBST_CLASS");

        for (String txClass : txClassList) {
            result.put(txClass, ((Long) getParam(Param.getByName(txClass + "." + attributeName))).floatValue());
        }

        //result.put("NBST_CLASS", ((Long)processedSample.getParam(Param.getByName("NBST_CLASS.AvgResponseTime_0"))).floatValue());

        return result;
    }

}
