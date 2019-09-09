package edu.escuelaing.arep;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.midi.SysexMessage;

public class AppServer {
	
	private HashMap<String,Handler> hash;
	
	public void escuchar() throws IOException {
		
		while (true) {
			Server servidor = new Server();
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(servidor.getPort());
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + servidor.getPort());
                System.exit(1);
            }
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine = "";
            String op = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if(inputLine.contains("GET")) {
	                if(inputLine.contains("/apps/")) {
	                	String path = inputLine.split(" ")[1].split("/")[2];
	                	String[] a = path.split("="); 
	                	op = "/"+a[0];
	                	String[] parametros = a[1].split("&");
	                	outputLine = "HTTP/1.1 200 OK\r\n"
	                            + "Content-Type: text/html\r\n"
	                            + "\r\n"
	                            + "<!DOCTYPE html>\n"
	                            + "<html>\n"
	                            + "<head>\n"
	                            + "<meta charset=\"UTF-8\">\n"
	                            + "<title>Title of the document</title>\n"
	                            + "</head>\n"
	                            + "<body>\n"
	                            + "<h1>"
	                            + hash.get(op).ejecutar(parametros)
	                            +"</h1>\n"
	                            + "</body>\n"
	                            + "</html>\n";
                	}else if(inputLine.contains("/imagenes/")) {
                		String path = inputLine.split(" ")[1].split("/")[2];
                		String formatoFile = path.substring(path.indexOf(".") + 1);
                		String direccion = System.getProperty("user.dir") + "/imagenes/" + path;
                    	BufferedImage bI = ImageIO.read(new File(direccion));
                    	ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
                    	ImageIO.write(bI, formatoFile, byteArrayOutput);
                    	byte [] listaB = byteArrayOutput.toByteArray();
                    	DataOutputStream salida = new DataOutputStream(clientSocket.getOutputStream());
                    	salida.writeBytes("HTTP/1.1 200 OK \r\n");
                    	salida.writeBytes("Content-Type: image/" + formatoFile + "\r\n");
                    	salida.writeBytes("Content-Length: " + listaB.length);
                    	salida.writeBytes("\r\n\r\n");
                    	salida.write(listaB);
                    	salida.close();
            			out.println(salida.toString());
                	}else if(inputLine.contains("/recursosWeb/")) {
                		String path = inputLine.split(" ")[1].split("/")[2];
                		String direccion = "https://primerproyectoarep.herokuapp.com" + "/recursosWeb/" + path;
                		outputLine = "HTTP/1.1 200 OK\r\n"
	                            + "Content-Type: text/html\r\n"
	                            + "\r\n";
                		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(direccion),"UTF8"));
                		while (br.ready()) {
                            outputLine += br.readLine();
            			}
                		out.print(outputLine);
                        br.close();
                	}
                }
                if (!in.ready()) {
                    break;
                }
            }

            out.write(outputLine);

            out.close();

            in.close();

            clientSocket.close();

            serverSocket.close();
        }
		
	}
	
	public void inicializar() throws IOException {
		String sCarpAct = System.getProperty("user.dir")+"/src/main/java/apps";
		File carpeta = new File(sCarpAct);
		String[] clases = carpeta.list();
		hash = new HashMap<String,Handler>();
		for (int i = 0; i < clases.length; i++) {
			String ruta = "apps." + clases[i].substring(0,clases[i].indexOf("."));
			cargar(ruta);
		}
		escuchar();
	}
	
	public void cargar(String classpath) {
		try {
			Class c = Class.forName(classpath);
			Method[] metodos = c.getDeclaredMethods();
			for(Method m : metodos) {
				if (m.getAnnotation(Web.class) != null) {
					hash.put(m.getAnnotation(Web.class).value(), new URLHandler(m));
				}		
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
