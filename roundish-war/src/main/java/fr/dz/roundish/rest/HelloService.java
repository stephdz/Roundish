package fr.dz.roundish.rest;

import fr.dz.roundish.Service;
import fr.dz.roundish.ServiceException;
import fr.dz.roundish.annotation.RESTMethod;
import fr.dz.roundish.annotation.RESTParameter;
import fr.dz.roundish.annotation.RESTService;


/**
 * Hello Service
 */
@RESTService("hello")
public class HelloService extends Service {

	@RESTMethod
	public String sayHello(@RESTParameter("name") String name) {
		return "Hello "+name+" !!!";
	}
	
	@RESTMethod
	public void throwException() throws ServiceException {
		throw new ServiceException("That's an exception !");
	}
}
