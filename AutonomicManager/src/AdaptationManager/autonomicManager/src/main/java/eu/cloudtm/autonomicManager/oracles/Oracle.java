package eu.cloudtm.autonomicManager.oracles;

import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/12/13
 */
public interface Oracle {

    public OutputOracle forecast(InputOracle input) throws OracleException;

}
