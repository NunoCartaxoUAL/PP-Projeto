package model;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private String name;
    private Location northStop;
    private double northDistance;
    private double southDistance;
    private Location southStop;
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

    public void setSouthAndNorthStop(Location southStop, Location northStop) {
        this.southStop = southStop;
        this.northStop = northStop;
    }

    public void addPassenger(Passenger p){
        this.passengers.add(p);
    }
    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", northStop=" + northStop.getName() +
                ", southStop=" + southStop.getName() +
                ", passengers=" + passengers +
                '}'+"\n";
    }
}
