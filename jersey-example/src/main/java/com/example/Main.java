package com.example;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
	public static String BASE_URI;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() throws UnknownHostException {
       // Base URI the Grizzly HTTP server will listen on
    	 String port = System.getenv("PORT");
    	 port = (null != port) ? port : "8080";
			 String hostName = System.getenv("HOSTNAME");
			 hostName = (null != hostName) ? hostName : "localhost";
		   BASE_URI = "http://" + hostName + ":" + port + "/";

        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig().packages("com.example");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
     // Add the StaticHttpHandler to serve static resources from the static folder
//        server.getServerConfiguration().addHttpHandler(
//                new StaticHttpHandler("src/main/resources/static/"), "static/");

        // Add the CLStaticHttpHandler to serve static resources located at
        // the static folder from the jar file jersey-example-1.0-SNAPSHOT.jar
        server.getServerConfiguration().addHttpHandler(
                new CLStaticHttpHandler(new URLClassLoader(new URL[] {
                    new File("target/repo/com/example/jersey-example/1.0-SNAPSHOT/jersey-example-1.0-SNAPSHOT.jar").toURI().toURL()}), "static/"), "/jarstatic");
        System.out.println("In order to test the server please try the following urls:");
        System.out.println(String.format("%s to see the default resource", BASE_URI));
        System.out.println(String.format("%sapplication.wadl to see the WADL resource", BASE_URI));
        System.out.println(String.format("%stweets to see the JAX-RS resource", BASE_URI));
        System.out.println(String.format("%sstatic/index.html to see the static resource", BASE_URI));
        System.out.println(String.format("%sjarstatic/index.html to see the jar static resource", BASE_URI));
        System.out.println();
        System.out.println("Press enter to stop the server...");
        
        System.in.read();
        server.shutdown();
    }
}
