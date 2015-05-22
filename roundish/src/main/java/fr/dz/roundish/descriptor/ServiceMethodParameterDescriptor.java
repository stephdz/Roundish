package fr.dz.roundish.descriptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Repository method parameter descriptor
 */
@Getter
@AllArgsConstructor
public class ServiceMethodParameterDescriptor {
    private final ServiceMethodDescriptor methodDescriptor;
    private final Class<?> parameterClass;
    private final String name;
}
