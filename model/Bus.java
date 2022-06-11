package model;

import java.nio.IntBuffer;
import java.util.*;

public class Bus extends Thread{
    private List<Passenger> passengers = new ArrayList<Passenger>();
    private String busID;
    private int capacity;
    private double speed;
    private String type;
    private Location location;
    private String direction;
    private String task = "busStop";
    private List<String> tasks = new ArrayList<String>();
    private List<Integer> stops = new ArrayList<Integer>();
    private boolean running = true;
    //Private Status status;
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

    /*public void setTask(String task) {
        this.task = task;
    }*/

    public void addTasks(String task) {
        tasks.add(task);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    void driving(){
        if(direction.matches("normal")){
            distance = location.getNextDistance();
        }else{
            distance = location.getPreviousDistance();
        }
        for (int i = 1; i <=distance; i++) {

            var timePerKm = 1/speed;
            var milliseconds = timePerKm*60*60*1000/100; //TODO remove the /
            var percentage = (i/distance)*100;
            //milliseconds = 10;
            System.out.println("the bus "+busID+"_"+type+" has done "+i+"km which is "+(int) percentage+"% of the way to "+location.getName());
            try {
                this.sleep((long) milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    void unloadPassangers(){
        synchronized (passengers){
            if (passengers.size()>0){
                for (int i = 0; i < passengers.size(); i++) {
                    var p= passengers.get(i);
                    if (p.getDestination().matches(location.getName())){
                        p.setArrived(true);
                        passengers.remove(p);//synchronize
                    }
                }
            }
        }

    }
    void loadPassangers(){
        synchronized (passengers){
            var stopPassangers = location.getPassangers();
            var passagerSize = stopPassangers.size();
            if (stopPassangers.size()>0){
                for (int i = 0; (i < passagerSize) && (passengers.size()<capacity); i++) {
                    Passenger passenger = stopPassangers.get(0);
                    stopPassangers.remove(0);
                    passengers.add(0, passenger);//synchronize
                }
            }
        }
    }
    void nextStop(){
        switch (direction){

            case "normal":
                if (location==location.getNextStop()){
                    direction="reversed";
                    location = location.getPreviousStop();
                }else{
                    location = location.getNextStop();
                }
                break;

            case "reversed":
                if (location==location.getPreviousStop()){
                    direction="normal";
                    location = location.getNextStop();
                }else{
                    location = location.getPreviousStop();
                }
                break;

            default:
                break;

        }
    }
    void busStop(){
        System.out.println(passengers + " " + busID + " " + type);
        unloadPassangers();
        System.out.println(passengers + " " + busID + " " + type);
        loadPassangers();
        System.out.println(passengers + " " + busID + " " + type);
        nextStop();
    }

    private void maintenance() throws InterruptedException {
        synchronized (this){
            this.wait();
        }
    }


    public void run() {

        while(running){
            switch (tasks.get(0)){//task
                case "driving":
                    tasks.remove(0);
                    driving();
                    //task="busStop";
                    tasks.add(0,"busStop");
                    break;

                case "busStop":
                    tasks.remove(0);
                    busStop();
                    //task="driving";
                    tasks.add(0,"driving");
                    break;

                case "maintenance":
                    tasks.remove(0);
                    try {
                        maintenance();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "terminate":
                    tasks.remove(0);
                    running=false;
                    break;
                default:
                    break;
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
                ", stops=" + stops +
                ", running=" + running +
                ", distance=" + distance +
                '}';
    }
}
