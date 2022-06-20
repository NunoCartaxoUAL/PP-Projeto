package model;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private String name;
    private Location northStop; //north stop(location) in the route
    private Location southStop; //south stop(location) in the route
    private double northDistance;
    private double southDistance;
    private List<Passenger> passengers =new ArrayList<Passenger>();

    public Location(String name, double northDistance, double southDistance) {
        this.name = name;
        this.northDistance = northDistance;
        this.southDistance = southDistance;
    }

    public String getName() {
        return name;
    }

    public Location getSouthStop() {
        return southStop;
    }

    public Location getNorthStop() {
        return northStop;
    }

    public List<Passenger> getPassangers() {
        return passengers;
    }

    public double getNorthDistance() {
        return northDistance;
    }

    public double getSouthDistance() {
        return southDistance;
    }

    public void setSouthAndNorthStop(Location southStop, Location northStop) { //adds both south and north city at the same time
        this.southStop = southStop;
        this.northStop = northStop;
    }

    public void addPassenger(Passenger p){
        this.passengers.add(p);
    }
}
