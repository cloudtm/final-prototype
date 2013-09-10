package eu.cloudtm.autonomicManager.optimizers;

import eu.cloudtm.autonomicManager.statistics.ProcessedSample;

/**
 * Author: Fabio Perfetti (perfabio87 [at] gmail.com)
 * Date: 8/10/13
 * Time: 11:35 AM
 */
public interface OptimizerFilter<T>{

    public OptimizerType getType();

    public T doOptimize(ProcessedSample processedSample);

}
