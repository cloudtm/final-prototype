package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.SampleProducer;

import java.util.Map;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/19/13
 * Time: 11:17 AM
 */
public class ProactiveChangeDetector extends AbstractChangeDetector {

    private WorkloadForecaster forecaster;

    public ProactiveChangeDetector(Map<Param, Double> monitoredParams2delta,
                                   Map<EvaluatedParam, Double> monitoredEvaluatedParams2delta,
                                   WorkloadForecaster forecaster) {
        super(monitoredParams2delta, monitoredEvaluatedParams2delta);
        this.forecaster = forecaster;
    }

    @Override
    public void samplePerformed(ProcessedSample sample) {
        forecaster.add(sample);
        add(forecaster.forecast());

        if(sampleSlideWindow.size() < SLIDE_WINDOW_SIZE){
            return;
        }

        boolean reconfigure = evaluateParam() || evaluateEvaluatedParam();
        if(reconfigure){
            fireEvent( WorkloadEvent.WorkloadEventType.WORKLOAD_WILL_CHANGE, sample);
        }
    }

}
