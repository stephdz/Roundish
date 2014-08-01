package fr.dz.roundish.util;

import lombok.Getter;

import org.apache.commons.lang.exception.ExceptionUtils;

import fr.dz.roundish.ServiceException;


/**
 * Exception wrapper in order to convert it to JSON
 */
@Getter
public class JSONException {
	private transient Throwable cause;
	private String code;
	private String message;
	private String localizedMessage;
	private String details;
	
	/**
	 * Constructor
	 * @param cause
	 */
	public JSONException(Throwable cause) {
		this.cause = cause;
		this.code = getCode(cause);
		this.message = getMessage(cause);
		this.localizedMessage = getLocalizedMessage(cause);
		this.details = getDetails(cause);
	}
	
	/**
	 * Get exception code
	 * @param cause
	 * @return
	 */
	public static String getCode(Throwable cause) {
		if ( cause instanceof ServiceException ) {
			ServiceException e = (ServiceException) cause;
			return e.getCode();
		} else {
			return getMessage(cause);
		}
	}
	
	/**
	 * Get exception message
	 * @param cause
	 * @return
	 */
	public static String getMessage(Throwable cause) {
		return cause.getMessage();
	}
	
	/**
	 * Get exception localized message
	 * @param cause
	 * @return
	 */
	public String getLocalizedMessage(Throwable cause) {
		return cause.getLocalizedMessage();
	}
	
	/**
	 * Get exception details
	 * @param cause
	 * @return
	 */
	public String getDetails(Throwable cause) {
		return ExceptionUtils.getFullStackTrace(cause);
	}
}
