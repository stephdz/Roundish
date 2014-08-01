package fr.dz.roundish.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import fr.dz.roundish.Repository;
import fr.dz.roundish.ServiceException;
import fr.dz.roundish.User;
import fr.dz.roundish.annotation.RESTMethod;
import fr.dz.roundish.annotation.RESTParameter;
import fr.dz.roundish.annotation.RESTService;
import fr.dz.roundish.model.Todo;


/**
 * Todo repository
 */
@RESTService("todos")
public class TodoRepository extends Repository<Long,Todo> {
    private Map<User,Map<Long, Todo>> repository = new ConcurrentHashMap<User,Map<Long, Todo>>();
    private AtomicLong counter = new AtomicLong(1);
 
    @Override
	public List<Todo> list() {
    	Map<Long,Todo> repository = getCurrentRepository();
    	if ( repository != null ) {
    		return new ArrayList<Todo>(repository.values());
    	} else {
    		return new ArrayList<Todo>();
    	}
    }
 
    @Override
	public Todo get(Long id) throws ServiceException {
        return getCurrentRepository().get(id);
    }
 
    @Override
	public Long create(Todo todo) throws ServiceException {
		long id = counter.getAndIncrement();
	    todo.setId(id);
	    getCurrentRepository().put(todo.getId(), todo);
        return id;
    }
 
    @Override
	public void update(Long id, Todo todo) throws ServiceException {
    	todo.setId(id);
    	getCurrentRepository().put(id, todo);
    }
 
    @Override
	public void delete(Long id) throws ServiceException {
    	throw new ServiceException("TodoRepository.delete.serviceError", "Erreur du Service TodoRepository.delete");
        //repository.remove(id);
    }
    
    @RESTMethod
    public String sayHello() {
    	return "Hello !!!!";
    }
    
    @RESTMethod
    public String sayHelloTo(@RESTParameter("name") String name) {
    	return "Hello "+name+" !!!!";
    }
    
    @RESTMethod
    public Todo ping(@RESTParameter("todo") Todo todo, @RESTParameter("display") String display) {
    	System.out.println(display);
    	return todo;
    }
    
    /**
     * Return repository of current connected user
     * @return
     */
    private Map<Long,Todo> getCurrentRepository() {
    	User user = getConnectedUser();
    	if ( user == null ) {
    		return null;
    	} else {
	    	if ( ! repository.containsKey(user) ) {
	    		repository.put(user, new ConcurrentSkipListMap<Long, Todo>());
	    	}
	    	return repository.get(user);
    	}
    }
}
