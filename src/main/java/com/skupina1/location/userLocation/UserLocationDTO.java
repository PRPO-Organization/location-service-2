package com.skupina1.location.userLocation;

import java.util.Date;

//class that contains the
//id --> long
//user_id -->string (mongodb objectId)
//lat , lng --> double
//used in the GET and PUT request
//use it to fetch the data in a DTO object
public class  UserLocationDTO extends LocationDTO{
    private long id ;
    Date createdAt;
    Date updatedAt;
    public UserLocationDTO(){
    }
    public UserLocationDTO(long id  ,  double lng, double lat, Date createdAt, Date updatedAt , Boolean isDriver) {
        super(lng,lat , isDriver);
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

   
  
}