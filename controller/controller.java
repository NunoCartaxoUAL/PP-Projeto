package controller;

import model.Bus;
import model.Location;
import model.Passanger;

import java.util.*;

public class controller {
    private HashMap<String,Bus> Busses = new HashMap<String,Bus>();
    private HashMap<String,Location> Locations = new HashMap<String,Location>();
    private int idGenerator = 1;
    private int passangerNum = 0;
    private int minPassanger;

    public controller() {
        addCities();
        List<Location> list = new ArrayList<Location>(Locations.values());
        for (int i = 0; i < list.size(); i++) {
            if(i>0 && i<list.size()-1){
                list.get(i).setPreviousAndNextStop(list.get(i-1),list.get(i+1));
            }else if(i==list.size()-1){
                list.get(i).setPreviousAndNextStop(list.get(i-1),list.get(i));
            }else{
                list.get(i).setPreviousAndNextStop(list.get(i),list.get(i+1));
            }

        }
    }
    public void addCities(){
        Location Cascais = new Location("Cascais");
        Location Lisboa = new Location("Lisboa");
        Location Coimbra = new Location("Coimbra");
        Location Porto = new Location("Porto");
        Location Braga = new Location("Braga");
        Locations.put("Cascais",Cascais);
        Locations.put("Lisboa",Lisboa);
        Locations.put("Coimbra",Coimbra);
        Locations.put("Porto",Porto);
        Locations.put("Braga",Braga);
    }

    public boolean checkPassangerNum(Integer num) {

        return num<minPassanger;
    }
    public void addPassangers(Integer ammount) {
        passangerNum += ammount;
        List<Location> destinations = new ArrayList<Location>(Locations.values());
        for (int i = 0; i < passangerNum; i++) {
            var start = destinations.get(new Random().nextInt(destinations.size()));
            destinations.remove(start);
            var destination = destinations.get(new Random().nextInt(destinations.size())).getName();
            destinations.add(start);
            var passanger = new Passanger(String.valueOf(i),destination);
            start.addPassenger(passanger);
        }
    }

    public void addBus(String type,String Start) {
        Location start = Locations.get(Start);
        String Direction = Arrays.asList("normal","reversed").get(new Random().nextInt(1));
        int speed;
        int capacity=0;
        String id;
        switch (type) {
            case "convencional":
                id=String.valueOf(idGenerator);
                capacity = 51;
                speed = 10;
                this.Busses.put(id,new Bus(capacity, speed, type, id,start,Direction));
                break;
            case "miniBus":
                id=String.valueOf(idGenerator);
                capacity = 24;
                speed = 10;
                this.Busses.put(id,new Bus(capacity, speed, type, id,start,Direction));
                break;
            case "longDrive":
                id=String.valueOf(idGenerator);
                capacity = 59;
                speed = 5;
                this.Busses.put(id,new Bus(capacity, 5, type, id,start,Direction));
                break;
            case "expresso":
                id=String.valueOf(idGenerator);
                capacity = 69;
                speed = 10;
                this.Busses.put(id,new Bus(capacity, speed, type, id,start,Direction));
                break;

            default:
                break;
        }
        minPassanger += capacity;
        idGenerator++;
    }

    public void runBusses() {

        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            String key = entry.getKey();
            Bus value = entry.getValue();
            value.start();
        }

    }

    public void maintenance(String id) {
        var bus =Busses.get(id);
        bus.setState("maintenance");

        Timer t1 = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                synchronized (bus){
                    bus.notify();
                    bus.setState("driving");
                }

            }
        };
        t1.schedule(tt,10000);
    }

    @Override
    public String toString() {
        return "controller{" +
                "Busses=" + Busses +
                ", Locations=" + Locations +
                ", idGenerator=" + idGenerator +
                ", passangerNum=" + passangerNum +
                ", minPassanger=" + minPassanger +
                '}';
    }


}
