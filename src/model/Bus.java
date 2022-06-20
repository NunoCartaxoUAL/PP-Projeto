package model;

import java.util.*;

public class Bus extends Thread{

    private final List<Passenger> passengers = new ArrayList<>();
    private final String busID;
    private final int capacity;
    private double speed;
    private final String type;
    private Location location;
    private String direction;
    private List<String> tasks = new ArrayList<>();     //array of tasks the bus needs to complete in order
    private boolean running = true;                    // boolean to stop the thread after a task if needed
    private double percentageToDestination =0.0;
    private String status = "Normal";
    private double distance = 0;

    public Bus(int capacity, double speed, String type, String busID,Location location,String direction) {
        this.capacity = capacity;
        this.speed = speed;
        this.type = type;
        this.busID = busID;
        this.location = location;
        this.direction = direction;
        this.tasks.add(0,"busStop");

    }

    public String getDirection() {
        return direction;
    }

    public int getNumOfPassangers(){
        return this.passengers.size();
    }

    public double getPercentageToDestination() {
        return percentageToDestination;
    }

    public Location getLocation() {
        return location;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getBusID() {
        return busID;
    }

    public Location getPastLocation(){ //retrieves the past location to display in the GUI after the simulation has started
        return switch (this.direction) {
            case "north" -> this.location.getSouthStop();
            case "south" -> this.location.getNorthStop();
            default -> null;
        };

    }

    private void driving(){  //calculates next stop and does the waiting for the expected driving time
        nextStop(); //Calculate the next stop

        if(this.direction.matches("north")){
            distance = this.location.getSouthDistance(); // get next stop north distance
        }else{
            distance = this.location.getNorthDistance();// get next stop south distance
        }

        for (int i = 1; i <=distance; i++) { //updates the percentage to destination and waits for the apropriate time

            var timePerKm = 1/speed; //calculates time per killometer
            var milliseconds = timePerKm*60*60*1000/1000; //calculates the time / by 1000
            var percentage = (i/distance)*100;
            milliseconds = 10;
            percentageToDestination = percentage;
            try {
                sleep((long) milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void unloadPassangers(){ //unloads Passengers and deletes them from the bus because they have reached their destination
        if (passengers.size()>0){
            for (int i = 0; i < passengers.size(); i++) {
                var p= passengers.get(i);
                if (p.getDestination().matches(this.location.getName())){
                    i--;    // sets the loop back once because the list will be 1 value shorter after the removal
                    p.setArrived(true);
                    passengers.remove(p);
                }
            }
        }
    }
    private void loadPassangers(){

        synchronized (this.location){ // lock the variable location so that no bus can change it while this one is loading passengers
            var stopPassangers = this.location.getPassangers();
            var passagerSize = stopPassangers.size();
            if (stopPassangers.size()>0){
                for (int i = 0; (i < passagerSize) && (passengers.size()<capacity); i++) {  // loops until either the capacity of the bus is reached (passengers.size()<capacity) or the location doesnt have any more passengers (i < passagerSize)
                    Passenger passenger = stopPassangers.get(0); // get first passenger in location list
                    stopPassangers.remove(0);  // remove that passenger from the list
                    passengers.add(0, passenger);// add the passenger to the bus passenger list
                }
            }
        }
    }


    private void loadPassangersExpresso() { // exactly the same function as loadPassengers except it is
                                            // used only by expresso type busses and doesnt let passengers with
                                            // incompatible destinations enter (Cascais or Coimbra)

        synchronized (this.location){       // lock the variable location so that no bus can change it while this one is loading passengers
            var stopPassangers = this.location.getPassangers();
            var passagerSize = stopPassangers.size();
            var skipPassanger = 0 ;  //variable to skip a passenger in case his destination isnt compatible with the bus
            if (stopPassangers.size()>0){
                for (int i = 0; (i < passagerSize) && (passengers.size()<capacity); i++) {
                    Passenger passenger = stopPassangers.get(skipPassanger);
                    if (passenger.getDestination().matches("Cascais") ||passenger.getDestination().matches("Coimbra")){
                        skipPassanger++; //variable to skip a passenger in case his destination isnt compatible with the bus
                    }else{
                        stopPassangers.remove(skipPassanger);
                        passengers.add(0, passenger);//synchronize
                    }
                }
            }
        }
    }
    private void nextStop(){ //calculates next stop with the current stop and the direction of travel

        if ("north".equals(this.direction)) {
            if (this.location == this.location.getNorthStop()) {
                this.direction = "south";
                this.location = this.location.getSouthStop();
            } else {
                this.location = this.location.getNorthStop();
            }
        } else if ("south".equals(this.direction)) {
            if (this.location == this.location.getSouthStop()) {
                this.direction = "north";
                this.location = this.location.getNorthStop();
            } else {
                this.location = this.location.getSouthStop();
            }
        }
    }

    private void busStop(){ // unloads and loads passengers in that order
        unloadPassangers();
        if (this.type.matches("expresso")){ //special load passenger function for the Expresso bus because
            loadPassangersExpresso();            // he cant take in passengers that want to go to Cascais or Coimbra
        }else{
            loadPassangers();
        }

    }

    public void run() {
        while(running){                 // runs the thread and consumes tasks while the running variable is true
            switch (tasks.get(0)) {     //task buffer of tasks the bus should do
                case "driving" -> {
                    tasks.remove(0); //remove the task that was just used
                    driving();             //
                    if ((this.type.matches("expresso") && (this.location.getName().matches("Cascais") || this.location.getName().matches("Coimbra")))) {
                        tasks.add("driving"); //if the bus is expresso and the location is cascais it just keeps on driving
                    } else {
                        tasks.add("busStop"); //setup the next task in line
                    }
                }
                case "busStop" -> {     //task buffer of tasks the bus should do
                    tasks.remove(0);//remove the task that was just used
                    busStop(); // unloads and loads passengers
                    tasks.add("driving"); //setup the next task in line
                }
            }
        }
    }


}
