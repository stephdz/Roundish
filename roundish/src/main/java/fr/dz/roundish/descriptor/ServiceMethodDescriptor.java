package fr.dz.roundish.descriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import lombok.Getter;
import fr.dz.roundish.ServiceException;
import fr.dz.roundish.User;
import fr.dz.roundish.annotation.RESTParameter;

/**
 * Service method descriptor
 */
@Getter
public class ServiceMethodDescriptor {
    private final ServiceDescriptor<?> serviceDescriptor;
    private final Method method;
    private final List<ServiceMethodParameterDescriptor> serviceMethodParameterDescriptors;

    /**
     * Constructor
     * 
     * @param serviceDescriptor
     * @param method
     */
    public ServiceMethodDescriptor(final ServiceDescriptor<?> serviceDescriptor, final Method method) {
	this.serviceDescriptor = serviceDescriptor;
	this.method = method;
	this.serviceMethodParameterDescriptors = initParameters(this);
    }

    /**
     * Execute the method using JSON parameters
     * 
     * @param parametersJSON
     * @return
     * @throws ServiceException
     */
    public Object execute(final User user, final Object... parameters) throws ServiceException {
	try {
	    return method.invoke(serviceDescriptor.getService(user), parameters);
	} catch (InvocationTargetException e) {
	    if (e.getCause() instanceof ServiceException) {
		throw (ServiceException) e.getCause();
	    } else {
		throw new WebApplicationException("Error while invoking method " + method.getName() + " from service "
			+ serviceDescriptor.getName(), e);
	    }
	} catch (Exception e) {
	    throw new WebApplicationException("Error while invoking method " + method.getName() + " from service "
		    + serviceDescriptor.getName(), e);
	}
    }

    /**
     * Initialize the parameters descriptors
     * 
     * @param methodDescriptor
     * @return
     */
    private static List<ServiceMethodParameterDescriptor> initParameters(final ServiceMethodDescriptor methodDescriptor) {
	List<ServiceMethodParameterDescriptor> result = new ArrayList<ServiceMethodParameterDescriptor>();

	// For each method parameter
	for (int i = 0; i < methodDescriptor.getMethod().getParameterTypes().length; i++) {
	    Class<?> parameterClass = methodDescriptor.getMethod().getParameterTypes()[i];
	    Annotation[] parameterAnnotations = methodDescriptor.getMethod().getParameterAnnotations()[i];

	    // Search for the @RESTParameter annotation
	    RESTParameter parameterAnnotation = null;
	    for (Annotation annotation : parameterAnnotations) {
		if (annotation instanceof RESTParameter) {
		    parameterAnnotation = (RESTParameter) annotation;
		    break;
		}
	    }

	    // Not found : error
	    if (parameterAnnotation == null) {
		throw new WebApplicationException("Parameter " + i + " of method "
			+ methodDescriptor.getMethod().toGenericString() + " has no @"
			+ RESTParameter.class.getSimpleName() + " annotation", Response.Status.INTERNAL_SERVER_ERROR);
	    }
	    // Store the parameter
	    else {
		result.add(new ServiceMethodParameterDescriptor(methodDescriptor, parameterClass, parameterAnnotation
			.value()));
	    }
	}

	return result;
    }
}
