package com.skupina1.location.userLocation;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
public class LocationGenerator {

    public LocationGenerator() {
    }
    
    private static final Random rand = new Random();
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    public static double getRandomLatitude() {
        // Latitude: -90 to +90
        return -90 + 180 * rand.nextDouble();
    }

    public static double getRandomLongitude() {
        // Longitude: -180 to +180
        return -180 + 360 * rand.nextDouble();
    }

    
   public List<UserLocation> generateLocations(int n) {
       List<UserLocation> userLocations = new ArrayList<>();
       for (int i = 0; i < n; i++) {
           double lat = getRandomLatitude();
           double lon = getRandomLongitude();
           Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
           UserLocation userLocation = new UserLocation(point , 1L,false);
           userLocations.add(userLocation);
       }
       return userLocations;
   }
}