
package com.skupina1.location.userResource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/location/health")
public class HealthCheckResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok()
            .entity("{\"status\":\"UP\",\"service\":\"rateuser\"}")
            .build();
    }
    
    @GET
    @Path("/ready")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ready() {
        // Add database connectivity check
        boolean dbHealthy = checkDatabase();
        
        if (dbHealthy) {
            return Response.ok()
                .entity("{\"status\":\"READY\"}")
                .build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("{\"status\":\"NOT_READY\"}")
                .build();
        }
    }
    
    @GET
    @Path("/live")
    @Produces(MediaType.APPLICATION_JSON)
    public Response live() {
        return Response.ok()
            .entity("{\"status\":\"ALIVE\"}")
            .build();
    }
    
    private boolean checkDatabase() {
        // Implement your database check
        try {
            // Example: simple query to verify connection
            // entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}