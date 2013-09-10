package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/24/13
 */
public class FakeMLOracle implements Oracle {

    private static AtomicInteger partialCounter = new AtomicInteger(0);
    private static AtomicInteger counter = new AtomicInteger(0);

    private Map<Integer, OutputOracleImpl> counter2plat = new HashMap<Integer, OutputOracleImpl>(){{
        put(0,  new OutputOracleImpl(530, 0.4, 15, 19.5 ) );
        put(1,  new OutputOracleImpl(778, 0.54, 18, 23.4 ) );
        put(2,  new OutputOracleImpl(805, 0.62, 24, 31.2 ) );
        put(3,  new OutputOracleImpl(925, 0.70, 27, 35.1 ) );
        put(4,  new OutputOracleImpl(997, 0.79, 33, 42.9 ) );
        put(5,  new OutputOracleImpl(1220, 0.81, 39, 50.7 ) );
        put(6,  new OutputOracleImpl(1238, 0.82, 47, 61.1 ) );
        put(7,  new OutputOracleImpl(1340, 0.89, 57, 74.1 ) );
        put(8,  new OutputOracleImpl(1440, 0.97, 63, 81.9 ) );
    }};

    @Override
    public OutputOracle forecast(InputOracle input) throws OracleException {

        OutputOracle toReturn = counter2plat.get( getCurr() );

        counter.incrementAndGet();

        return toReturn;
    }

    private int getCurr(){
        return counter.get() % 9;
    }

}
