package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/24/13
 */
public class SimulatorTest {

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        try {
            sim.forecast(null);
        } catch (OracleException e) {
            e.printStackTrace();
        }


    }

}
