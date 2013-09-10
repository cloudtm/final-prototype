package eu.cloudtm.autonomicManager;

import com.google.gson.Gson;
import eu.cloudtm.autonomicManager.commons.Forecaster;
import eu.cloudtm.autonomicManager.commons.InstanceConfig;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.commons.dto.WhatIfCustomParamDTO;
import eu.cloudtm.autonomicManager.commons.dto.WhatIfDTO;
import eu.cloudtm.autonomicManager.optimizers.OptimizerType;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 9/3/13
 */
public class Console {

    private static Log log = LogFactory.getLog(Console.class);
    private final AutonomicManager autonomicManager;

    public Console(AutonomicManager autonomicManager){
        this.autonomicManager = autonomicManager;
    }

    public void menu(){
        int selected = -1;
        while (selected!=0){
            log.info("Actions:");
            log.info("0 - Exit");
            log.info("1 - Prints current platform configuration");
            log.info("2 - Prints current tuning configurations");
            log.info("3 - Change configurations");
            log.info("4 - WhatIfService");
            log.info("5 - Optimize now");
            log.info("6 - Enable/Disable WorkloadAnalyzer [ current: " + (autonomicManager.isWorkloadAnalyzerEnabled() ? "enabled" : "disabled")  + " ]");
            Scanner in = new Scanner(System.in);
            selected = in.nextInt();
            processInput(selected);
        }
    }

    private void processInput(int selection){
        switch (selection){
            case 0:
                break;
            case 1:
                currentConfiguration();
                break;
            case 2:
                break;
            case 3:
                customConfiguration();
                break;
            case 4:
                whatIf();
                break;
            case 5:
                optimizeNow();
                break;
            case 6:
                switchWorkloadAnalyzer();
                break;
            default:
                log.info("Unexpected input");
                break;
        }
    }

    private void currentConfiguration(){
        log.info("*** Current Configuration ***");
        log.info( autonomicManager.platformConfiguration() );
        log.info("");
    }

    private void customConfiguration(){

        PlatformConfiguration platformConfiguration = new PlatformConfiguration();

        try {
            Scanner in = new Scanner(System.in);

            log.info("Nodes: ");
            int nodes = in.nextInt();
            platformConfiguration.setPlatformScale(nodes, InstanceConfig.MEDIUM);


            log.info("Degree: ");
            int degree = in.nextInt();
            platformConfiguration.setRepDegree(degree);

            log.info("Protocol {2PC, TO, PB}: ");
            String protocolString = in.next();
            ReplicationProtocol protocol = ReplicationProtocol.valueOf(protocolString);
            platformConfiguration.setRepProtocol(protocol);

        } catch (IllegalArgumentException e){
            log.info("Illegal value for replication protocol!");
            return;
        }

        Map<OptimizerType, Object> configuration = new HashMap<OptimizerType, Object>();
        configuration.put(OptimizerType.PLATFORM, platformConfiguration);
        configuration.put(OptimizerType.LARD, null);

        autonomicManager.customConfiguration(configuration);
    }

    private void switchWorkloadAnalyzer(){
        autonomicManager.switchWorkloadAnalyzer();
    }

    private void optimizeNow(){
        autonomicManager.optimizeNow();
    }

    private void whatIf(){
        /* reading and setting whatIf options */
        Scanner in = new Scanner(System.in);


        /* forecaster */
        log.info("Oracle {ANALYTICAL, SIMULATOR, MACHINE_LEARNING}: ");
        String forecasterStr = in.next();
        Forecaster forecaster = Forecaster.valueOf(forecasterStr);

        List<Forecaster> forecasters = new ArrayList<Forecaster>();
        forecasters.add(forecaster);

        /* replication protocol */
        log.info("Replication Protocol {TWOPC, TO, PB}: ");
        String replicationProtocolStr = in.next();
        ReplicationProtocol replicationProtocol = ReplicationProtocol.valueOf(replicationProtocolStr);

        /* replication degree */
        log.info("Replication Degree: ");
        int repDegree = in.nextInt();

        List<WhatIfDTO> result = autonomicManager.whatIf(forecasters, replicationProtocol, repDegree, null);

//        /* Stampa delle predizioni */
//        for (WhatIfDTO whatIfRes : result){
//            log.info("Forecaster: " + whatIfRes.getForecaster());
//            log.info("to reimplement");
//
//        }

        Gson gson = new Gson();
        String json = gson.toJson(result);

        log.trace("RESULT: " + json);

    }


}
