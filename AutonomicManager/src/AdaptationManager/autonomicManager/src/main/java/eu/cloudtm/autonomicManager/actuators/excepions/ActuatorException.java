package eu.cloudtm.autonomicManager.actuators.excepions;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/28/13
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActuatorException extends  Exception{

    public ActuatorException(Throwable t){
        super(t);
    }

    public ActuatorException(String str){
        super(str);
    }

}
