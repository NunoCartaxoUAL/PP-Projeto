package view;
import controller.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class CLI{

    private controller BusManager = new controller(); // create the controller/busManager

    public CLI() throws FileNotFoundException, InterruptedException {
        readfile();
        supervisor();
        checkSimulationParameter(); // check if passengers > than the sum of the capacity of all busses
                                    // and if busses number 4<=x<=10
        malfunctionStart();

    }

    private void startSimulation() {
        BusManager.runBusses();
    }

    private void checkSimulationParameter() throws InterruptedException {
        System.out.println(BusManager.busNumWithinLimits());
        if (!BusManager.busNumWithinLimits()) { //TODO change this to be 1 of every type and the output in the print
            System.out.println("Numero de Autocarros invalido. \n O numero de Autocarros que inseriu tem de ser entre de 4 a 10.");
        }else if (!BusManager.passangerNumWhitinLimits()){ //check is number of passangers > sum of all capacities of the busses
            System.out.println("Numero de Passageiros invalido. \n O  numero de Passageiros tem the ser superior a soma das capacidades dos autocarros");
        }else{
            synchronized (this){
                wait(3000);
                startSimulation();
            }

        }
    }

    private void malfunctionStart() {
        BusManager.malfuntion();
    }

    private void supervisor() throws InterruptedException {
        Thread.sleep(10);
        new GUI(BusManager);
    }

    public void readfile() throws FileNotFoundException {
        File myObj = new File("src/view/test.txt");
        final Scanner sc = new Scanner(myObj);
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
