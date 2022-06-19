package model;
import controller.controller;
import view.GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;


public class Starter {

    private controller BusManager = new controller(); // create the controller/busManager
    private GUI GUI;

    public Starter() throws FileNotFoundException, InterruptedException {
        readfile(); //read the config file and add the information to start the simulation
        supervisor(); //Open the GUI for the supervisor to control things
        checkSimulationParameter(); // check if passengers > than the sum of the capacity of all busses
                                    // and if busses number 4<=x<=10
        malfunctionStart();

    }

    private void startSimulation() {
        BusManager.runBusses();
    }

    private void checkSimulationParameter() throws InterruptedException {
        if (!BusManager.busNumWithinLimits()) { //TODO change this to be 1 of every type and the output in the print
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Autocarros invalido. \n O numero de Autocarros que inseriu tem de ser entre de 4 a 10.");
            GUI.getUpdateTimer().stop();
        }else if(!BusManager.oneOfEachBus()){
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n É necessario inserir um autocarro de cada tipo");
            GUI.getUpdateTimer().stop();
        } else if (!BusManager.passangerNumWhitinLimits()){ //check is number of passangers > sum of all capacities of the busses
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Passageiros invalido. \n O  numero de Passageiros tem the ser superior a soma das capacidades dos autocarros");
            GUI.getUpdateTimer().stop();
        }else{
            synchronized (this){
                wait(1500);
                startSimulation();
            }

        }
    }

    private void malfunctionStart() {
        BusManager.malfuntion();
    }

    private void supervisor() throws InterruptedException {
        Thread.sleep(10);
        GUI = new GUI(BusManager);
    }

    private void readfile() throws FileNotFoundException {
        String currentPath = System.getProperty("user.dir").replace("\\src",""); //get path to project folder(removes src if started in the command line)
        File myFile = new File(currentPath+"/config.txt");
        final Scanner sc = new Scanner(myFile);
        Boolean stop=false;
        while (sc.hasNextLine() && !stop) {
            var input =  sc.nextLine();
            String[] commands = input.split(" ");
            switch (commands[0]) {
                case "Bus" -> {
                    //TODO expresso começar no sitio errado
                    //TODO turn off testing mode
                    String type = commands[1];
                    String start = commands[2];
                    BusManager.addBus(type, start);
                }
                //TODO remove all prints/ to strings in classes
                case "Passenger" -> {
                    //TODO turn off testing mode2
                    //TODO nao por isto acima dos busses
                    Integer ammount = Integer.parseInt(commands[1]);

                    BusManager.addPassangers(ammount);
                }
                default -> {
                }
            }
        }
        sc.close();
    }
}
