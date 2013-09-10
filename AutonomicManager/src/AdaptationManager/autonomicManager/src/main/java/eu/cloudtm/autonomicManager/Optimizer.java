package eu.cloudtm.autonomicManager;

import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;

/**
 * Author: Fabio Perfetti (perfabio87 [at] gmail.com)
 * Date: 8/6/13
 * Time: 2:08 PM
 */
public interface Optimizer {

    public abstract void optimize(ProcessedSample processedSample);

}
