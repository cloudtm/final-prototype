package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.configs.Config;
import eu.cloudtm.autonomicManager.configs.KeyConfig;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.SampleListener;
import eu.cloudtm.autonomicManager.statistics.SampleProducer;
import org.apache.commons.collections15.Buffer;
import org.apache.commons.collections15.BufferUtils;
import org.apache.commons.collections15.buffer.CircularFifoBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/19/13
 * Time: 11:24 AM
 */
public abstract class AbstractChangeDetector {

    private static Log log = LogFactory.getLog(AbstractChangeDetector.class);

    protected static final int SLIDE_WINDOW_SIZE = Config.getInstance().getInt(KeyConfig.SLIDE_WINDOW_SIZE.key() );

    protected Buffer<ProcessedSample> sampleSlideWindow = BufferUtils.synchronizedBuffer(new CircularFifoBuffer<ProcessedSample>(SLIDE_WINDOW_SIZE));

    private List<WorkloadEventListener> listeners = new ArrayList<WorkloadEventListener>();

    private Map<Param, Double> monitoredParams2delta;
    private Map<EvaluatedParam, Double> monitoredEvaluatedParams2delta;

    private Map<Param, Double> lastAvgParams = new HashMap<Param, Double>();
    private Map<EvaluatedParam, Double> lastAvgEvaluatedParams = new HashMap<EvaluatedParam, Double>();

    private AtomicBoolean enabled = new AtomicBoolean(false);


    public AbstractChangeDetector(Map<Param, Double> monitoredParams2delta,
                                  Map<EvaluatedParam, Double> monitoredEvaluatedParams2delta){

        this.monitoredParams2delta = monitoredParams2delta;
        this.monitoredEvaluatedParams2delta = monitoredEvaluatedParams2delta;
        init();
    }


    public abstract void samplePerformed(ProcessedSample sample);

    private void init(){

        for(Param param : monitoredParams2delta.keySet()){
            log.warn("initializing avg structure ["+param+"]");
            lastAvgParams.put(param, 0.0);
        }
        for(EvaluatedParam evaluatedParam : monitoredEvaluatedParams2delta.keySet()){
            log.warn("initializing avg structure ["+evaluatedParam+"]");
            lastAvgEvaluatedParams.put(evaluatedParam, 0.0);
        }
    }

    protected boolean evaluateParam(){
        for(Param param : monitoredParams2delta.keySet()){
            log.trace("Analyzing " + param.getKey());
            double sum = 0.0;
            for (ProcessedSample sample : sampleSlideWindow){
                sum +=  ((Number) sample.getParam( param ) ).doubleValue();
            }
            double currentAvg =  sum / ((double) sampleSlideWindow.size());
            log.debug("currentAvg: " + currentAvg);
            log.debug("lastAvg: " + lastAvgParams.get(param));

            if(lastAvgParams.get(param) == 0 || lastAvgParams.get(param) == Double.NaN){
                log.debug("Updating && Skipping lastAvgParams");
                lastAvgParams.put(param, currentAvg);
            } else {
                double ratio = ( currentAvg / lastAvgParams.get(param) ) * 100;
                log.debug("ratio: " + ratio );

                double variation = Math.abs(ratio - 100);
                log.debug("variation: " + variation );

                if( variation >= monitoredParams2delta.get(param) ){
                    log.trace("Updating lastAvgEvaluatedParams for " + param + " to: " + currentAvg);
                    lastAvgParams.put(param, currentAvg);
                    log.info("BOUND REACHED (" + param.getKey() + ")" );
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean evaluateEvaluatedParam(){
        for(EvaluatedParam param : monitoredEvaluatedParams2delta.keySet()){
            log.trace("");
            log.trace("Analyzing " + param.getKey());
            double sum = 0.0;
            for (ProcessedSample sample : sampleSlideWindow){
                sum += (Double) sample.getEvaluatedParam( param );
            }
            double currentAvg =  sum / ((double) sampleSlideWindow.size());
            log.debug("currentAvg: " + currentAvg);
            log.debug("lastAvg: " + lastAvgEvaluatedParams.get(param));

            if(lastAvgEvaluatedParams.get(param) == 0 || lastAvgEvaluatedParams.get(param) == Double.NaN){
                log.debug("Updating && Skipping lastAvgEvaluatedParams");
                lastAvgEvaluatedParams.put(param, currentAvg);
            } else {
                double ratio = ( currentAvg / lastAvgEvaluatedParams.get(param) ) * 100;
                log.debug("ratio: " + ratio );

                double variation = Math.abs(ratio - 100);
                log.debug("variation: " + variation );

                if( variation >= monitoredEvaluatedParams2delta.get(param) ){
                    log.trace("Updating lastAvgEvaluatedParams for " + param + " to: " + currentAvg);
                    lastAvgEvaluatedParams.put(param, currentAvg);
                    log.info("BOUND REACHED (" + param.getKey() + ")" );
                    return true;
                }

            }
        }
        return false;
    }

    protected boolean add(ProcessedSample sample){
        return sampleSlideWindow.add(sample);
    }

    public synchronized void addEventListener(WorkloadEventListener listener)  {
        listeners.add(listener);
    }

    public synchronized void removeEventListener(WorkloadEventListener listener)   {
        listeners.remove(listener);
    }

    protected synchronized void fireEvent(WorkloadEvent.WorkloadEventType type, ProcessedSample sample) {
        WorkloadEvent event = new WorkloadEvent(this, type, sample);
        Iterator<WorkloadEventListener> i = listeners.iterator();
        while(i.hasNext())  {
            i.next().workloadEventPerformed(event);
        }
    }
}
