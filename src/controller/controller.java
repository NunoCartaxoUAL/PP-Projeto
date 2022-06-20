package controller;

import model.Bus;
import model.Location;
import model.Passenger;
import java.util.*;

public class controller {

    private HashMap<String,Bus> Busses = new HashMap<>();
    private HashMap<String,Location> Locations = new HashMap<>();
    private List<Passenger> passengers = new ArrayList<>();
    private int idGenerator = 1; //bus id generator
    private int passangerNum = 0; //number of passengers in the simulation , used to check if they are enough to start it
    private int minPassenger; // min number of passengers to start the simulation
    private int randTimeMalfunction = 0; //random time of the malfunction generator
    private int[] bussesPerCity = {0,0,0,0,0}; // 0-Cascais , 1- Lisboa , 2-Coimbra , 3-Porto , 4-Braga
    private String finishedMessage ="";

    public controller() {
        randTimeMalfunction = new Random().nextInt(5*1000,40*1000);
        addCities(); // statically add cities
        Timer t1 = new Timer();
        TimerTask checkIfFinished = new TimerTask() { // check ever second if the simulation is over
            @Override
            public void run() {
                if(checkFinished()){ // if checkfinished == true then stop every thread including this timer
                    finishedMessage = "all passangers have reached their destination";
                    System.out.println("all passangers have reached their destination");
                    stopAllThreads();
                    t1.cancel();
                    t1.purge();
                }
            }
        };
        t1.scheduleAtFixedRate(checkIfFinished,500,1000);
    }

    public void addCities(){ // add all cities statically
        Location Cascais = new Location("Cascais",30,0); // create cities one by one
        Location Lisboa = new Location("Lisboa",204,30);
        Location Coimbra = new Location("Coimbra",122,204);
        Location Porto = new Location("Porto",55,122);
        Location Braga = new Location("Braga",0,55);
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
    }

    public void bussesPerCity(String city, Integer busses) { //creates the list that stores the bus capacity in each city
                                                            // 0-Cascais , 1- Lisboa , 2-Coimbra , 3-Porto , 4-Braga
        switch (city){
            case "Cascais"->{
                this.bussesPerCity[0]=busses;
            }
            case "Lisboa"->{
                this.bussesPerCity[1]=busses;
            }
            case "Coimbra"->{
                this.bussesPerCity[2]=busses;
            }
            case "Porto"->{
                this.bussesPerCity[3]=busses;
            }
            case "Braga"->{
                this.bussesPerCity[4]=busses;
            }
        }
    }
    private Location locationsAvailable() {  //chooses a random city out of all of the cities for a starting point of a bus

        List<Location> startingPoint = new ArrayList<>(Locations.values());
        while (startingPoint.size()>0){
            Location start = startingPoint.get(new Random().nextInt(startingPoint.size())); //random location of the locations available
            switch (start.getName()){
                case "Cascais"->{
                    if (this.bussesPerCity[0]!=0){ //if capacity for busses of the location isnt 0 then this location is a valid return for the bus
                        this.bussesPerCity[0]--;
                        return start;
                    }else{                          //else remove the location from the choices available since the
                        startingPoint.remove(start);
                    }
                }
                case "Lisboa"->{
                    if (this.bussesPerCity[1]!=0){
                        this.bussesPerCity[1]--;
                        return start;
                    }else{
                        startingPoint.remove(start);
                    }
                }
                case "Coimbra"->{
                    if (this.bussesPerCity[2]!=0){
                        this.bussesPerCity[2]--;
                        return start;
                    }else{
                        startingPoint.remove(start);
                    }
                }
                case "Porto"->{
                    if (this.bussesPerCity[3]!=0){
                        this.bussesPerCity[3]--;
                        return start;
                    }else{
                    startingPoint.remove(start);
                    }
                }
                case "Braga"->{
                    if (this.bussesPerCity[4]!=0){
                        this.bussesPerCity[4]--;
                        return start;
                    }else{
                        startingPoint.remove(start);
                    }
                }
            }
        }
        return null; //if there is no more spaces in the cities for busses , their location is null and it is checked before starting the simulation
    }

