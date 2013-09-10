package eu.cloudtm.autonomicManager.commons;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/16/13
 */
public class SLAItem implements Comparable<SLAItem> {

    private double throughput;
    private double abortRate;
    private double responseTime;

    public SLAItem(double _throughput, double _abortRate, double _responseTime){
        throughput = _throughput;
        abortRate = _abortRate;
        responseTime = _responseTime;
    }

    public double getThroughput() {
        return throughput;
    }

    public double getAbortRate() {
        return abortRate;
    }

    public double getResponseTime() {
        return responseTime;
    }

    @Override
    public int compareTo(SLAItem o) {
        if( throughput < o.getThroughput() )
            return -1;
        else if( throughput > o.getThroughput() )
            return 1;
        else
            return 0;
    }
}
