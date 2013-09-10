package eu.cloudtm.webif;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <code>Set welcome message.</code>
 */
public class MyCloud extends BaseAction {

	private static Log log = LogFactory.getLog(MyCloud.class);

	public String execute() throws Exception {
		log.info("MyCloud execute()");

		//String output = response.getEntity(String.class);
		
		setMessage(getText(MESSAGE));
		return SUCCESS;
	}

	
	
	/**
	 * Provide default value for Message property.
	 */
	public static final String MESSAGE = "MyCloud.message";

	/**
	 * Field for Message property.
	 */
	private String message;

	/**
	 * Return Message property.
	 * 
	 * @return Message property
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set Message property.
	 * 
	 * @param message
	 *            Text to display on HelloWorld page.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
