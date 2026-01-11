package com.skupina1.location.userLocation;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.skupina1.location.userRepo.UserRepo;

public class UserLocationTest {

    @Test
    public void testGenerateLocationsWithoutDB() {
        // Mock the repository
        UserRepo userRepo = mock(UserRepo.class);
        List<UserLocation> savedLocations = new ArrayList<>();

        // When addUserLocation is called, just store the object in a list
        try {
               doAnswer(invocation -> {
            UserLocation loc = invocation.getArgument(0);
            savedLocations.add(loc);
            return null;
        }).when(userRepo).addUserLocation(any(UserLocation.class));
        } catch (Exception e) {
        }
     

        // Generate random locations
        LocationGenerator generator = new LocationGenerator();
        List<UserLocation> locations = generator.generateLocations(5);

        // "Save" them using mocked repo
        for (UserLocation loc : locations) {
            try {
                
            userRepo.addUserLocation(loc);
            } catch (Exception e) {
                
            }
        }

        assert savedLocations.size() == 5;
        savedLocations.forEach(loc -> System.out.println("Generated location: " + loc.getLocation()));
    }
}
