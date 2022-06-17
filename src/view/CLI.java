package view;
import controller.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class CLI{

    private controller BusManager = new controller();

    public CLI() throws FileNotFoundException, InterruptedException {
        Readfile();
        malfunctionStart();
        Supervisor();
        //TODO PUT SRC FOLDER IN PROJECT
    }

    private void malfunctionStart() throws InterruptedException {
        BusManager.malfuntion();
    }

    private void Supervisor() throws InterruptedException {
        Thread.sleep(100);
        new GUI(BusManager);
    }

    public void Readfile() throws FileNotFoundException, InterruptedException {
        File myObj = new File("src/view/test.txt");
        final Scanner sc = new Scanner(myObj);
        Boolean stop=false;
        while (sc.hasNextLine() && !stop) {
            System.out.print(">");
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
                    /*if (!BusManager.busMaxReached()){
                        String type = commands[1];
                        String start = commands[2];
                        BusManager.addBus(type,start);
                    }else{
                        System.out.println("Numero de Autocarros invalido numero de Autocarros que inseriu tem de ser entre de 4 a 10.");
                        stop=true;
                    }*/
                case "Print" -> System.out.println(BusManager.toString());
                case "Run" -> BusManager.runBusses();
                case "Passenger" -> {
                    //TODO turn off testing mode2
                    //TODO nao por isto acima dos busses
                    Integer ammount = Integer.parseInt(commands[1]);
                    /*
                    if(BusManager.checkPassangerNum(ammount)){
                        System.out.println("Numero de Passageiros invalidos, tem de ser superior a soma do maximo de todos os autocarros");
                        stop=true;
                    }else{
                        BusManager.addPassangers(ammount);
                    }*/
                    BusManager.addPassangers(ammount);
                }
                default -> {
                }
            }
        }
        sc.close();
    }
}
