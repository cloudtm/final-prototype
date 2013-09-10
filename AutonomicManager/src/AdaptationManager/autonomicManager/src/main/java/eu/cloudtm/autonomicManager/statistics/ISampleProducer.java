package eu.cloudtm.autonomicManager.statistics;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/23/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISampleProducer {

    public void removeListener(SampleListener listener);

    public void addListener(SampleListener listener);

    public void notify(ProcessedSample sample);
}
