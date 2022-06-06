package view;
import controller.controller;
import model.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CLI {
    public CLI() throws FileNotFoundException {
        File myObj = new File("view/test.txt");
        final Scanner sc = new Scanner(myObj);
        controller BusManager = new controller();
        while (sc.hasNextLine()) {
            System.out.print(">");
            var input =  sc.nextLine();
            String[] commands = input.split(" ");
            switch(commands[0]) {
                case "Bus":
                    String type = commands[1];
                    String start = commands[2];
                    BusManager.addBus(type,start);
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
                    String ammount = commands[1];
                    BusManager.addPassangers(ammount);
                default:
                    break;
            }
        }
    }
}
