package eu.cloudtm.autonomicManager.optimizers;

import eu.cloudtm.autonomicManager.ControllerLogger;
import eu.cloudtm.autonomicManager.Optimizer;
import eu.cloudtm.autonomicManager.Reconfigurator;
import eu.cloudtm.autonomicManager.SLAManager;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.PlatformTuning;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Fabio Perfetti (perfabio87 [at] gmail.com)
 * Date: 8/10/13
 * Time: 11:36 AM
 */
public class OptimizerImpl implements Optimizer {

    private static Log log = LogFactory.getLog(OptimizerImpl.class);
    private Reconfigurator reconfigurator;

    private List<OptimizerFilter> optimizerFilters;

    public OptimizerImpl(Reconfigurator reconfigurator, List<OptimizerFilter> optimizerFilters){
        this.reconfigurator = reconfigurator;
        this.optimizerFilters = optimizerFilters;
    }

    @Override
    public void optimize(ProcessedSample processedSample) {

        Map<OptimizerType, Object> filter2output = new HashMap<OptimizerType, Object>();

        for(OptimizerFilter filter : optimizerFilters){
            log.trace("Optimizing by " + filter.getType());
            Object ret = filter.doOptimize(processedSample);
            if(ret!=null){
                filter2output.put( filter.getType(),ret );
            } else {
                log.trace("OptimizerFilter " + filter.getType() + " returned a null");
            }
        }

        ControllerLogger.log.info(" »»» Ready to reconfigure «««" );
        reconfigurator.reconfigure( filter2output );

    }
}
