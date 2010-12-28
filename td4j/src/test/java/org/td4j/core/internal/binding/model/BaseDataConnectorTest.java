package org.td4j.core.internal.binding.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BaseDataConnectorTest {
	
	protected Field field(Class<?> cls, String fieldName) {
		try {
			return cls.getField(fieldName);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	protected Method method(Class<?> cls, String methodName, Class<?>... paramTypes) {
		if (methodName == null) return null;
		
		try {
			return cls.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}


}
