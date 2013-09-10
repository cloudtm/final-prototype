package eu.cloudtm.autonomicManager.commons;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/1/13
 */
public class PlatformConfiguration implements Comparable<PlatformConfiguration> {

    private static Log log = LogFactory.getLog(PlatformConfiguration.class);


    /* SCALE */
    private int platformSize = 2;
    private int threadPerNode = 2;
    private InstanceConfig nodesConfig = InstanceConfig.MEDIUM;

    /* REP PROT */
    private ReplicationProtocol replicationProtocol = ReplicationProtocol.TWOPC;

    /* REP DEGREE */
    private int replicationDegree = 2;

    /* DATA PLACEMENT */
    private AtomicBoolean dataPlacement = new AtomicBoolean(false);

    public PlatformConfiguration(){
    }

    public PlatformConfiguration(int size, int repDegree, ReplicationProtocol repProtocol){
        setPlatformScale(size, InstanceConfig.MEDIUM);
        setRepDegree(repDegree);
        setRepProtocol(repProtocol);
    }


    /* *** GETTER *** */

    public int platformSize(){
        return this.platformSize;
    }

    public int threadPerNode(){
        return this.threadPerNode;
    }

    public InstanceConfig nodeConfiguration(){
        return this.nodesConfig;
    }

    public ReplicationProtocol replicationProtocol(){
        return this.replicationProtocol;
    }

    public int replicationDegree(){
        return this.replicationDegree;
    }

    public boolean isDataPlacement(){
        return dataPlacement.get();
    }

    /* ************************ *** */
    /* *** SCALE UPDATE METHODS *** */
    /* ************************ *** */

    public void setPlatformScale(int size, InstanceConfig nodesConfig){
        if( size<=0 || nodesConfig == null )
            throw new IllegalArgumentException("Scale not valid!");
        platformSize = size;
        this.nodesConfig = nodesConfig;
    }


    /* ******************************* *** */
    /* *** REP PROTOCOL UPDATE METHODS *** */
    /* ******************************* *** */

    public void setRepProtocol(ReplicationProtocol repProtocol){
        if(repProtocol==null)
            throw new IllegalArgumentException("Replication Protocol not valid!");
        replicationProtocol = repProtocol;
    }

    /* ***************************** *** */
    /* *** REP DEGREE UPDATE METHODS *** */
    /* ***************************** *** */

    public void setRepDegree(int repDegree){
        if(repDegree<=0)
            throw new IllegalArgumentException("Replication Degree not valid!");
        replicationDegree = repDegree;
    }

    /* ********************************* *** */
    /* *** DATA PLACEMENT UPDATE METHODS *** */
    /* ********************************* *** */

    public void setDataPlacement(boolean enabled){
        dataPlacement.set(enabled);
    }


    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("{ ")
                .append("Nodes: ").append( platformSize() ).append(", ")
                .append("Degree: ").append(replicationDegree()).append(", ")
                .append("Protocol: ").append( replicationProtocol() ).append(" ")
                .append("}");
        return builder.toString();
    }

    public PlatformConfiguration cloneThroughJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return gson.fromJson(json, PlatformConfiguration.class);
    }

    @Override
    public int compareTo(PlatformConfiguration o) {

        if( platformSize() > o.platformSize() ){
            return 1;
        } else if(platformSize() < o.platformSize() ){
            return -1;
        } else {
            if( replicationDegree() > o.replicationDegree() ){
                return 1;
            } else if( replicationDegree() < o.replicationDegree() ){
                return -1;
            } else {
                if( replicationProtocol().getId() > o.replicationProtocol().getId() ){
                    return 1;
                } else if( replicationProtocol().getId() < o.replicationProtocol().getId() ){
                    return -1;
                } else {
                    return 0;
                }

            }
        }

    }
}
