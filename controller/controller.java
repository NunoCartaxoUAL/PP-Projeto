package controller;

import model.Bus;
import model.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class controller {
    private List<Bus> Busses=new ArrayList<Bus>();
    private List<Location> Locations=new ArrayList<Location>();
    private int idGenerator = 1;

    public controller() {
        Locations.add(new Location("Cascais",1,null));
        Locations.add(new Location("Lisboa",2,0));
        Locations.add(new Location("Quimbra",3,1));
        Locations.add(new Location("Porti",4,2));
        Locations.add(new Location("Braga",null,3));


    }

    @Override
    public String toString() {
        return "controller{" +
                "\nBusses=" + Busses +
                ", \nLocations=" + Locations +
                ", \nidGenerator=" + idGenerator +
                '}';
    }

    public void addBus(String type) {
        List<Integer> stops = Arrays.asList(0, 1, 2, 3, 4);
        Location start = Locations.get(new Random().nextInt(5));
        switch (type) {
            case "convencional":
                this.Busses.add(new Bus(51, 10, type, String.valueOf(idGenerator),stops,start));
                break;
            case "miniBus":
                this.Busses.add(new Bus(24, 10, type, String.valueOf(idGenerator),stops,start));
                break;
            case "longDrive":
                this.Busses.add(new Bus(59, 5, type, String.valueOf(idGenerator),stops,start));
                break;
            case "expresso":
                stops = Arrays.asList(1, 3, 4);
                this.Busses.add(new Bus(69, 10, type, String.valueOf(idGenerator),stops,start));
                break;

            default:
                break;
        }
        idGenerator++;
    }

    public void runBusses() {
        for (Bus bus : Busses) {
            bus.start();
        }
    }
}
