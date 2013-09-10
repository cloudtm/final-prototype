package eu.cloudtm.autonomicManager.statistics;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/16/13
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Processor {

    public ProcessedSample process(Sample sample);

}
