package fr.dz.roundish.descriptor;

import java.util.Set;

import fr.dz.roundish.Service;
import fr.dz.roundish.User;

/**
 * Service descriptor
 * 
 * @param <K>
 *            The service type
 */
public interface ServiceDescriptor<T extends Service> {

    /**
     * Return the service name
     * 
     * @return
     */
    public String getName();

    /**
     * Return the service
     * 
     * @return
     */
    public T getService(final User user);

    /**
     * Return the types of the service
     * 
     * @return
     */
    public Set<Class<? extends Service>> getTypes();

    /**
     * Return a method descriptor from its name
     * 
     * @param methodName
     * @return
     */
    public ServiceMethodDescriptor getMethodDescriptor(final String methodName);
}
