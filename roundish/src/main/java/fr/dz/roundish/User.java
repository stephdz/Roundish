package fr.dz.roundish;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Describe a user
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of="login")
public class User {
	private String login;
	private String password;
	private String fullName;
	private Locale locale;
}
