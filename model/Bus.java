package model;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class Bus extends Thread{
    HashMap<String, String> clientTest;
    String busID;
    int capacity;
    double speed;
    String type;
    //Status status;
    double distance = 0;
    //Bus 1 2
    //Bus 2 4
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                distance+= speed;
                System.out.println(distance + " " + busID);
            }
        }, 0, 1000);
    }

    public Bus(int capacity, double speed, String type, String busID) {
        this.capacity = capacity;
        this.speed = speed;
        this.type = type;
        this.busID = busID;
        this.run();
    }
}
