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
@EqualsAndHashCode(of = "login")
public class User {
    private final String login;
    private final String password;
    private final String fullName;
    private final Locale locale;
}
