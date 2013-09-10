package eu.cloudtm.autonomicManager.workloadAnalyzer;

import eu.cloudtm.autonomicManager.AbstractPlatformOptimizer;
import eu.cloudtm.autonomicManager.Optimizer;
import eu.cloudtm.autonomicManager.Reconfigurator;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.configs.Config;
import eu.cloudtm.autonomicManager.configs.KeyConfig;
import eu.cloudtm.autonomicManager.statistics.SampleProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/19/13
 * Time: 1:49 PM
 */
public class WorkloadAnalyzerFactory {

    private static Log log = LogFactory.getLog(WorkloadAnalyzerFactory.class);

    private SampleProducer statsManager;
    private Reconfigurator reconfigurator;
    private Optimizer optimizer;

    Map<Param, Double> param2delta = new HashMap<Param, Double>(){{
        log.warn("TODO read params from config file");

        log.trace("CHANGE DETECTOR MONITOR:");
        log.trace("Param.AvgNumPutsBySuccessfulLocalTx, 20");
        put(Param.AvgNumPutsBySuccessfulLocalTx, 20D);

        log.trace("Param.PercentageSuccessWriteTransactions, 20");
        put(Param.PercentageSuccessWriteTransactions, 20D);

        log.trace("Param.LocalUpdateTxLocalServiceTime, 20");
        put(Param.LocalUpdateTxLocalServiceTime, 20D);

        log.trace("Param.LocalReadOnlyTxLocalServiceTime, 20");
        put(Param.LocalReadOnlyTxLocalServiceTime, 20D);

        log.trace("Param.AvgGetsPerWrTransaction, 20");
        put(Param.AvgGetsPerWrTransaction, 20D);

        log.trace("Param.AvgGetsPerROTransaction, 20");
        put(Param.AvgGetsPerROTransaction, 20D);
    }};

    Map<EvaluatedParam, Double> evaluatedParam2delta = new HashMap<EvaluatedParam, Double>(){{
        log.trace("Param.ACF, 20");
        put(EvaluatedParam.ACF, 20D);
    }};

    public WorkloadAnalyzerFactory(SampleProducer statsManager,
                                    Reconfigurator reconfigurator,
                                    Optimizer optimizer){
        this.statsManager = statsManager;
        this.reconfigurator = reconfigurator;
        this.optimizer = optimizer;

    }

    public WorkloadAnalyzer build(){

        WorkloadForecaster workloadForecaster = new WorkloadForecaster(
                param2delta.keySet(),
                evaluatedParam2delta.keySet()
        );

        AbstractChangeDetector proactiveChangeDetector = new ProactiveChangeDetector(
                param2delta,
                evaluatedParam2delta,
                workloadForecaster
        );

        AbstractChangeDetector reactiveChangeDetector = new ReactiveChangeDetector(
                param2delta,
                evaluatedParam2delta
        );


        String policy = Config.getInstance().getString(KeyConfig.ALERT_MANAGER_POLICY.key());
        AbstractAlertManager alertManager = AbstractAlertManager.createInstance(policy, optimizer, reconfigurator);

        proactiveChangeDetector.addEventListener(alertManager);
        reactiveChangeDetector.addEventListener(alertManager);

        boolean enabled = Config.getInstance().getBoolean(KeyConfig.WORKLOAD_ANALYZER_AUTOSTART.key());

        WorkloadAnalyzer workloadAnalyzer = new WorkloadAnalyzer(enabled,
                statsManager,
                reactiveChangeDetector,
                proactiveChangeDetector,
                alertManager);

        return workloadAnalyzer;
    }

}
