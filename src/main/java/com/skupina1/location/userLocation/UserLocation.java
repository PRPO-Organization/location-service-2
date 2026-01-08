package com.skupina1.location.userLocation;

import java.util.Date;

import org.locationtech.jts.geom.Point;

import com.skupina1.location.converter.GeometryConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "UserLocation.findDistance",
                query = "SELECT ST_Distance(" +
                        "ST_SetSRID(ST_MakePoint(?1, ?2), 4326)::geography, " +
                        "ST_SetSRID(ST_MakePoint(?3, ?4), 4326)::geography)"
        ),
        @NamedNativeQuery(
                name="UserLocation.findNearestLocation",
    query = "select * from public.user_locations " +
    "where is_driver = true " +
    "order BY ST_DISTANCE(location::geography, ST_SetSRID(ST_MakePoint(?1,?2),4326)::geography) " +
    "LIMIT 5",
            resultClass = UserLocation.class
        ) 
})
@NamedQueries({
        @NamedQuery(
                name = "UserLocation.findByUserId",
                query = "select u from UserLocation u where u.id = :id"
        )

})
@Table(name="user_locations")
public class UserLocation{
    @Id
    private Long id;
    @Convert(converter = GeometryConverter.class)
    @Column(name="location",columnDefinition = "geography")
    private Point location;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name="is_driver")
    private boolean isDriver;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    public UserLocation(){

    }
    public UserLocation( Point location , Long id){
        this.location = location;
        this.id = id;
    }
    public UserLocation( Point location , Long id , Boolean isDriver){
        this.location = location;
        this.id = id;
        this.isDriver  = isDriver;
    }
    public void setLocation(Point location  ) {
        this.location = location;
    }
    public Point getLocation() {
        return location;
    }
    public Long getId() {
        return id;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt (Date updatedAt){
        this.updatedAt = updatedAt;
    }
    public void setIsDriver(Boolean isDriver){
        this.isDriver = isDriver ; 
    }
    public Boolean getIsDriver(){
        return this.isDriver  ; 
    }

}