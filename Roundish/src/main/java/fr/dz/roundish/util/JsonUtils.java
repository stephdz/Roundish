package fr.dz.roundish.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.dz.roundish.descriptor.ServiceMethodDescriptor;
import fr.dz.roundish.descriptor.ServiceMethodParameterDescriptor;

/**
 * Utilities method class for JSON
 */
public abstract class JsonUtils {

	/**
	 * Convert json to method parameters
	 * @param json
	 * @return
	 */
	public static Object[] convertToParameters(String json, ServiceMethodDescriptor methodDescriptor) {
		List<Object> result = new ArrayList<Object>();
		Gson gson = new Gson();
		JsonObject parameters = gson.fromJson(json, JsonObject.class);
		if ( parameters != null ) {
			for ( ServiceMethodParameterDescriptor parameter : methodDescriptor.getServiceMethodParameterDescriptors()  ) {
				JsonElement value = parameters.get(parameter.getName());
				Object realValue = gson.fromJson(value, parameter.getParameterClass());
				result.add(realValue);
			}
			return result.toArray();
		} else {
			return null;
		}
	}
	
	/**
	 * Convert a JSON string to an entity from given class
	 * @param json
	 * @return
	 */
	public static <E> E convertToEntity(String json, Class<E> clazz) {
		return new Gson().fromJson(json, clazz);
	}
	
	/**
	 * Convert an entity to a JSON string
	 * @param entity
	 * @return
	 */
	public static String convertToJson(Object entity) {
		return new Gson().toJson(entity);
	}

	/**
	 * Convert an exception to a JSON string
	 * @param t
	 * @return
	 */
	public static String convertExceptionToJson(Throwable t) {
		return convertToJson(new JSONException(t));
	}
}
