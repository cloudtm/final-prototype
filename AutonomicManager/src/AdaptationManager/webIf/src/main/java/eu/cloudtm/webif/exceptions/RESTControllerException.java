package eu.cloudtm.webif.exceptions;

public class RESTControllerException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3619892833617701553L;

	public RESTControllerException(Throwable t){
		super(t);		
	}
	
	public RESTControllerException(String  msg){
		super(msg);		
	}
		
}
