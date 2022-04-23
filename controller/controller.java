package controller;

import model.Bus;

import java.util.ArrayList;

public class controller {
    ArrayList<Bus> Busses= new ArrayList<Bus>(10);

    public void runBus(String id,int speed) {
        this.Busses.add(new Bus(10,speed,"yes",id));

    }
}
