package apps;

import edu.escuelaing.arep.Web;

public class Prueba {
	
	@Web("/cuadrado")
	public static double cuadrado(String a) {
		return Math.pow(Double.parseDouble(a), 2);
	}
	
	@Web("/cuadradoTest")
	public static String cuadrado() {
		return "En proceso";
	}

}