package eu.cloudtm.autonomicManager;

import eu.cloudtm.autonomicManager.commons.SLAItem;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Author: Fabio Perfetti (perfabio87 [at] gmail.com)
 * E-mail: perfabio87@gmail.com
 * Date: 6/16/13
 */
public class SLAManager {

    private Set<SLAItem> readTxClass = new TreeSet<SLAItem>(
            Arrays.asList(
                    new SLAItem(50000,1,10)
            )
    );

    private Set<SLAItem> writeTxClass = new TreeSet<SLAItem>(
            Arrays.asList(
                new SLAItem(50000,1,10)
            )
    );

    public SLAItem getWriteSLA(double throughput){

        for(SLAItem sla : writeTxClass){
            if(sla.getThroughput() > throughput)
                return sla;
        }
        return null;
    }

    public SLAItem getReadSLA(double throughput){

        for(SLAItem sla : readTxClass){
            if(sla.getThroughput()>throughput)
                return sla;
        }
        return null;
    }

}
