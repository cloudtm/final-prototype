package eu.cloudtm.autonomicManager.oracles;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/12/13
 */
public class OutputOracleImpl implements OutputOracle {

    private double throughput;
    private double abortProbability;
    private double responseTimeRead;
    private double responseTimeWrite;

    public OutputOracleImpl(double throughput, double abortProbability, double responseTimeRead, double responseTimeWrite) {
        this.throughput = throughput;
        this.abortProbability = abortProbability;
        this.responseTimeRead = responseTimeRead;
        this.responseTimeWrite = responseTimeWrite;
    }

    @Override
    public double throughput(int txClassId) {
        return throughput;
    }

    @Override
    public double abortRate(int txClassId) {
        return abortProbability;
    }

    @Override
    public double responseTime(int txClassId) {
        return responseTimeRead;
    }

    @Override
    public double getConfidenceThroughput(int txClassId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getConfidenceAbortRate(int txClassId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getConfidenceResponseTime(int txClassId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "OutputOracleImpl{" +
                "throughput=" + throughput +
                ", abortProbability=" + abortProbability +
                ", responseTimeRead=" + responseTimeRead +
                '}';
    }

//    @Override
//    public int compareTo(Object o) {
//        //Comparison based #nodes first, #thread later
//        if( this.platformConfiguration.platformSize() == ((OutputOracleImpl) o).platformConfiguration.platformSize() ) {
//            return 0;
////            if (this.platformConfiguration.getNumThreads() == ((OutputOracleImpl) o).platformConfiguration.getNumThreads()) {
////
////            } else if (this.platformConfiguration.getNumThreads() > ((OutputOracleImpl) o).platformConfiguration.getNumThreads()) {
////                return 1;
////            } else {
////                return -1;
////            }
//        } else if( this.platformConfiguration.platformSize() > ((OutputOracleImpl) o).platformConfiguration.platformSize() ) {
//            return 1;
//        } else {
//            return -1;
//        }
//        //throw new RuntimeException("TO IMPLEMENT!");
//    }

    @Override
    public boolean equals(Object obj){
        if( this.throughput == ((OutputOracleImpl) obj).throughput )
            if( this.abortProbability == ((OutputOracleImpl) obj).abortProbability )
                if( this.responseTimeRead == ((OutputOracleImpl) obj).responseTimeRead)
                    return true;
        return false;
    }

}
