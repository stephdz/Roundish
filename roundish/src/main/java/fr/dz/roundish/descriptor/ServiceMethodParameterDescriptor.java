package fr.dz.roundish.descriptor;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Repository method parameter descriptor
 */
@Getter
@AllArgsConstructor
public class ServiceMethodParameterDescriptor {
	private ServiceMethodDescriptor methodDescriptor;
	private Class<?> parameterClass;
	private String name;
}
