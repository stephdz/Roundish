package fr.dz.roundish;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;

import fr.dz.roundish.annotation.RESTService;
import fr.dz.roundish.rest.RESTAuthenticationHandler;
import fr.dz.roundish.rest.RESTServiceHandler;

/**
 * Roundish Jersey application
 */
@Log4j
@Getter
public abstract class Application extends ResourceConfig {
    private final RESTAuthenticationHandler authenticationHandler;

    /**
     * Constructor :
     * - load all service classes
     * - register RepositoryRESTService
     */
    public Application() {
	super();
	this.authenticationHandler = new RESTAuthenticationHandler(this);
	addServicePackages("");
	register(authenticationHandler);
	register(new RESTServiceHandler(this));
    }

    /**
     * Return the resource bundle for the given locale
     * TODO Implement authentication before
     * 
     * @param locale
     * @return
     */
    // public abstract ResourceBundle getResourceBundle(Locale locale);

    /**
     * Log in of a user :
     * - return the connected user if authentication is successfull
     * - return null if authentication failed - throws an exception if an error
     * occurred
     * 
     * @param login
     * @param password
     * @return
     * @throws ServiceException
     */
    public abstract User login(final String login, final String password) throws ServiceException;

    /**
     * Log out of a user :
     * - throws an exception if an error occurred
     * 
     * @param request
     * @return
     * @throws ServiceException
     */
    public void logout(final HttpServletRequest request) throws ServiceException {

    }

    /**
     * Return the connected user from the request
     * 
     * @param request
     * @return
     */
    public User getConnectedUser(final HttpServletRequest request) {
	return authenticationHandler.getConnectedUser();
    }

    /**
     * Find all repositories in the given packages
     * 
     * @param packageNames
     * @return
     */
    public void addServicePackages(final String... packageNames) {
	if (packageNames != null) {
	    Set<Class<?>> types = new HashSet<Class<?>>();
	    for (String packageName : packageNames) {
		Reflections reflections = new Reflections(packageName);
		types.addAll(reflections.getSubTypesOf(Service.class));
		types.addAll(reflections.getTypesAnnotatedWith(RESTService.class));
		types.addAll(reflections.getTypesAnnotatedWith(RESTService.class));
	    }
	    for (Class<?> type : types) {
		addService(type);
	    }
	}
    }

    /**
     * Add a repository from a class
     * 
     * @param clazz
     */
    public void addService(final Class<?> clazz) {
	try {
	    register(clazz.newInstance());
	} catch (Exception e) {
	    log.error("Roundish cannot instanciate class " + clazz.getName(), e);
	}
    }

    protected void setConnectedUser() {
	// TODO Auto-generated method stub

    }
}
