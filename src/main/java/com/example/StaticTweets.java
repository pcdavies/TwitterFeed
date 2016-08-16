package com.example;


import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ChunkedOutput;

/**
 * Root resource (exposed at "statictweets" path)
 */
@Path("statictweets")
public class StaticTweets {
	
    private static SampleStreamExample example = new SampleStreamExample();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as an application/json response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {
    	
    	final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
    	
    	runTask(output,null);
    	return Response.ok()
    			.entity(output)
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
    			.build();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/json" media type.
     *
     * @return String that will be returned as an application/json response.
     */
    /* --- Remove this comment to allow for Filtered Searching 
    @Path("{search}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItWithCount(@PathParam("search") final String search) {
	    	final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
	    	runTask(output, search);
	    	return Response.ok()
	    			.entity(output)
	    			.header("Access-Control-Allow-Origin", "*")
	    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
	    			.build();

		
    }
	
    --- Remove this comment to allow for Filtered Searching */
    
	private void runTask(ChunkedOutput<String> output, String s) {
        new Thread(() -> {
            try {
            	example.runStaticTwitterStream(output, s); 
            } catch (IOException e) {
				e.printStackTrace();
            } finally {
            	if (output != null) {
	            	try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            }
        }).start();
    	// the output will be probably returned even before
        // a first chunk is written by the new thread
	}
    

}
