package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.autonomicManager.commons.Param;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/8/13
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Sample extends Serializable {

    public long getId();

    public Object getParam(Param param);

    public Map<String, Object> getParams();

}
