package apps;

import edu.escuelaing.arep.Web;

public class Prueba2 {
	
	@Web("/sumar2")
	public static double sumar(String a, String b) {
		return Double.parseDouble(a) + Double.parseDouble(b);
	}

}
