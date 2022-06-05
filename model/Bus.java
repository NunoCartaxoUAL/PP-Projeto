package model;

import java.util.*;


public class Bus extends Thread{
    private List<Client> clients = new ArrayList<Client>();
    private String busID;
    private int capacity;
    private double speed;
    private String type;
    private Location location;
    private String state = "driving";
    private List<Integer> stops = new ArrayList<Integer>();
    //Private Status status;
    private double distance = 0;

    public void run() {
        var value = 2000 - speed*10;

        while(true){
            switch (state){
                case "calculating distance":

                    if(distance== 40){
                        System.out.println("nextstop");
                        state="disembarking";
                        distance = 0;
                    }else{
                        distance+= speed;
                        System.out.println(distance + " " + busID + " " + type);
                        state="driving";
                    }
                    break;
                case "driving":
                    try {
                        this.sleep((long) value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    state="calculating distance";
                    break;
                case "disembarking":
                    System.out.println("disembarking");
                    state="calculating distance";
                    break;
                default:
            }
        }
    }

    public Bus(int capacity, double speed, String type, String busID,List<Integer> stops,Location location) {
        this.capacity = capacity;
        this.speed = speed;
        this.type = type;
        this.busID = busID;
        this.stops = stops;
        this.location = location;

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
