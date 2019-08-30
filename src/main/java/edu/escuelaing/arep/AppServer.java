package edu.escuelaing.arep;

import java.lang.reflect.Method;

public class AppServer {
	
	public void escuchar() {
		
	}
	
	public void inicializar() {
		try {
			Class c = Class.forName("apps.Prueba");
			System.out.println(c.getMethods().length);

			Method m = c.getDeclaredMethod("pagina", null);
			System.out.println(m.invoke(null,null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
