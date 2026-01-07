package com.skupina1.location.userLocation;

import jakarta.persistence.*;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import com.skupina1.location.converter.GeometryConverter;
import org.locationtech.jts.geom.Point;

import java.util.Date;


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
                query = "select * from public.user_locations "+
                "order BY ST_DISTANCE(location::geography , ST_SetSRID(ST_MakePoint(:lng,:lat),4326)::geography) "+
                "LIMIT 1",
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = GeometryConverter.class)
    @Column(name="location",columnDefinition = "geography")
    private Point location;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    public UserLocation(){

    }
    public UserLocation( Point location){
        this.location = location;
    }
    public void setLocation(Point location) {
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

}