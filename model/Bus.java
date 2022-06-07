package model;

import java.util.*;

public class Bus extends Thread{
    private List<Passanger> clients = new ArrayList<Passanger>();
    private String busID;
    private int capacity;
    private double speed;
    private String type;
    private Location location;
    private String direction;
    private String state = "driving";
    private List<Integer> stops = new ArrayList<Integer>();
    //Private Status status;
    private double distance = 0;

    public Bus(int capacity, double speed, String type, String busID,Location location,String direction) {
        this.capacity = capacity;
        this.speed = speed;
        this.type = type;
        this.busID = busID;
        this.location = location;
        this.direction = direction;

    }

    public void setState(String state) {
        this.state = state;
    }


    void driving(){
        var value = 2000 - speed*10;
        try {
            this.sleep((long) value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    void busStop(){
        System.out.println("bus:"+this.type+" busStop:"+location.getName());
        //TODO load passangers
        if(direction=="normal"){
            if (location==location.getNextStop()){
                direction="reversed";
                location = location.getPreviousStop();
            }else{
                location = location.getNextStop();
            }
        }else{
            if (location==location.getPreviousStop()){
                direction="normal";
                location = location.getNextStop();
            }else{
                location = location.getPreviousStop();
            }
        }

    }

    private void maintenance() throws InterruptedException {
        synchronized (this){
            this.wait();
        }
    }

    public void run() {
        while(true){
            switch (state){

                case "driving":
                    driving();
                    state="busStop";
                    break;

                case "busStop":
                    busStop();
                    state="driving";
                    break;

                case "maintenance":
                    try {
                        maintenance();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public String toString() {
        return "\nBus{" +
                "clients=" + clients +
                ", busID='" + busID + '\'' +
                ", capacity=" + capacity +
                ", speed=" + speed +
                ", type='" + type + '\'' +
                ", stops=" + stops +
                ", distance=" + distance +
                '}';
    }
}
