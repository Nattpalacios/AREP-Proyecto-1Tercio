package edu.escuelaing.arep;

import java.io.IOException;

public class Controlador {
	
	public static void main(String[] args) {
		AppServer appS = new AppServer();
		try {
			appS.inicializar();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
