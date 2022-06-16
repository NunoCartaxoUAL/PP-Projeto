package controller;

import model.Bus;
import model.Location;
import model.Passenger;
import java.util.*;

public class controller {

    private HashMap<String,Bus> Busses = new HashMap<>();
    private HashMap<String,Location> Locations = new HashMap<>();
    private List<Passenger> passengers = new ArrayList<>();
    private int idGenerator = 1;
    private int passangerNum = 0;
    private int minPassanger;
    private int randTimeMalfunction = 100;

    public controller() {
        addCities();
        Timer t1 = new Timer();
        TimerTask checkIfFinished = new TimerTask() {
            @Override
            public void run() {
                if(checkFinished()){
                    System.out.println("all passangers have reached their destination");
                    System.out.println(this);
                    stopAllThreads();
                }
            }
        };
        t1.scheduleAtFixedRate(checkIfFinished,500,1000);
    }

    public void addCities(){
        Location Cascais = new Location("Cascais",20,0);
        Location Lisboa = new Location("Lisboa",25,20);
        Location Coimbra = new Location("Coimbra",30,25);
        Location Porto = new Location("Porto",35,30);
        Location Braga = new Location("Braga",0,35);
        Cascais.setPreviousAndNextStop(Cascais,Lisboa);
        Lisboa.setPreviousAndNextStop(Cascais,Coimbra);
        Coimbra.setPreviousAndNextStop(Lisboa,Porto);
        Porto.setPreviousAndNextStop(Coimbra,Braga);
        Braga.setPreviousAndNextStop(Porto,Braga);
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
        List<Location> destinations = new ArrayList<>(Locations.values());
        for (int i = 0; i < passangerNum; i++) {
            var start = destinations.get(new Random().nextInt(destinations.size()));
            destinations.remove(start);
            var destination = destinations.get(new Random().nextInt(destinations.size())).getName();
            destinations.add(start);
            var passanger = new Passenger(String.valueOf(i),destination);
            start.addPassenger(passanger);
            passengers.add(passanger);
        }
    }

    public boolean checkFinished(){
        var stop = true;

        for (Passenger p :
                passengers) {
            if (!p.getArrived()){
                stop = false;
            }
        }
        return stop;
    }

    public void addBus(String type,String Start) {
        Location start = Locations.get(Start);
        String Direction = Arrays.asList("north","south").get(new Random().nextInt(1));
        int speed;
        int capacity=0;
        String id;
        switch (type) {
            case "convencional" -> {
                id = String.valueOf(idGenerator);
                capacity = 51;
                speed = 80;
                this.Busses.put(id, new Bus(capacity, speed, type, id, start, Direction));
            }
            case "miniBus" -> {
                id = String.valueOf(idGenerator);
                capacity = 24;
                speed = 80;
                this.Busses.put(id, new Bus(capacity, speed, type, id, start, Direction));
            }
            case "longDrive" -> {
                id = String.valueOf(idGenerator);
                capacity = 59;
                speed = 60;
                this.Busses.put(id, new Bus(capacity, speed, type, id, start, Direction));
            }
            case "expresso" -> {
                id = String.valueOf(idGenerator);
                capacity = 69;
                speed = 80;
                this.Busses.put(id, new Bus(capacity, speed, type, id, start, Direction));
            }
            default -> {
            }
        }
        minPassanger += capacity;
        idGenerator++;
    }

    public void runBusses() {
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            value.start();
        }

    }

    public void stopAllThreads() {
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            //value.stop();
            value.setRunning(false);
        }
        System.exit(1);
    }

    public void maintenance(String id) {
        var bus =Busses.get(id);
        //bus.setTask("maintenance");
        bus.addTasks("maintenance");
        Timer t1 = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                synchronized (bus){
                    //bus.setTask("driving");
                    bus.notify();
                }

            }
        };
        t1.schedule(tt,1000);
    }

    @Override
    public String toString() {
        return "controller{" +
                "Busses=" + Busses +
                ", Locations="+"\n" + Locations +
                ", idGenerator=" + idGenerator +
                ", passangerNum=" + passangerNum +
                ", minPassanger=" + minPassanger +
                '}';
    }

    public void malfuntion() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                // do something...
                int randInt = new Random().nextInt(Busses.size())+1;
                String randBusId = String.valueOf(randInt);
                var affectedBus = Busses.get(randBusId);
                synchronized (affectedBus){
                    try {
                        maintenance(randBusId);
                        affectedBus.suspend();
                        Thread.sleep(2000);
                        affectedBus.resume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                nextMalfunction(timer);
            }
        }, randTimeMalfunction,1 );
    }
    public void nextMalfunction(Timer timer){
        randTimeMalfunction = new Random().nextInt(2000,40000);
        //randTimeMalfunction = 200;   // change the period time
        timer.cancel(); // cancel time
        malfuntion();   // start the time again with a new period time
        timer.purge();
    }
}
