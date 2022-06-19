package model;
import controller.controller;
import view.GUI;
import java.io.File;
import java.io.FileNotFoundException;
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
        if (!BusManager.busNumWithinLimits()) {
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Autocarros invalido. \n O numero de Autocarros que inseriu tem de ser entre de 4 a 10.");
            GUI.getUpdateTimer().stop();
        }else if(!BusManager.oneOfEachBus()){
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n E necessario inserir um autocarro de cada tipo");
            GUI.getUpdateTimer().stop();
        }else if(BusManager.expressoBadLocation()){
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Autocarros expresso nao podem estar em Cascais ou Coimbra.");
            GUI.getUpdateTimer().stop();
        } else if (!BusManager.passangerNumWhitinLimits()){ //check is number of passangers > sum of all capacities of the busses
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Passageiros invalido. \n O  numero de Passageiros tem the ser superior a soma das capacidades dos autocarros");
            GUI.getUpdateTimer().stop();
        }else{
            synchronized (this){
                wait(1000);
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
                    if (commands.length!=3){break;}
                    String type = commands[1];
                    String start = commands[2];
                    BusManager.addBus(type, start);
                }
                case "Passenger" -> {
                    if (commands.length!=2){break;}
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
