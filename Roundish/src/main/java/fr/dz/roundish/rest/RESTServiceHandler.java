package fr.dz.roundish.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang.StringUtils;

import fr.dz.roundish.Application;
import fr.dz.roundish.Repository;
import fr.dz.roundish.Service;
import fr.dz.roundish.annotation.RESTService;
import fr.dz.roundish.descriptor.ServiceDescriptor;
import fr.dz.roundish.descriptor.ServiceDescriptorImpl;
import fr.dz.roundish.descriptor.ServiceMethodDescriptor;
import fr.dz.roundish.util.JsonUtils;


/**
 * REST service handler
 */
@Log4j
@Getter
@Singleton
@Path("{service}")
@SuppressWarnings("unchecked")
public class RESTServiceHandler extends RESTHandler {
	
	// Services
	private Map<Class<? extends Service>,Map<String,ServiceDescriptor<?>>> services;
	
	// Service names
	private Set<String> serviceNames;
	
	/**
	 * Constructor
	 * @param application
	 */
	public RESTServiceHandler(Application application) {
		super(application);
		initServices();
	}
	
	/* 
	 * Generic service Methods
	 */
	
	/**
	 * Call a specified method from the service
	 * @param service
	 * @param method
	 * @param parametersJSON
	 * @return
	 */
	@POST
	@Path("{method}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callMethod(@PathParam("service") String service, @PathParam("method") String method, String parametersJSON) {
		try {
			ServiceDescriptor<Service> descriptor = getServiceDescriptor(Service.class, service);
			ServiceMethodDescriptor methodDescriptor = descriptor.getMethodDescriptor(method);
			Object[] parameters = JsonUtils.convertToParameters(parametersJSON, methodDescriptor);
			Object result = methodDescriptor.execute(getConnectedUser(), parameters);
			return handleEntity(result);
		} catch(Throwable t) {
			return handleException(t);
		}
	}

	/* 
	 * Repositories Methods
	 */
	
	/**
	 * Get all the elements from the repository
	 * TODO Handle lazy loading, pages or better
	 * @param service
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(@PathParam("service") String service) {
		try {
			return handleEntity(getServiceDescriptor(Repository.class, service).getService(getConnectedUser()).list());
		} catch(Throwable t) {
			return handleException(t);
		}
	}

	/**
	 * Create a new entity in the repository
	 * @param service
	 * @param entityJSON
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@SuppressWarnings("rawtypes")
	public Response create(@PathParam("service") String service, String entityJSON) {
		try {
			ServiceDescriptor<Repository> descriptor = getServiceDescriptor(Repository.class, service);
			Object entity = JsonUtils.convertToEntity(entityJSON, descriptor.getService(getConnectedUser()).getEntityClass());
			return handleEntity(descriptor.getService(getConnectedUser()).create(entity));
		} catch(Throwable t) {
			return handleException(t);
		}
	}
	
	/**
	 * Get an entity from the repository
	 * @param service
	 * @param keyJSON
	 * @return
	 */
	@GET
	@Path("{key}")
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("rawtypes")
	public Response get(@PathParam("service") String service, @PathParam("key") String keyJSON) {
		try {
			ServiceDescriptor<Repository> descriptor = getServiceDescriptor(Repository.class, service);
			Object key = JsonUtils.convertToEntity(keyJSON, descriptor.getService(getConnectedUser()).getKeyClass());
			return handleEntity(descriptor.getService(getConnectedUser()).get(key));
		} catch(Throwable t) {
			return handleException(t);
		}
	}
	
	/**
	 * Update an entity into the repository
	 * @param service
	 * @param keyJSON
	 * @param entityJSON
	 */
	@PUT
	@Path("{key}")
	@Consumes(MediaType.APPLICATION_JSON)
	@SuppressWarnings("rawtypes")
	public Response update(@PathParam("service") String service, @PathParam("key") String keyJSON, String entityJSON) {
		try {
			ServiceDescriptor<Repository> descriptor = getServiceDescriptor(Repository.class, service);
			Object key = JsonUtils.convertToEntity(keyJSON, descriptor.getService(getConnectedUser()).getKeyClass());
			Object entity = JsonUtils.convertToEntity(entityJSON, descriptor.getService(getConnectedUser()).getEntityClass());
			descriptor.getService(getConnectedUser()).update(key, entity);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch(Throwable t) {
			return handleException(t);
		}
	}
	
	/**
	 * Delete an entity from the repository
	 * @param service
	 * @param keyJSON
	 */
	@DELETE
	@Path("{key}")
	@SuppressWarnings("rawtypes")
	public Response delete(@PathParam("service") String service, @PathParam("key") String keyJSON) {
		try {
			ServiceDescriptor<Repository> descriptor = getServiceDescriptor(Repository.class, service);
			Object key = JsonUtils.convertToEntity(keyJSON, descriptor.getService(getConnectedUser()).getKeyClass());
			descriptor.getService(getConnectedUser()).delete(key);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch(Throwable t) {
			return handleException(t);
		}
	}
	
	/*
	 * Private utilities
	 */

	/**
	 * Get a service from it's name. If it doesn't exist, it throws a 404 exception.
	 * @param serviceName
	 * @return
	 */
	private <T extends Service> ServiceDescriptor<T> getServiceDescriptor(Class<T> type, String serviceName) {
		if ( getServices().containsKey(type) && getServices().get(type).containsKey(serviceName) ) {
			return (ServiceDescriptor<T>) getServices().get(type).get(serviceName);
		} else {
			throw new WebApplicationException(type.getSimpleName()+" '"+serviceName+"' doesn't exist", Response.Status.NOT_FOUND);
		}
	}

	/**
	 * Initializes a map of all defined services (including repositories)
	 * @param application
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private void initServices() {
		this.services = new HashMap<Class<? extends Service>,Map<String,ServiceDescriptor<?>>>();
		this.serviceNames = new HashSet<String>();
		for ( Object singleton : getApplication().getSingletons() ) {
			if ( singleton instanceof Service ) {
				ServiceDescriptor<?> descriptor = new ServiceDescriptorImpl((Service) singleton);
				if ( StringUtils.isBlank(descriptor.getName()) ) {
					log.warn("Service with class "+singleton.getClass().getName()+" must have @"+RESTService.class.getSimpleName()+" annotation. It will not be bound to an HTTP path.");
				} else if ( serviceNames.contains(descriptor.getName()) ) {
					log.warn("Multiple services bound to HTTP path '"+descriptor.getName()+"'. Only the first found will be registered.");
				} else {
					for ( Class<? extends Service> type : descriptor.getTypes() ) {
						if ( ! services.containsKey(type) ) {
							services.put(type, new HashMap<String,ServiceDescriptor<?>>());
						}
						services.get(type).put(descriptor.getName(), descriptor);
					}
					serviceNames.add(descriptor.getName());
				}
			} else if ( ! (singleton instanceof RESTHandler) ) {
				log.warn("Singleton of class "+singleton.getClass().getName()+" found doesn't implement Service");
			}
		}
	}
}
