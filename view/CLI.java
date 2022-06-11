package view;
import controller.controller;
import model.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CLI {

    private controller BusManager = new controller();

    public CLI() throws FileNotFoundException {
        Readfile();
    }

    public void Readfile() throws FileNotFoundException {
        File myObj = new File("view/test.txt");
        final Scanner sc = new Scanner(myObj);
        Boolean stop=false;
        while (sc.hasNextLine() && !stop) {
            System.out.print(">");
            var input =  sc.nextLine();
            String[] commands = input.split(" ");
            switch(commands[0]) {
                case "Bus":
                    //TODO turn off testing mode
                    String type = commands[1];
                    String start = commands[2];
                    BusManager.addBus(type,start);
                    /*if (!BusManager.busMaxReached()){
                        String type = commands[1];
                        String start = commands[2];
                        BusManager.addBus(type,start);
                    }else{
                        System.out.println("Numero de Autocarros invalido numero de Autocarros que inseriu tem de ser entre de 4 a 10.");
                        stop=true;
                    }*/
                    break;
                case "Print":
                    System.out.println(BusManager.toString());
                    break;
                case "Run":
                    BusManager.runBusses();

                    break;
                case "M":
                    String id = commands[1];
                    BusManager.maintenance(id);
                    break;
                case "Passenger":
                    //TODO turn off testing mode2
                    Integer ammount = Integer.parseInt(commands[1]);
                    /*
                    if(BusManager.checkPassangerNum(ammount)){
                        System.out.println("Numero de Passageiros invalidos, tem de ser superior a soma do maximo de todos os autocarros");
                        stop=true;
                    }else{
                        BusManager.addPassangers(ammount);
                    }*/
                    BusManager.addPassangers(ammount);
                    break;
                default:
                    break;
            }
        }
        sc.close();
    }
}
