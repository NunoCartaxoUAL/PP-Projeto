package model;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private String name;
    private Location nextStop;
    private double nextDistance;
    private double previousDistance;
    private Location previousStop;
    private List<Passenger> passengers =new ArrayList<Passenger>();

    public Location(String name, double nextDistance, double previousDistance) {
        this.name = name;
        this.nextDistance = nextDistance;
        this.previousDistance = previousDistance;
    }

    public String getName() {
        return name;
    }

    public Location getPreviousStop() {
        return previousStop;
    }

    public Location getNextStop() {
        return nextStop;
    }

    public List<Passenger> getPassangers() {
        return passengers;
    }

    public double getNextDistance() {
        return nextDistance;
    }

    public double getPreviousDistance() {
        return previousDistance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreviousStop(Location previousStop) {
        this.previousStop = previousStop;
    }

    public void setPassangers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void setNextStop(Location nextStop) {
        this.nextStop = nextStop;
    }

    public void setPreviousAndNextStop(Location previousStop, Location nextStop) {
        this.previousStop = previousStop;
        this.nextStop = nextStop;
    }

    public void addPassenger(Passenger p){
        this.passengers.add(p);
    }
    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", nextStop=" + nextStop.getName() +
                ", previousStop=" + previousStop.getName() +
                ", passengers=" + passengers +
                '}'+"\n";
    }
}
