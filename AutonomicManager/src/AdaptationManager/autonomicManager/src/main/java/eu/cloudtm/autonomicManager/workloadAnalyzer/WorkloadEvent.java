package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.statistics.ProcessedSample;

import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/19/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkloadEvent extends EventObject {

    public enum WorkloadEventType{
        WORKLOAD_CHANGED, WORKLOAD_WILL_CHANGE;
    }

    private final WorkloadEventType type;
    private final long timestamp;
    private final ProcessedSample sample;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException
     *          if source is null.
     */
    public WorkloadEvent(AbstractChangeDetector source, WorkloadEventType type, ProcessedSample sample) {
        super(source);
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.sample = sample;
    }

    public WorkloadEventType getEventType(){
        return type;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public ProcessedSample getSample(){
        return sample;
    }


}
