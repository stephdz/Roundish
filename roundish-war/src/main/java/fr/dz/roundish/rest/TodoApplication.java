package fr.dz.roundish.rest;

import java.util.Locale;

import javax.ws.rs.ApplicationPath;

import fr.dz.roundish.Application;
import fr.dz.roundish.ServiceException;
import fr.dz.roundish.User;


/**
 * Todo Roundish Application
 */
@ApplicationPath("rest")
public class TodoApplication extends Application {

	@Override
	public User login(String login, String password) throws ServiceException {
		return new User(login, password, login, Locale.FRANCE);
	}
}
