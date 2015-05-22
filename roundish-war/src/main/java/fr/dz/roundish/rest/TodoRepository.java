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
public class TodoRepository extends Repository<Long, Todo> {
    private final Map<User, Map<Long, Todo>> repository = new ConcurrentHashMap<User, Map<Long, Todo>>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public List<Todo> list() {
	Map<Long, Todo> repository = getCurrentRepository();
	if (repository != null) {
	    return new ArrayList<Todo>(repository.values());
	} else {
	    return new ArrayList<Todo>();
	}
    }

    @Override
    public Todo get(final Long id) throws ServiceException {
	return getCurrentRepository().get(id);
    }

    @Override
    public Long create(final Todo todo) throws ServiceException {
	long id = counter.getAndIncrement();
	todo.setId(id);
	getCurrentRepository().put(todo.getId(), todo);
	return id;
    }

    @Override
    public void update(final Long id, final Todo todo) throws ServiceException {
	todo.setId(id);
	getCurrentRepository().put(id, todo);
    }

    @Override
    public void delete(final Long id) throws ServiceException {
	throw new ServiceException("TodoRepository.delete.serviceError", "Erreur du Service TodoRepository.delete");
	// repository.remove(id);
    }

    @RESTMethod
    public String sayHello() {
	return "Hello !!!!";
    }

    @RESTMethod
    public String sayHelloTo(@RESTParameter("name") final String name) {
	return "Hello " + name + " !!!!";
    }

    @RESTMethod
    public Todo ping(@RESTParameter("todo") final Todo todo, @RESTParameter("display") final String display) {
	System.out.println(display);
	return todo;
    }

    /**
     * Return repository of current connected user
     * 
     * @return
     */
    private Map<Long, Todo> getCurrentRepository() {
	User user = getConnectedUser();
	if (user == null) {
	    return null;
	} else {
	    if (!repository.containsKey(user)) {
		repository.put(user, new ConcurrentSkipListMap<Long, Todo>());
	    }
	    return repository.get(user);
	}
    }
}
