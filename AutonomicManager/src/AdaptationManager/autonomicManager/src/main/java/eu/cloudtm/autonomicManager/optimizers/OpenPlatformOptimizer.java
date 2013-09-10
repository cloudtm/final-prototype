package eu.cloudtm.autonomicManager.optimizers;

import eu.cloudtm.autonomicManager.AbstractPlatformOptimizer;
import eu.cloudtm.autonomicManager.SLAManager;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.PlatformTuning;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/16/13
 */
public class OpenPlatformOptimizer extends AbstractPlatformOptimizer {

    private static Log log = LogFactory.getLog(OpenPlatformOptimizer.class);

    private final static int ARRIVAL_RATE_GUARANTEE_PERC = 50;
    private final static int ABORT_GUARANTEE_PERC = 5;
    private final static int RESPONSE_TIME_GUARANTEE_PERC = 5;

    private SLAManager slaManager;

    public OpenPlatformOptimizer(SLAManager slaManager,
                                 PlatformConfiguration platformConfiguration,
                                 PlatformTuning platformTuning) {
        super(platformConfiguration, platformTuning);
        this.slaManager = slaManager;
    }


    public PlatformConfiguration optimize(ProcessedSample processedSample) {
        throw new RuntimeException("TO IMPLEMENT");
    }


}



// TODO: Cercare una configurazione valida per ogni classe transazionale (Read, Write) cioè che rispetta gli SLAs

        /*
        double arrivalRateToGuarantee =  current.throughput() +  (current.throughput() * (ARRIVAL_RATE_GUARANTEE_PERC / 100));
        log.trace("arrivalRateToGuarantee:" + arrivalRateToGuarantee);

        double abortRateToGuarantee = current.abortRate() + (current.abortRate() * (ABORT_GUARANTEE_PERC / 100));
        log.trace("abortRateToGuarantee:" + abortRateToGuarantee);

        double responseTimeToGuarantee = current.responseTime(0) + (current.responseTime(0) * (RESPONSE_TIME_GUARANTEE_PERC / 100));
        log.trace("responseTimeToGuarantee:" + abortRateToGuarantee);
        */

//SLAItem sla = slaManager.getReadSLA(arrivalRateToGuarantee);
// TODO: cercare una configurazione che soddisfi tutte le classi txs
