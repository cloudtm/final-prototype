package eu.cloudtm.autonomicManager;

import eu.cloudtm.autonomicManager.optimizers.OptimizerType;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/24/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Reconfigurator {

    public void reconfigure(Map<OptimizerType, Object> toReconfigure);

    public boolean isReconfiguring();

}
