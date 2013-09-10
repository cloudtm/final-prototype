package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.ControllerLogger;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.SampleListener;
import eu.cloudtm.autonomicManager.statistics.SampleProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/19/13
 * Time: 1:46 PM
 */
public class WorkloadAnalyzer implements SampleListener {

    private static Log log = LogFactory.getLog(WorkloadAnalyzer.class);

    private AtomicBoolean enabled;

    private SampleProducer sampleProducer;
    private AbstractChangeDetector reactive, proactive;
    private WorkloadEventListener workloadEventListener;

    public WorkloadAnalyzer(Boolean enabled,
                            SampleProducer sampleProducer,
                            AbstractChangeDetector reactive,
                            AbstractChangeDetector proactive,
                            WorkloadEventListener workloadEventListener){
        this.reactive = reactive;
        this.proactive = proactive;
        this.workloadEventListener = workloadEventListener;

        this.sampleProducer = sampleProducer;
        this.sampleProducer.addListener(this);

        this.enabled = new AtomicBoolean(enabled);
        enable(this.enabled.get());
    }

    public boolean isEnabled(){
        return enabled.get();
    }

    public void enable(boolean value){
        enabled.set(value);
        ControllerLogger.log.info("WorkloadAnalyzer " + (enabled.get() ? "enabled" : "disabled") );
    }

    @Override
    public final void onNewSample(ProcessedSample sample){
        if( !isEnabled() ){
            log.info("WorkloadAnalyzer disabled! Skipping sample...");
            return;
        }
        reactive.samplePerformed(sample);
        proactive.samplePerformed(sample);
    }
}
