package model;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private String name;
    private Location nextStop;
    private Location previousStop;
    private List<Passanger> passangers=new ArrayList<Passanger>();

    public Location(String name) {
        this.name = name;
    }

    public Location(String name, Location nextStop, Location previousStop) {
        this.name = name;
        this.nextStop = nextStop;
        this.previousStop = previousStop;
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

    public List<Passanger> getPassangers() {
        return passangers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreviousStop(Location previousStop) {
        this.previousStop = previousStop;
    }

    public void setPassangers(List<Passanger> passangers) {
        this.passangers = passangers;
    }

    public void setNextStop(Location nextStop) {
        this.nextStop = nextStop;
    }
    public void setPreviousAndNextStop(Location previousStop, Location nextStop) {
        this.previousStop = previousStop;
        this.nextStop = nextStop;
    }

    public void addPassenger(Passanger p){
        this.passangers.add(p);
    }
    @Override
    public String toString() {
        return "\nLocation{" +
                "name='" + name + '\'' +
                ", nextStop=" + nextStop.getName() +
                ", previousStop=" + previousStop.getName() +
                ", passangers=" + passangers +
                '}';
    }
}
