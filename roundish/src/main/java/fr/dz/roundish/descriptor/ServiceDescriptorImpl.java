package fr.dz.roundish.descriptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import fr.dz.roundish.Repository;
import fr.dz.roundish.Service;
import fr.dz.roundish.User;
import fr.dz.roundish.annotation.RESTMethod;
import fr.dz.roundish.annotation.RESTService;

/**
 * Service descriptor
 */
@Log4j
@Getter
public class ServiceDescriptorImpl<T extends Service> implements ServiceDescriptor<T> {
    @Getter(AccessLevel.PRIVATE)
    private final T service;
    private final String name;
    private final Set<Class<? extends Service>> types;
    private final Map<String, ServiceMethodDescriptor> methodDescriptors;

    /**
     * Constructor
     * 
     * @param service
     */
    public ServiceDescriptorImpl(final T service) {
	this.service = service;
	this.name = initName(service);
	this.types = initTypes(service);
	this.methodDescriptors = initMethodDescriptors(this);
    }

    @Override
    public ServiceMethodDescriptor getMethodDescriptor(final String methodName) {
	if (methodDescriptors.containsKey(methodName)) {
	    return methodDescriptors.get(methodName);
	} else {
	    throw new WebApplicationException("No method '" + methodName + "' for service '" + getName() + "'",
		    Response.Status.NOT_FOUND);
	}
    }

    @Override
    public T getService(final User user) {
	return service.forUser(user);
    }

    /**
     * Service name initialization
     * 
     * @param repository
     * @return
     */
    private static String initName(final Service service) {
	RESTService serviceAnnotation = service.getClass().getAnnotation(RESTService.class);
	if (serviceAnnotation == null) {
	    return null;
	} else {
	    return serviceAnnotation.value();
	}
    }

    /**
     * Service type initialization TODO Get rid of instanceof
     * 
     * @param service
     * @return
     */
    private static <T extends Service> Set<Class<? extends Service>> initTypes(final T service) {
	Set<Class<? extends Service>> types = new HashSet<Class<? extends Service>>();
	if (service instanceof Repository) {
	    types.add(Repository.class);
	}
	if (service instanceof Service) {
	    types.add(Service.class);
	}
	return types;
    }

    /**
     * Method descriptors initialization
     * 
     * @param serviceDescriptor
     * @return
     */
    private static <T extends Service> Map<String, ServiceMethodDescriptor> initMethodDescriptors(
	    final ServiceDescriptorImpl<T> serviceDescriptor) {
	Map<String, ServiceMethodDescriptor> result = new HashMap<String, ServiceMethodDescriptor>();
	for (Method method : serviceDescriptor.getService().getClass().getDeclaredMethods()) {
	    if (method.isAnnotationPresent(RESTMethod.class)) {
		if (result.containsKey(method.getName())) {
		    log.warn("Multiple method named " + method.getName() + " with " + RESTMethod.class.getSimpleName()
			    + " annotation in service " + serviceDescriptor.getName());
		} else {
		    result.put(method.getName(), new ServiceMethodDescriptor(serviceDescriptor, method));
		}
	    }
	}
	return result;
    }
}
