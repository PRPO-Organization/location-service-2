package com.skupina1.location.userLocation;

//DTO to store the coordinates of the person
//used in PATCH request
//lng --> double
//lat --> double
public class LocationDTO {
    private double lng;
    private double lat;
    private Boolean isDriver ; 
    public LocationDTO(double lng, double lat , Boolean isDriver) {
        this.lng = lng;
        this.lat = lat;
        this.isDriver = isDriver ; 
    }
    public LocationDTO(){

    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public Boolean getIsDriver(){
        return this.isDriver;
    }
    public void setIsDriver(Boolean isDriver){
        this.isDriver = isDriver ; 
    }
}