package fr.dz.roundish;

import java.util.List;

import lombok.Getter;
import fr.dz.roundish.util.ReflectionUtils;

/**
 * Interface to a repository containing entities
 * @param <K> The key type
 * @param <E> The entity type
 */
@Getter
public abstract class Repository<K,E> extends Service {
	private Class<E> keyClass;
	private Class<E> entityClass;
	
	/**
	 * Constructor
	 */
	public Repository() {
		this.keyClass = ReflectionUtils.getParameterizedClass(getClass(), 0);
		this.entityClass = ReflectionUtils.getParameterizedClass(getClass(), 1);
	}

	/**
	 * Find all entities from the repository
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<E> list() throws ServiceException;
	
	/**
	 * Create a new entity into the repository
	 * @param entity
	 * @throws ServiceException
	 */
	public abstract K create(E entity) throws ServiceException;
	
	
	/**
	 * Update an entity from the repository
	 * @param entity
	 * @throws ServiceException
	 */
	public abstract void update(K key, E entity) throws ServiceException;
	
	/**
	 * Get an entity from the repository
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public abstract E get(K key) throws ServiceException;
	
	/**
	 * Delete an entity from the repository
	 * @param key
	 * @throws ServiceException
	 */
	public abstract void delete(K key) throws ServiceException;
}
