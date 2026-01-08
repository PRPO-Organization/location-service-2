package com.skupina1.location.userResource;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import com.skupina1.location.userLocation.LocationDTO;
import com.skupina1.location.userLocation.UserLocation;
import com.skupina1.location.userLocation.UserLocationDTO;
import com.skupina1.location.userRepo.DistanceDTO;
import com.skupina1.location.userRepo.UserRepo;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//rest api for the location api
@Path("location")
public class UserLocationResource {
    //endpoint which fetches the current location of the user
    @Inject
    UserRepo userRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getLocation(@PathParam("id") Long id ) {
        if (id == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("id must not be null")
                    .build();
        }
        List<UserLocation> currentLocations;
        try {
            currentLocations = userRepo.findLocationByUserId(id);
            if (currentLocations.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("not found")
                        .build();
            }
            if (currentLocations.size() != 1) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Only one location can be found")
                        .build();
            }
            UserLocation currentLocation = currentLocations.get(0);
            UserLocationDTO userLocationDTO = new UserLocationDTO(id,currentLocation.getLocation().getX(), currentLocation.getLocation().getY(),currentLocation.getCreatedAt(),currentLocation.getUpdatedAt(),currentLocation.getIsDriver());
            return Response.ok(userLocationDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("")
                    .build();
        }

    }

    //endpoint which uploads the location of a new user
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response postLocation(@PathParam("id") Long id, LocationDTO userLocation) {
        //System.out.println("POST req");
        if (userLocation == null || id == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please input location id and userLocation")
                    .build();
        }
    
        List<UserLocation> locations = userRepo.findLocationByUserId(id);
        if (!locations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("location already exists")
                    .build();
        }

        Date currentTimestamp = new Date() ;
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = gf.createPoint(new Coordinate(userLocation.getLng(), userLocation.getLat()));
        UserLocation currentLocation = new UserLocation( point , id , userLocation.getIsDriver());
        try {
            currentLocation.setCreatedAt(currentTimestamp);
            currentLocation.setUpdatedAt(currentTimestamp);
            currentLocation = userRepo.addUserLocation(currentLocation);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to save location: " + e.getMessage())
                    .build();
        }

        UserLocationDTO userLocationDTO = new UserLocationDTO(currentLocation.getId(), userLocation.getLat(), userLocation.getLng() , currentTimestamp , currentTimestamp , userLocation.getIsDriver());
        return Response.ok(userLocationDTO).build();
    }

    //endpoint which changes the location of an existing user
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response patchLocation(@PathParam("id") Long id, LocationDTO userLocation) {
        if (id == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("id is required")
                    .build();
        }
        try {
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            Point point = gf.createPoint(new Coordinate(userLocation.getLng(), userLocation.getLat()));
            boolean exists = userRepo.changeLocation(id , point);
            if (!exists) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("user location does not exist")
                        .build();
            }
            return Response.ok(userLocation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        }

    }

    //change the location of the user
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response putLocation(@PathParam("id") Long id, UserLocationDTO userLocation) {
        if (id == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("id is required")
                    .build();
        }
        try {
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            Point point = gf.createPoint(new Coordinate(userLocation.getLng(), userLocation.getLat()));
            boolean exists = userRepo.changeLocation(id, point);
            if (!exists) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("user location does not exist")
                        .build();
            }
            return Response.ok(userLocation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid location id " + e.getMessage())
                    .build();
        }

    }
    //to find the location between two points use the userId as a
    // path parameter and the target user object id as a query parameter
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/distance/{id}")
    public Response getDistance(
            @QueryParam("dest") Long dest,
            @PathParam("id") Long id
    ){
        if(dest==null||id==null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("dest or id is null")
                    .build();
        }
        //find user location
        UserLocation userLocation = userRepo.getUserLocation(id);
        if  (userLocation == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("user location does not exist")
                    .build();
        }
        UserLocation destLocation = userRepo.getUserLocation(dest);
        if (destLocation == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("user location does not exist")
                    .build();
        }
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point loc1 = userLocation.getLocation();
        Point loc2 = destLocation.getLocation();
        Double distance = userRepo.findDistance(loc1, loc2);
        if (distance == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("distance is null")
                    .build();
        }
        //find the dest location
        DistanceDTO distanceDTO = new DistanceDTO(distance);
        //convert to points and use the query in userRepo to get the distance
        return Response.ok(distanceDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/nearest/{id}")
    public Response nearestDrivers(
            @PathParam("id") Long id , 
            LocationDTO currentLocation 
    ){
        if(currentLocation ==null||id==null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("dest or id is null")
                    .build();
        }
        try {
            UserLocation userLocation = userRepo.getUserLocation(id);
            if (userLocation == null ){
                return Response.status(Response.Status.NOT_FOUND)   
                    .entity("location not found")
                    .build();
            }
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            Point point = gf.createPoint(new Coordinate(currentLocation.getLng(),currentLocation.getLat()));
            List<UserLocation> nearestDrivers = userRepo.findNearestUsers(point);
            List<UserLocationDTO> nearestDriversDTO = nearestDrivers.stream()
                .map(UserLocationDTO::new)
                .collect(Collectors.toList());
            return Response.ok(nearestDriversDTO).build();
        }catch(Exception exception){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exception)
                .build(); 
        }
        
       
}
}

