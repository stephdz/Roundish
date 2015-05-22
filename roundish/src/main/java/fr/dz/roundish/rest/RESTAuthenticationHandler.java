package fr.dz.roundish.rest;

import javax.inject.Singleton;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import fr.dz.roundish.Application;
import fr.dz.roundish.User;
import fr.dz.roundish.rest.model.LoginInfo;
import fr.dz.roundish.util.JsonUtils;


/**
 * REST authentication handler
 */
@Log4j
@Getter
@Singleton
@Path("auth")
public class RESTAuthenticationHandler extends RESTHandler {
		
	// Constants
	private static final String CONNECTED_USER = "roundish.connectedUser";

	/**
	 * Constructor
	 * @param application
	 */
	public RESTAuthenticationHandler(Application application) {
		super(application);
	}
	
	/*
	 * Real definition used by Application
	 */
	@Override
	public User getConnectedUser() {
		return (User) getRequest().getSession().getAttribute(CONNECTED_USER);
	}

	/**
	 * Return the connected user
	 * @param loginInfoJson
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response connectedUser() {
		try {
			return handleEntity(getConnectedUser());
		} catch(Throwable t) {
			return handleException(t);
		}
	}
	
	/**
	 * Perform user login
	 * @param loginInfoJson
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(String loginInfoJson) {
		try {
			
			// Do the login
			LoginInfo loginInfo = JsonUtils.convertToEntity(loginInfoJson, LoginInfo.class);
			User loggedUser = getApplication().login(loginInfo.getLogin(), loginInfo.getPassword());
			if ( loggedUser != null ) {
				log.debug("User '"+loginInfo.getLogin()+"' successfully connected");
				
				// Enforce to create a JSESSIONID and register the connected user
				HttpSession session = getRequest().getSession();
				session.setAttribute(CONNECTED_USER, loggedUser);
			} else {
				log.debug("User '"+loginInfo.getLogin()+"' failed to connect");
			}
			
			// Return the user
			return handleEntity(loggedUser);
		} catch(Throwable t) {
			return handleException(t);
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		try {
			
			// Do the logout
			User loggedUser = getConnectedUser();
			getApplication().logout(getRequest());
			if ( loggedUser != null ) {
				log.debug("User '"+loggedUser.getLogin()+"' successfully disconnected");
			}
			
			// Clear the session
			getRequest().getSession().invalidate();
			
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch(Throwable t) {
			return handleException(t);
		}
	}
}
