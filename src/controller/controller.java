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
    private int minPassenger;
    private int randTimeMalfunction = 10000;

    public controller() {
        addCities(); // statically add cities
        Timer t1 = new Timer();
        TimerTask checkIfFinished = new TimerTask() { // check ever second if the simulation is over
            @Override
            public void run() {
                if(checkFinished()){ // if checkfinished == true then stop every thread including this timer
                    System.out.println("all passangers have reached their destination");
                    stopAllThreads();
                    t1.cancel();
                    t1.purge();
                }
            }
        };
        t1.scheduleAtFixedRate(checkIfFinished,500,1000);
    }

    public void addCities(){
        Location Cascais = new Location("Cascais",20,0); // create cities one by one
        Location Lisboa = new Location("Lisboa",25,20);
        Location Coimbra = new Location("Coimbra",30,25);
        Location Porto = new Location("Porto",35,30);
        Location Braga = new Location("Braga",0,35);
        Cascais.setSouthAndNorthStop(Cascais,Lisboa); // link cities with eachother's memory pointers
        Lisboa.setSouthAndNorthStop(Cascais,Coimbra);
        Coimbra.setSouthAndNorthStop(Lisboa,Porto);
        Porto.setSouthAndNorthStop(Coimbra,Braga);
        Braga.setSouthAndNorthStop(Porto,Braga);
        Locations.put("Cascais",Cascais); // add cities to hashmap for storage
        Locations.put("Lisboa",Lisboa);
        Locations.put("Coimbra",Coimbra);
        Locations.put("Porto",Porto);
        Locations.put("Braga",Braga);

        System.out.println(Locations);
    }

    public void addBus(String type,String Start) {
        Location start = Locations.get(Start);
        String Direction = Arrays.asList("north","south").get(new Random().nextInt(2)); // choose randomly one direction to go
        int speed;
        int capacity=0;
        String id = String.valueOf(idGenerator); //get id for the bus
        String key = id+"_"+type; // make key for the hash map
        switch (type) {
            case "convencional" -> {
                capacity = 51;
                speed = 80;
                this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction));
            }
            case "miniBus" -> {
                capacity = 24;
                speed = 80;
                this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction));
            }
            case "longDrive" -> {
                capacity = 59;
                speed = 60;
                this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction));
            }
            case "expresso" -> {
                capacity = 69;
                speed = 80;
                this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction));
            }
            default -> {
            }
        }
        minPassenger += capacity;  //increase minimum passenger number for the simulation
        idGenerator++; //increment id
    }

    public void addPassangers(Integer ammount) {

        List<Location> destinations = new ArrayList<>(Locations.values());
        for (int i = 0; i < ammount; i++) {
            passangerNum ++;
            var start = destinations.get(new Random().nextInt(destinations.size())); //choose random starting point for the passenger
            destinations.remove(start);                                                       //remove it from the list so the starting position and the destination can be different
            var destination = destinations.get(new Random().nextInt(destinations.size())).getName();// choose random destination
            destinations.add(start);                                                                        // re-add the removed destination
            var passanger = new Passenger(String.valueOf(passangerNum),destination);
            start.addPassenger(passanger); // add passangers to city
            passengers.add(passanger); // add passangers to a global list for checking if all passangers have reached their destination
        }
    }

    public HashMap<String, Bus> getBusses() {
        return Busses;
    }

    public boolean passangerNumWhitinLimits() {
        int numPassengers = passengers.size();
        return numPassengers>minPassenger;
    }

    public boolean busNumWithinLimits() {
        return Busses.size()<=10 && Busses.size()>=4;
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



    public void maintenance(String id,int time) throws InterruptedException {
        var bus =Busses.get(id);
        //bus.setTask("maintenance");
        bus.suspend();
        Thread.sleep(time*1000);
        bus.resume();
    }


    public void malfuntion() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                int randInt = new Random().nextInt(Busses.size());    // Choose a random integer between 0 to the number of busses we have -1
                List<Bus> busList = new ArrayList<Bus>(Busses.values());  // create a list from the bus hashmap values
                var affectedBus = busList.get(randInt);  //get a bus with the random integer
                synchronized (affectedBus){
                    try {
                        chooseMalfunction(affectedBus);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    nextMalfunction(timer); // calculates next random delay, terminates the timer thread and calls malfunction
                                            //  again to start a new timer with a random time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, randTimeMalfunction,1 );
    }

    private void chooseMalfunction(Bus affectedBus) throws InterruptedException {
        String anomaly = Arrays.asList("tire","cooler","animal").get(new Random().nextInt(2)); // choose random anomaly
        switch (anomaly){
            case "tire" -> {
                affectedBus.setStatus("This bus is dealing with a punctured tire");
                affectedBus.suspend();// suspend the thread from bus
                Thread.sleep(10*1000);//  put malfunction timer to sleep //TODO check this time
                affectedBus.resume();// resume the thread from bus after sleeping for 8 seconds
                affectedBus.setStatus("Normal");
            }
            case "cooler" -> {
                affectedBus.setStatus("This bus is dealing with a punctured tire");
                affectedBus.suspend();// suspend the thread from bus
                Thread.sleep(20*1000);//  put malfunction timer to sleep //TODO check this time
                affectedBus.resume();// resume the thread from bus after sleeping for 8 seconds
                affectedBus.setStatus("Normal");
            }
            case "animal" -> {
                affectedBus.setStatus("this bus has an animal in front of the road");
                affectedBus.suspend();// suspend the thread from bus
                Thread.sleep(7*1000);//  put malfunction timer to sleep //TODO check this time
                affectedBus.resume();// resume the thread from bus after sleeping for 8 seconds
                affectedBus.setStatus("Normal");
            }
        }

    }

    public void nextMalfunction(Timer timer) throws InterruptedException {
        randTimeMalfunction = new Random().nextInt(5*1000,40*1000); //TODO check this time
        //randTimeMalfunction = 200;   // change the period time
        timer.cancel(); // cancel time
        malfuntion();   // start the time again with a new period time
        timer.purge();  // purge timer
    }

    public String getAllText() {
        String text ="";
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            text += value.getType()+
                    " "+value.getBusID()+": "+
                    value.getPastLocation().getName()+"---"+(int) value.getPercentageToDestination()+"%--->"+value.getLocation().getName()+
                    " , "+value.getDirection() +
                    " , "+value.getNumOfPassangers()+" passageiros)"+
                    " , Status: "+value.getStatus()+"."+
                    "\n";
        }
        text+="\n\n";
        for(Map.Entry<String, Location> entry : Locations.entrySet()) {
            Location value = entry.getValue();
            text += value.getName()+": "+
                    value.getPassangers().size()+" passageiros a espera de Autocarro."+
                    "\n";
        }
        return text;
    }
    public void runBusses() {
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            value.start();
        }

    }

    public void stopAllThreads() {
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) { // loop for every entry in the hashmap
            Bus value = entry.getValue(); // get the value of the entry which is a bus
            value.stop(); //forcefully stop the thread
        }
    }


}

