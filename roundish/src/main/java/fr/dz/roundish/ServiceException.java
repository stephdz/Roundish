package fr.dz.roundish;

import lombok.Getter;
import lombok.NonNull;

/**
 * Exception that can be sent by a service
 */
@Getter
public class ServiceException extends Exception {
    private static final long serialVersionUID = 212474831955522981L;

    @NonNull
    private final String code;

    public ServiceException(final String code) {
	super();
	this.code = code;
    }

    public ServiceException(final String code, final String message, final Throwable cause) {
	super(message, cause);
	this.code = code;
    }

    public ServiceException(final String code, final String message) {
	super(message);
	this.code = code;
    }

    public ServiceException(final String code, final Throwable cause) {
	super(cause);
	this.code = code;
    }
}
