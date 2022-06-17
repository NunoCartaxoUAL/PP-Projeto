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
    private String status;
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
    public void setRunning(boolean running) {
        this.running = running;
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

    public void addTasks(String task) {
        tasks.add(task);
    }

    void driving(){
        nextStop();
        if(this.direction.matches("north")){
            distance = this.location.getPreviousDistance();

        }else{
            distance = this.location.getNextDistance();
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
    void unloadPassangers(){
        //TODO REMOVE THIS
        if (passengers.size()>0){
            for (int i = 0; i < passengers.size(); i++) {
                var p= passengers.get(i);
                if (p.getDestination().matches(this.location.getName())){
                    p.setArrived(true);
                    passengers.remove(p);//synchronize
                }
            }
        }
    }
    void loadPassangers(){
        synchronized (this.location){
            var stopPassangers = this.location.getPassangers();
            var passagerSize = stopPassangers.size();
            if (stopPassangers.size()>0){
                for (int i = 0; (i < passagerSize) && (passengers.size()<capacity); i++) {
                    Passenger passenger = stopPassangers.get(0);
                    passenger.setOnBus(true);
                    stopPassangers.remove(0);
                    passengers.add(0, passenger);//synchronize
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
                        passenger.setOnBus(true);
                        stopPassangers.remove(skipPassanger);
                        passengers.add(0, passenger);//synchronize
                    }
                }
            }
        }
    }
    void nextStop(){
        switch (this.direction){

            case "north":
                if (this.location==this.location.getNextStop()){
                    this.direction="south";
                    this.location = this.location.getPreviousStop();
                }else{
                    this.location = this.location.getNextStop();
                }
                break;

            case "south":
                if (this.location==this.location.getPreviousStop()){
                    this.direction="north";
                    this.location = this.location.getNextStop();
                }else{
                    this.location = this.location.getPreviousStop();
                }
                break;

            default:
                break;

        }
    }

    void busStop(){
        unloadPassangers();
        if (this.type.matches("expresso")){
            loadPassangersExpresso();
        }else{
            loadPassangers();
        }

    }

    private void maintenance() throws InterruptedException {
        synchronized (this){
            this.wait();
        }
    }

    public void run() {

        while(running){
            switch (tasks.get(0)) {//task
                case "driving" -> {
                    tasks.remove(0);
                    driving();
                    if ((this.type.matches("expresso") && (this.location.getName().matches("Cascais") || this.location.getName().matches("Coimbra")))) {
                        tasks.add("driving");
                    } else {
                        tasks.add("busStop");
                    }
                }
                case "busStop" -> {
                    tasks.remove(0);
                    busStop();
                    //task="driving";
                    tasks.add("driving");
                }
                case "maintenance" -> {
                    tasks.remove(0);
                    try {
                        maintenance();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case "terminate" -> {
                    tasks.remove(0);
                    running = false;
                }
                default -> {
                }
            }
        }
    }


    @Override
    public String toString() {
        return "Bus{" +
                "passengers=" + passengers +
                ", busID='" + busID + '\'' +
                ", capacity=" + capacity +
                ", speed=" + speed +
                ", type='" + type + '\'' +
                ", location=" + location.getName() +
                ", direction='" + direction + '\'' +
                ", tasks=" + tasks +
                ", running=" + running +
                ", distance=" + distance +
                '}';
    }

    public String getType() {
        return type;
    }

    public String getBusID() {
        return busID;
    }
}
