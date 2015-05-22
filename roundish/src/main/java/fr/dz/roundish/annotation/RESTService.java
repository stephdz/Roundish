package fr.dz.roundish.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;

/**
 * Annotation for Roundish service
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod(HttpMethod.GET)
@Documented
public @interface RESTService {
    /**
     * Specifies the path to HTTP repository.
     */
    String value();
}
