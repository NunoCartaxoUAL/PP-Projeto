package view;
import controller.controller;

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
                    String id = commands[1];
                    Integer speed = Integer.parseInt(commands[2]);
                    BusManager.runBus(id,speed);
                default:
                    break;
            }
        }
    }
}
