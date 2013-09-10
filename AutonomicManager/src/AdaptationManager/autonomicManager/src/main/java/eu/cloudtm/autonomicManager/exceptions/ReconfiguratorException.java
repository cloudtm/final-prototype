package eu.cloudtm.autonomicManager.exceptions;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/21/13
 */
public class ReconfiguratorException extends Exception {

    public ReconfiguratorException(String str){
        super(str);
    }

    public ReconfiguratorException(Throwable t){
        super(t);
    }

}
