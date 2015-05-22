package fr.dz.roundish.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utilities method class for reflection
 */
public abstract class ReflectionUtils {

    /**
     * Return the <code>index</code> th parameterized type from class
     * <code>clazz</code>
     * 
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getParameterizedClass(final Class<?> clazz, final int index) {
	Class<T> result;
	Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[index];
	if (type instanceof Class) {
	    result = (Class<T>) type;
	} else if (type instanceof ParameterizedType) {
	    ParameterizedType parameterizedType = (ParameterizedType) type;
	    result = (Class<T>) parameterizedType.getRawType();
	} else {
	    result = null;
	}
	return result;
    }
}
