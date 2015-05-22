package fr.dz.roundish;

import javax.ws.rs.WebApplicationException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Interface for services
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class Service implements Cloneable {
    private User connectedUser;

    /**
     * Specialize a service for a given user
     * 
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Service> T forUser(final User user) {
	try {
	    T clone = (T) super.clone();
	    clone.setConnectedUser(user);
	    return clone;
	} catch (CloneNotSupportedException e) {
	    throw new WebApplicationException(e);
	}
    }
}
