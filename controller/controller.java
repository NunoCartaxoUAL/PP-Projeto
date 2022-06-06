package controller;

import model.Bus;
import model.Location;
import model.Passanger;

import java.util.*;

public class controller {
    private HashMap<String,Bus> Busses = new HashMap<String,Bus>();
    private HashMap<String,Location> Locations = new HashMap<String,Location>();
    private int idGenerator = 1;
    private int ammount = 0;

    public controller() {


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

    public void addPassangers(String num) { //TODO add checks for number of passangers to be > than all bus capacity
        ammount += Integer.parseInt(num);
        List<Location> destinations = new ArrayList<Location>(Locations.values());
        for (int i = 0; i < ammount; i++) {
            var stop = destinations.get(new Random().nextInt(destinations.size()-1)); //TODO THEY CAN BE THE SAME , SO A PASSANGER COULD SPAWN IN ITS DESTINATION AND I CANT BE BOTHERED TO DEAL WITH THAT RN
            var passanger = new Passanger(String.valueOf(i),destinations.get(new Random().nextInt(destinations.size()-1)).getName());
            stop.addPassenger(passanger);
        }
    }

    public void addBus(String type,String Start) {
        List<Integer> stops = Arrays.asList(0, 1, 2, 3, 4);
        Location start = Locations.get(Start);
        String Direction = Arrays.asList("normal","reversed").get(new Random().nextInt(1));
        switch (type) {
            case "convencional":
                this.Busses.put(String.valueOf(idGenerator),new Bus(51, 10, type, String.valueOf(idGenerator),stops,start,Direction));
                break;
            case "miniBus":
                this.Busses.put(String.valueOf(idGenerator),new Bus(24, 10, type, String.valueOf(idGenerator),stops,start,Direction));
                break;
            case "longDrive":
                this.Busses.put(String.valueOf(idGenerator),new Bus(59, 5, type, String.valueOf(idGenerator),stops,start,Direction));
                break;
            case "expresso":
                stops = Arrays.asList(1, 3, 4);
                this.Busses.put(String.valueOf(idGenerator),new Bus(69, 10, type, String.valueOf(idGenerator),stops,start,Direction));
                break;

            default:
                break;
        }
        idGenerator++;
    }

    public void runBusses() {

        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            String key = entry.getKey();
            Bus value = entry.getValue();
            value.start();

            // do what you have to do here
            // In your case, another loop.
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
                "\nBusses=" + Busses +
                ", \nLocations=" + Locations +
                ", \nidGenerator=" + idGenerator +
                '}';
    }
}
