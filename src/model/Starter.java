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
        checkSimulationParameter(); //check the configuration file for a valid configurion and start the simultion if so
        malfunctionStart(); //start the malfunction generator
    }

    private void startSimulation() {
        BusManager.runBusses();
    } //starts every bus so the simulation starts

    private void checkSimulationParameter() throws InterruptedException {
        if (!BusManager.busNumWithinLimits()) { //Check is the number of busses is between 4 and 10 (4 <= x <= 10)
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Autocarros invalido. \n O numero de Autocarros que inseriu tem de ser entre de 4 a 10."); //show a warning on the GUI
            GUI.getUpdateTimer().stop(); //stop the GUI from updating text so the warning message doesnt disappear
        }else if(!BusManager.oneOfEachBus()){ //Check if there is atleast one of each bus
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n E necessario inserir um autocarro de cada tipo."); //show a warning on the GUI
            GUI.getUpdateTimer().stop(); //stop the GUI from updating text so the warning message doesnt disappear
        }else if (!BusManager.passangerNumWhitinLimits()){ //check is number of passangers > sum of all capacities of the busses
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Passageiros invalido.\n O  numero de Passageiros tem de ser superior a soma das capacidades dos autocarros."); //show a warning on the GUI
            GUI.getUpdateTimer().stop(); //stop the GUI from updating text so the warning message doesnt disappear
        }else if (!BusManager.busCapacityIsFilled()){ //Check if the city bus capacity has been fully filled
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Autocarros invalido.\n A soma dos numeros de Autocarros tem de ser igual a soma de numeros dos Autocarros nas Cidades.\nNão inseriu autocarros suficientes para preencher as cidades"); //show a warning on the GUI
            GUI.getUpdateTimer().stop(); //stop the GUI from updating text so the warning message doesnt disappear
        }else if(!BusManager.busNumberIsTheSame()){ //checks is the number of busses were too many for the cities busses capicities
            GUI.setTextPanelText("(Ficheiro de configuracao errado)\n Numero de Autocarros invalido.\n A soma dos numeros de Autocarros tem de ser igual a soma de numeros dos Autocarros nas Cidades.\nInsuficientes espaços para autocarros nas cidades."); //show a warning on the GUI
            GUI.getUpdateTimer().stop(); //stop the GUI from updating text so the warning message doesnt disappear
        }
        else{
            synchronized (this){ //synchronize so i can use wait()
                wait(1500); // wait 1.5 seconds so the user has time to look at the GUI before the simulation starts
                startSimulation(); //run the simulation
            }

        }
    }

    private void malfunctionStart() {
        BusManager.malfuntion(); //start the malfunction timer
    }

    private void supervisor() throws InterruptedException {
        GUI = new GUI(BusManager); //creates the GUI with the busManager controller
    }

    private void readfile() throws FileNotFoundException {
        String currentPath = System.getProperty("user.dir").replace("\\src",""); //get path to project folder(removes src if started in the command line)
        File myFile = new File(currentPath+"/config.txt"); //get path to the configuration file
        final Scanner sc = new Scanner(myFile);                     // open a scanner from the file
        while (sc.hasNextLine()) {                                  //for each line in the file
            var input =  sc.nextLine();                      // store the line
            String[] commands = input.split(" ");             // split it by spaces
            switch (commands[0]) {                                  //and check the first position to know what the line is
                case "Cidade" ->{
                    if (commands.length!=3){break;}                 //stops the function if the command is invalid
                    String city = commands[1];                      //name of the city
                    Integer busses = Integer.parseInt(commands[2]); //bus capacity of the city
                    BusManager.bussesPerCity(city,busses);          //creates the list that stores the bus capacity in each city
                }
                case "Bus" -> {
                    if (commands.length!=3){break;}                 //stops the function if the command is invalid
                    Integer num = Integer.parseInt(commands[1]);    //number of bussses
                    String type = commands[2];                      //type of the bus
                    BusManager.addBus(num, type);                   //Adds the busses to the simulation
                }
                case "Passenger" -> {
                    if (commands.length!=2){break;}
                    Integer ammount = Integer.parseInt(commands[1]);//number of passengers

                    BusManager.addPassangers(ammount);              //Adds the Passengers to the simulation
                }
                default -> {
                }
            }
        }
        sc.close(); //close the scanner that read the file
    }
}
