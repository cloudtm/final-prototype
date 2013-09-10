package eu.cloudtm.autonomicManager.statistics;


import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/8/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TWOPCProcessedSample extends ProcessedSample {

    public TWOPCProcessedSample(Sample sample) {
        super(sample);
    }

    @Override
    public Double getACF() {

//        double PaoloLocalTakenLocks = (Long) getParam(Param.PaoloLocalTakenLocks);
//        double NumPuts = (Double) getParam(Param.NumPuts);
//        double PaoloLocalTakenHoldTime = (Double) getParam(Param.PaoloLocalTakenHoldTime);
//        double PaoloRemoteTakenHoldTime = (Double) getParam(Param.PaoloRemoteTakenHoldTime);
//        double PaoloRemoteTakenLocks = (Double) getParam(Param.PaoloRemoteTakenLocks);
//        double threads = 0; //Controller.getInstance().getCurrentConfiguration().threadPerNode();
//        double timeWindow = 60D; // Controller.TIME_WINDOW;
//
//        double acf = new ACF(
//                PaoloLocalTakenLocks,
//                NumPuts,
//                PaoloLocalTakenHoldTime,
//                PaoloRemoteTakenHoldTime,
//                PaoloRemoteTakenLocks,
//                threads,
//                timeWindow).evaluate();
        Random rnd = new Random();

        //return rnd.nextDouble();
        return 0.0D;
    }

}
