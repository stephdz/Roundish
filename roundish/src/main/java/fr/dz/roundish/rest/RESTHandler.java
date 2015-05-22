package fr.dz.roundish.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import fr.dz.roundish.Application;
import fr.dz.roundish.User;
import fr.dz.roundish.util.JsonUtils;

/**
 * REST handler common methods
 */
@Getter
@RequiredArgsConstructor
public abstract class RESTHandler {

    @NonNull
    private final Application application;

    @Context
    private HttpServletRequest request;

    /**
     * Return connected user
     * 
     * @return
     */
    public User getConnectedUser() {
	return application.getConnectedUser(getRequest());
    }

    /**
     * Handle an entity as a response
     * 
     * @param entity
     * @return
     */
    protected Response handleEntity(final Object entity) {
	if (entity == null) {
	    return Response.status(Response.Status.NO_CONTENT).build();
	} else {
	    return Response.status(Response.Status.OK).entity(JsonUtils.convertToJson(entity)).build();
	}
    }

    /**
     * Handle an exception and creates the response TODO Debug the request
     * 
     * @param e
     * @return
     */
    protected Response handleException(final Throwable e) {
	if (e instanceof WebApplicationException) {
	    WebApplicationException exception = (WebApplicationException) e;
	    return exception.getResponse();
	} else {
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JsonUtils.convertExceptionToJson(e))
		    .build();
	}
    }
}
