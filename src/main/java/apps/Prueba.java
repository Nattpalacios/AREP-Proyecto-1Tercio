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
	
	@Web("/multiplicar")
	public static double multiplicar(String a, String b) {
		return Double.parseDouble(a) * Double.parseDouble(b);
	}
	
	@Web("/sumar")
	public static double sumar(String a, String b) {
		return Double.parseDouble(a) + Double.parseDouble(b);
	}

}