    public void addBus(Integer busNum,String type) {
        for (int i = 0; i <  busNum; i++) {
            Location start = this.locationsAvailable();
            String Direction = Arrays.asList("north","south").get(new Random().nextInt(2)); // choose randomly one direction to go
            int speed;
            int capacity=0;
            String id = String.valueOf(idGenerator); //get id for the bus
            String key = id+"_"+type; // make key for the hash map
            switch (type) {
                case "convencional" -> {
                    capacity = 51;
                    speed = 80;
                    this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction)); // addes a new pair key and value for the hashmap of key,busses
                }
                case "miniBus" -> {
                    capacity = 24;
                    speed = 80;
                    this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction)); // addes a new pair key and value for the hashmap of key,busses
                }
                case "longDrive" -> {
                    capacity = 59;
                    speed = 60;
                    this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction)); // addes a new pair key and value for the hashmap of key,busses
                }
                case "expresso" -> {
                    capacity = 69;
                    speed = 80;
                    this.Busses.put(key, new Bus(capacity, speed, type, id, start, Direction)); // addes a new pair key and value for the hashmap of key,busses
                }
                default -> {
                }
            }
            minPassenger += capacity;  //increase minimum passenger number for the simulation
            idGenerator++; //increment id
        }

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

    public boolean passangerNumWhitinLimits() {  //checks if the num of passengers is higher than the sum of all busses capacity
        int numPassengers = passengers.size();
        return numPassengers>minPassenger;
    }

    public boolean busNumWithinLimits() {   //checks if there is the right ammount of busses in the simulation
        return Busses.size()<=10 && Busses.size()>=4;
    }
    public boolean oneOfEachBus() {  //checks if there is 1 of each bus
        Boolean busExp = false;
        Boolean busCon = false;
        Boolean busMin = false;
        Boolean busLon = false;
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            if (value.getType().matches("expresso")){
                busExp = true;
            }
            if (value.getType().matches("miniBus")){
                busMin = true;
            }
            if (value.getType().matches("longDrive")){
                busLon = true;
            }
            if (value.getType().matches("convencional")){
                busCon = true;
            }
        }
        return busExp && busCon && busLon && busMin;
    }

    public boolean busCapacityIsFilled() { //checks if every location got filled with the desired ammount of busses
        Boolean same = true;
        for (Integer busNum :
                bussesPerCity) {
            same = same && (busNum == 0);
        }
        return same;
    }

    public boolean busNumberIsTheSame(){ //checks if number of busses is the same as the number specified in the cities
        Boolean same=true;
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            same = same && (value.getLocation()!=null);  //location of the bus is null if there are too many busses in the simulation
        }
        return same;
    }

    public boolean checkFinished(){ //checks every passenger if he has arrived
        var stop = true;

        for (Passenger p :
                passengers) {
            if (!p.getArrived()){
                stop = false;
            }
        }
        return stop;
    }

    public void maintenance(String id,int time,String Reason) throws InterruptedException { //suspends the bus for the desired time
        var bus =Busses.get(id);
        bus.setStatus(Reason);
        bus.suspend();
        Thread.sleep(time* 1000L);
        bus.resume();
        bus.setStatus("Normal");
    }


    public void malfuntion() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                int randInt = new Random().nextInt(Busses.size());    // Choose a random integer between 0 to the number of busses we have -1
                List<Bus> busList = new ArrayList<>(Busses.values());  // create a list from the bus hashmap values
                var affectedBus = busList.get(randInt);  //get a bus with the random integer
                synchronized (affectedBus){
                    try {
                        chooseMalfunction(affectedBus); // chooses a random malfunction
                        nextMalfunction(timer); // calculates next random delay, terminates the timer thread and calls malfunction
                                                //  again to start a new timer with a random time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, randTimeMalfunction,1 ); //task delay is random and restarted every cycle because if the period is random once it is started it will stay constant
    }

    private void chooseMalfunction(Bus affectedBus) throws InterruptedException { //randomize on malfunction every so often
        String anomaly = Arrays.asList("tire","cooler","animal").get(new Random().nextInt(2)); // choose random anomaly
        switch (anomaly){
            case "tire" -> {
                affectedBus.setStatus("This bus is dealing with a punctured tire");
                affectedBus.suspend();// suspend the thread from bus
                Thread.sleep(10*1000);//  put malfunction timer to sleep
                affectedBus.resume();// resume the thread from bus after sleeping for 8 seconds
                affectedBus.setStatus("Normal");
            }
            case "cooler" -> {
                affectedBus.setStatus("This bus is dealing with a punctured tire");
                affectedBus.suspend();// suspend the thread from bus
                Thread.sleep(20*1000);//  put malfunction timer to sleep
                affectedBus.resume();// resume the thread from bus after sleeping for 8 seconds
                affectedBus.setStatus("Normal");
            }
            case "animal" -> {
                affectedBus.setStatus("this bus has an animal in front of the road");
                affectedBus.suspend();// suspend the thread from bus
                Thread.sleep(7*1000);//  put malfunction timer to sleep
                affectedBus.resume();// resume the thread from bus after sleeping for 8 seconds
                affectedBus.setStatus("Normal");
            }
        }

    }

    private void nextMalfunction(Timer timer) throws InterruptedException {//tops the current malfunction timer and randomizes a new delay for a new timer to start
        randTimeMalfunction = new Random().nextInt(5*1000,40*1000);
        //randTimeMalfunction = 200;   // change the period time
        timer.cancel(); // cancel time
        malfuntion();   // start the time again with a new period time
        timer.purge();  // purge timer
    }

    public String getAllText() { //retrieves all important text to be shown in the simulation
        String text ="";
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            text += value.getType()+
                    " "+value.getBusID()+": "+
                    " , "+value.getNumOfPassangers()+" passageiros, "+
                    value.getPastLocation().getName()+"---"+(int) value.getPercentageToDestination()+"%--->"+value.getLocation().getName()+
                    " , "+value.getDirection() +

                    " , Status: "+value.getStatus()+"."+
                    "\n";
        }
        text+="\n";
        text+=finishedMessage; //add the message to the GUI saying that it is finished , this is "" until the simulation finishes
        text+="\n\n";
        for(Map.Entry<String, Location> entry : Locations.entrySet()) {
            Location value = entry.getValue();
            text += value.getName()+": "+
                    value.getPassangers().size()+" passageiros a espera de Autocarro."+
                    "\n";
        }
        return text;
    }
    public void runBusses() { // starts all buss threads
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) {
            Bus value = entry.getValue();
            value.start();
        }

    }

    public void stopAllThreads() { // stops all bus threads
        for(Map.Entry<String, Bus> entry : Busses.entrySet()) { // loop for every entry in the hashmap
            Bus value = entry.getValue(); // get the value of the entry which is a bus
            value.stop(); //forcefully stop the thread
        }
    }

}

