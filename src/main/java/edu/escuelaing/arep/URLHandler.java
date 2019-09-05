package edu.escuelaing.arep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class URLHandler implements Handler{

	private Method metodo;
	
	public URLHandler(Method m) {
		metodo = m;
	}
	
	@Override
	public Object ejecutar(String[] args) {
		try {
			return metodo.invoke(null, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	

}
