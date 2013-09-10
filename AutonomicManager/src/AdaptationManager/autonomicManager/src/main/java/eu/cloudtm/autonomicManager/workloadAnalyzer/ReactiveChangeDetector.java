package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.SampleProducer;

import java.util.Map;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/19/13
 * Time: 2:06 PM
 */
public class ReactiveChangeDetector extends AbstractChangeDetector {

    public ReactiveChangeDetector(Map<Param, Double> monitoredParams2delta, Map<EvaluatedParam, Double> monitoredEvaluatedParams2delta) {
        super(monitoredParams2delta, monitoredEvaluatedParams2delta);
    }

    @Override
    public void samplePerformed(ProcessedSample sample) {
        add(sample);

        if(sampleSlideWindow.size() < SLIDE_WINDOW_SIZE){
            return;
        }

        boolean reconfigure = evaluateParam() || evaluateEvaluatedParam();
        if(reconfigure){
            fireEvent( WorkloadEvent.WorkloadEventType.WORKLOAD_CHANGED, sample );
        }
    }

}
