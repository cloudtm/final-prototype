package eu.cloudtm.autonomicManager.oracles;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 7/13/13
 */
public interface OutputOracle {

    public double throughput(int txClassId);

    public double abortRate(int txClassId);

    public double responseTime(int txClassId);

    public double getConfidenceThroughput(int txClassId);

    public double getConfidenceAbortRate(int txClassId);

    public double getConfidenceResponseTime(int txClassId);


}
