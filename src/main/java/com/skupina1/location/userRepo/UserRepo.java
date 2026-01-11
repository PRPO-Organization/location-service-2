package com.skupina1.location.userRepo;
import java.util.Date;
import java.util.List;

import org.locationtech.jts.geom.Point;

import com.skupina1.location.userLocation.UserLocation;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public  class UserRepo {

    @PersistenceContext(unitName = "JPADatasourceExamplePU")
    private EntityManager em;
    
    public UserRepo() {
    }
    @Transactional
    public UserLocation addUserLocation(UserLocation userLocation) throws Exception {
        try{
            em.persist(userLocation);
            em.flush();
            return userLocation;
        }catch(Exception e){
            throw new Exception(e);
        }
    }
    //function to update the user location
    @Transactional
    public boolean changeLocation(Long id, Point newLocation) throws Exception {
        if (id == null) {
            return false;
        }
        try{
            List<UserLocation> userLocations = this.findLocationByUserId(id);
            if (userLocations.isEmpty()){
                return false ;
            }
            if(userLocations.size()!=1){
                return false ;
            }
            Date currentTimeStamp = new Date() ;
            UserLocation userLocation = userLocations.get(0);
            userLocation.setUpdatedAt( currentTimeStamp );
            userLocation.setLocation(newLocation);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserLocation> findLocationByUserId(Long id){
        if (id == null){
            return null;
        }
        return  em. createNamedQuery("UserLocation.findByUserId",UserLocation.class)
                .setParameter("id",id)
                .getResultList();

    }
    public UserLocation getUserLocation(Long id){
        if (id == null){
            return null;
        }
        List<UserLocation> userLocations = this.findLocationByUserId(id);
        if(userLocations.isEmpty()){
            return null;
        }
        if (userLocations.size()!=1){
            return null;
        }
        return userLocations.get(0);
    }

    //function to find the closest neighbour using a native named query
    public List<UserLocation> findNearestUsers(Point userLocation){
        return em.createNamedQuery("UserLocation.findNearestLocation",UserLocation.class)
                .setParameter(1,userLocation.getX())
                .setParameter(2,userLocation.getY())
                .getResultList();
    }

    //find the distance between two points in space
    public Double findDistance(Point p1 , Point p2){
        Number distance = (Number) em.createNamedQuery("UserLocation.findDistance")
                .setParameter(1, p1.getX())  // lng1
                .setParameter(2, p1.getY())  // lat1
                .setParameter(3, p2.getX())  // lng2
                .setParameter(4, p2.getY())  // lat2
                .getSingleResult();
        return distance.doubleValue();

    }
    public void setEm(EntityManager em){
        this.em = em ;
    }
   
}