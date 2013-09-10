package eu.cloudtm.autonomicManager;

import com.google.gson.Gson;
import eu.cloudtm.autonomicManager.commons.*;
import eu.cloudtm.autonomicManager.commons.dto.WhatIfCustomParamDTO;
import eu.cloudtm.autonomicManager.commons.dto.WhatIfDTO;
import eu.cloudtm.autonomicManager.optimizers.OptimizerType;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import eu.cloudtm.autonomicManager.statistics.StatsManager;
import eu.cloudtm.autonomicManager.workloadAnalyzer.WorkloadAnalyzer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/10/13
 */
public class AutonomicManager {

    private static Log log = LogFactory.getLog(AutonomicManager.class);

    private State state;
    private PlatformTuning platformTuning;
    private PlatformConfiguration platformConfiguration;
    private StatsManager statsManager;
    private WorkloadAnalyzer workloadAnalyzer;
    private Optimizer optimizer;
    private Reconfigurator reconfigurator;


    public AutonomicManager(State state,
                            PlatformConfiguration platformConfiguration,
                            PlatformTuning platformTuning,
                            StatsManager sampleManager,
                            WorkloadAnalyzer workloadAnalyzer,
                            Optimizer optimizer,
                            Reconfigurator reconfigurator){
        this.state = state;
        this.statsManager = sampleManager;
        this.platformConfiguration = platformConfiguration;
        this.platformTuning = platformTuning;

        this.workloadAnalyzer = workloadAnalyzer;
        this.optimizer = optimizer;
        this.reconfigurator = reconfigurator;
    }

    public void customConfiguration( Map<OptimizerType, Object> configuration){
        reconfigurator.reconfigure( configuration );
    }

    public boolean isWorkloadAnalyzerEnabled(){
        return workloadAnalyzer.isEnabled();
    }


    public void switchWorkloadAnalyzer(){
        workloadAnalyzer.enable( !workloadAnalyzer.isEnabled() );
    }

    public void optimizeNow(){
        optimizer.optimize( statsManager.getLastSample() );
    }

    public List<WhatIfDTO> whatIf(List<Forecaster> forecasters, ReplicationProtocol protocol, int degree, WhatIfCustomParamDTO customParamDTO ){

        ProcessedSample processedSample = statsManager.getLastSample();
        if(processedSample==null) {
            log.warn("No sample in the StatsManager");
            //return null;
        }

        WhatIfService whatIfService = new WhatIfService(processedSample);

        /* retrieving current values */
        if(customParamDTO == null){
            customParamDTO = whatIfService.retrieveCurrentValues();
        }

        /* forecaster */
        for(Forecaster forecaster : forecasters){
            customParamDTO.addForecaster(forecaster);
        }

        /* replication protocol */
        customParamDTO.setFixedProtocol(protocol);

        /* replication degree */
        customParamDTO.setFixedDegree(degree);

        List<WhatIfDTO> result = whatIfService.evaluate(customParamDTO);

//        /* Stampa delle predizioni */
//        for (WhatIfDTO whatIfRes : result){
//            log.info("Forecaster: " + whatIfRes.getForecaster());
//            log.info("to reimplement");
//        }

//        Gson gson = new Gson();
//        String json = gson.toJson(result);

//        log.trace("RESULT: " + json);

        return result;
    }

    private PlatformConfiguration currentConfiguration(){
        log.trace("Cloning platformConfiguration...");
        return platformConfiguration();
    }

    /**
     * Returns a copy of current PlatformTuning
     * @return a copy of current PlatformTuning
     */
    public PlatformTuning platformTuning(){
        log.trace("Cloning platformTuning...");
        return platformTuning.cloneThroughJson();
    }

    /**
     * Returns a copy of current PlatformConfiguration
     * @return a copy of current PlatformConfiguration
     */
    public PlatformConfiguration platformConfiguration(){
        log.trace("Cloning platformConfiguration...");
        return platformConfiguration.cloneThroughJson();
    }

    /**
     * Returns a copy of current State
     * @return a copy of current State
     */
    public State state(){
        log.trace("Cloning state...");
        return state.cloneThroughJson();
    }

    public void updateForecaster(Forecaster forecaster){
        log.info("Updating forecaster from:" + platformTuning().forecaster() + " to " + forecaster);
        platformTuning.setForecaster(forecaster);
    }

    public void updateScale(boolean tuning, int size, InstanceConfig instanceConfig){
        log.info("Updating scale");

        platformTuning.autoScale(tuning);
        if(!tuning){
            log.info("Triggering reconfiguration (" + size + ")");
            platformConfiguration.setPlatformScale(size, instanceConfig);
        }

    }

    public void updateDegree(boolean tuning, int degree){
        log.info("Updating degree");

        platformTuning.autoDegree(tuning);
        if(!tuning){
            log.info("Triggering reconfiguration (" + degree + ")");
            platformConfiguration.setRepDegree(degree);
        }
    }

    public void updateProtocol(boolean tuning, ReplicationProtocol protocol){
        log.info("Updating protocol");

        platformTuning.autoProtocol(tuning);
        if(!tuning){
            log.info("Triggering reconfiguration (" + protocol + ")");
            platformConfiguration.setRepProtocol(protocol);
        }
    }

    public StatsManager getStatsManager(){
        return statsManager;
    }

}
