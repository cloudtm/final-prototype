package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/24/13
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class FakeAnalyticalOracle implements Oracle {

    private static Log log = LogFactory.getLog(FakeAnalyticalOracle.class);

    private static AtomicInteger partialCounter = new AtomicInteger(0);
    private static AtomicInteger counter = new AtomicInteger(0);

    private Random rnd = new Random();
//
//    private Map<Integer, PlatformConfiguration> counter2plat = new HashMap<Integer, PlatformConfiguration>(){{
//        put(0,  new PlatformConfiguration(2,  2, ReplicationProtocol.TO ) );        // full
//        put(1,  new PlatformConfiguration(4,  4, ReplicationProtocol.TWOPC ) );     // full
//        put(2, new PlatformConfiguration(6,  3, ReplicationProtocol.TO ) );         // 50%
//        put(3, new PlatformConfiguration(8,  2, ReplicationProtocol.TO ) );         // 25%
//        put(4, new PlatformConfiguration(10, 2, ReplicationProtocol.TWOPC ) );      // 20%
//    }};


//    @Override
//    public OutputOracle forecast(InputOracle input) throws OracleException {
//
//        PlatformConfiguration toApply = counter2plat.get( getCurr() );
//
//        boolean scaleBool = (toApply.platformSize() == (Integer) input.getForecastParam(ForecastParam.NumNodes))? true : false;
//        boolean degreeBool = (toApply.replicationDegree() == (Integer) input.getForecastParam(ForecastParam.ReplicationDegree))? true : false;
//        boolean protocolBool = (toApply.replicationProtocol() == (ReplicationProtocol) input.getForecastParam(ForecastParam.ReplicationProtocol))? true : false;
//
//        OutputOracle toReturn;
//        if(scaleBool && degreeBool && protocolBool){
//            //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//            toReturn = new OutputOracleImpl(1000, rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble() );
//        } else {
//            toReturn = new OutputOracleImpl(0, rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble() );
//        }
//
//        if( partialCounter.incrementAndGet() >= 135 ){
//            partialCounter.set(0);
//            counter.incrementAndGet();
//            //System.out.println("incrementing curr to:" + getCurr() );
//        } else {
//            //System.out.println("curr: " + getCurr());
//        }
//        return toReturn;
//    }
//
//    private int getCurr(){
//        return counter.get() % 5;
//    }


    // THIS HAS BEEN USED TO MAKE SNAPSHOTs

    private Map<Integer, OutputOracleImpl> counter2plat = new HashMap<Integer, OutputOracleImpl>(){{
        put(0,  new OutputOracleImpl(500, 0.3, 18, 23.4 ) );
        put(1,  new OutputOracleImpl(820, 0.42, 20, 26 ) );
        put(2,  new OutputOracleImpl(930, 0.54, 22, 28.6 ) );
        put(3,  new OutputOracleImpl(965, 0.59, 28, 36.4 ) );
        put(4,  new OutputOracleImpl(1010, 0.65, 34, 44.2 ) );
        put(5,  new OutputOracleImpl(1120, 0.74, 44, 57.2 ) );
        put(6,  new OutputOracleImpl(1210, 0.81, 50, 65 ) );
        put(7,  new OutputOracleImpl(1290, 0.87, 57, 74.1 ) );
        put(8,  new OutputOracleImpl(1350, 0.93, 62, 80.6 ) );
    }};

    @Override
    public OutputOracle forecast(InputOracle input) throws OracleException {

        log.info( "counter: " + counter.get() );
        OutputOracle toReturn = counter2plat.get( getCurr() );

        counter.incrementAndGet();

        return toReturn;
    }

    private int getCurr(){
        return counter.get() % 9;
    }
}
