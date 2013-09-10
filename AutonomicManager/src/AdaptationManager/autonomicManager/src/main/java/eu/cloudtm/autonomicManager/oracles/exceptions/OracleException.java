package eu.cloudtm.autonomicManager.oracles.exceptions;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 7/10/13
 */
public class OracleException extends Exception {

    public OracleException(String str){
        super(str);
    }

    public OracleException(Throwable t){
        super(t);
    }

}
