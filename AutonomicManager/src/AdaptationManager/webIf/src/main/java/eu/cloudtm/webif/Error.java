package eu.cloudtm.webif;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Error extends BaseAction {
	
	private static Log log = LogFactory.getLog(Error.class);
	
    public String execute() throws Exception {
    	
    	log.info("Error execute()");
    	       
        return SUCCESS;
    }

}
