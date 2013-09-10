package eu.cloudtm.autonomicManager.configs;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/22/13
 * Time: 11:20 AM
 */
public enum KeyConfig {

    /* ****** ENVIRONMENT ****** */

    ENVIRONMENT_ISOLATION_LEVEL ("environment.isolationLevel"),


    /* ****** WORKLOAD ANALYZER ****** */

    WORKLOAD_ANALYZER_AUTOSTART("workloadAnalyzer.autoStart"),

    ALERT_MANAGER_POLICY("alertManager.policy"),


    /* ******* OPTIMIZER ******* */
    OPTIMIZER_PLATFORM("optimizer.platform"),
    OPTIMIZER_LARD("optimizer.lard"),


    /* ******* ACTUATOR ******* */

    ACTUATOR_TYPE("actuator.type"),
    ACTUATOR_IS_RADARGUN("actuator.isRadargun"),

    DELTACLOUD_MAX_RETRIES("deltacloud.maxRetries"),
    DELTACLOUD_SECONDS_BETWEEN_RETRY("deltacloud.timeBetweenRetry"),

    DELTACLOUD_URL("deltacloud.url"),
    DELTACLOUD_USER("deltacloud.user"),
    DELTACLOUD_PASSWORD("deltacloud.password"),
    DELTACLOUD_IMAGE("deltacloud.image"),
    DELTACLOUD_FLAVOR("deltacloud.flavor"),
    DELTACLOUD_KEY("deltacloud.key"),

    ISPN_JMX_PORT("infinispan.jmxPort"),
    ISPN_DOMAIN("infinispan.domain"),
    ISPN_CACHE_NAME("infinispan.cacheName"),

    ISPN_ACTUATOR_FENIX_FRAMEWORK("infinispan.fenixFrameworkDomain"),
    ISPN_ACTUATOR_APPLICATION_NAME("infinispan.applicationName"),

    ISPN_ACTUATOR_FORCE_STOP("infinispan.forceStop"),
    ISPN_ACTUATOR_ABORT_ON_STOP("infinispan.abortOnStop"),

    RADARGUN_CLIENT("radargun.client"),
    RADARGUN_COMPONENT("radargun.component"),

    FUTUREGRID_FILE_NODES("futurgrid.fileNodes"),
    FUTUREGRID_START_SCRIPT("futuregrid.startScript"),
    FUTUREGRID_STOP_SCRIPT("futuregrid.stopScript"),


    /* ******* RECONFIGURATOR ******* */
    RECONFIGURATOR_IGNORE_ERROR("reconfigurator.ignoreError"),
    RECONFIGURATOR_SIMULATE("reconfigurator.simulate"),
    RECONFIGURATOR_SECONDS_BETWEEN_RECONFIGURATIONS("reconfigurator.timeBetweenReconfiguration"),

    RECONFIGURATOR_SWITCH_REBALANCING ("reconfigurator.switchRebalancing"),
    RECONFIGURATOR_RECONFIGURE_NODES("reconfigurator.reconfigureNodes"),
    RECONFIGURATOR_RECONFIGURE_DEGREE("reconfigurator.reconfigureDegree"),
    RECONFIGURATOR_RECONFIGURE_PROTOCOL("reconfigurator.reconfigureProtocol"),

    /* ******* CHANGE DETECTORs ******* */
    SLIDE_WINDOW_SIZE("changeDetector.slideWindowSize"),

    /* ******* FORECASTERs ******* */
    FORECASTER_ANALYTICAL("forecaster.analytical"),
    FORECASTER_SIMULATOR("forecaster.simulator"),
    FORECASTER_MACHINE_LEARNING("forecaster.machineLearning"),

    ;

    private final String key;

    private KeyConfig(String key){
        this.key = key;
    }

    public String key(){
        return this.key;
    }

}
