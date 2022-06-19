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
    private List<String> tasks = new ArrayList<>();
    private boolean running = true;
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

    public List<String> getTasks() {
        return tasks;
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

    public Location getPastLocation(){
        return switch (this.direction) {
            case "north" -> this.location.getSouthStop();
            case "south" -> this.location.getNorthStop();
            default -> null;
        };

    }

    private void driving(){
        nextStop(); //Calculate the next stop
        if(this.direction.matches("north")){
            distance = this.location.getSouthDistance(); // get next stop north distance
        }else{
            distance = this.location.getNorthDistance();// get next stop south distance
        }

        for (int i = 1; i <=distance; i++) {

            var timePerKm = 1/speed;
            var milliseconds = timePerKm*60*60*1000/100; //TODO remove the /
            var percentage = (i/distance)*100;
            //milliseconds = 10;
            percentageToDestination = percentage;
            //System.out.println("the bus "+busID+"_"+type+" has done "+i+"km which is "+(int) percentage+"% of the way to "+this.location.getName());
            try {
                sleep((long) milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void unloadPassangers(){
        if (passengers.size()>0){
            for (int i = 0; i < passengers.size(); i++) {
                var p= passengers.get(i);
                if (p.getDestination().matches(this.location.getName())){
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
                for (int i = 0; (i < passagerSize) && (passengers.size()<capacity); i++) {
                    Passenger passenger = stopPassangers.get(0); // get first passenger in location list
                    stopPassangers.remove(0);  // remove that passenger from the list
                    passengers.add(0, passenger);// add the passenger to the bus passenger list
                }
            }
        }
    }


    private void loadPassangersExpresso() {
        synchronized (this.location){
            var stopPassangers = this.location.getPassangers();
            var passagerSize = stopPassangers.size();
            var skipPassanger = 0 ;
            if (stopPassangers.size()>0){
                for (int i = 0; (i < passagerSize) && (passengers.size()<capacity); i++) {
                    Passenger passenger = stopPassangers.get(skipPassanger);
                    if (passenger.getDestination().matches("Cascais") ||passenger.getDestination().matches("Coimbra")){
                        skipPassanger++;
                    }else{
                        stopPassangers.remove(skipPassanger);
                        passengers.add(0, passenger);//synchronize
                    }
                }
            }
        }
    }
    private void nextStop(){

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

    private void busStop(){
        unloadPassangers();
        if (this.type.matches("expresso")){
            loadPassangersExpresso();
        }else{
            loadPassangers();
        }

    }

    public void run() {

        while(running){
            switch (tasks.get(0)) {//task buffer of tasks the bus should do
                case "driving" -> {
                    tasks.remove(0); //remove the task that ws just used
                    driving();
                    if ((this.type.matches("expresso") && (this.location.getName().matches("Cascais") || this.location.getName().matches("Coimbra")))) {
                        tasks.add("driving");
                    } else {
                        tasks.add("busStop");
                    }
                }
                case "busStop" -> {
                    tasks.remove(0);//remove the task that ws just used
                    busStop();
                    //task="driving";
                    tasks.add("driving");
                }
            }
        }
    }


}
