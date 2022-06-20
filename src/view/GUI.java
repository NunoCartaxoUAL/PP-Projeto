package view;

import controller.controller;
import model.Bus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TimerTask;

public class GUI extends JFrame {
    private final SpringLayout lyt;
    private final int width;
    private final int heigth;
    private final JPanel mainPanel;
    private final JTextPane textPanel;
    private final controller busManager;
    private JComboBox bussesComboBox;
    private Timer updateTimer;

    public GUI(controller busManager){

        //basic setup of the window and creation of spring layout for the window
        this.busManager = busManager;
        this.setSize(740, 450);
        this.width =this.getSize().width;
        this.heigth = this.getSize().height;
        this.lyt = new SpringLayout();
        this.setTitle("PP-Projeto");

        //basic setup of the main Jpanel
        this.mainPanel = new JPanel();
        this.setContentPane(mainPanel);
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(lyt);

        //Setup for the text Jpane that updates every second with text from the simulation
        textPanel = new JTextPane();
        textPanel.setPreferredSize(new Dimension(this.width-200, this.heigth-100));
        textPanel.setBackground(Color.DARK_GRAY);
        textPanel.setForeground(Color.white);
        textPanel.setFont(new Font("Calibri",0, 14));
        lyt.putConstraint(SpringLayout.NORTH,textPanel,10,SpringLayout.NORTH,mainPanel);
        lyt.putConstraint(SpringLayout.WEST,textPanel,10,SpringLayout.WEST,mainPanel);

        //Action listenner that tied to a java swing timer that updates the text of JText panel every 500 milliseconds
        ActionListener updateText = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String text="";
                text = busManager.getAllText();
                textPanel.setText(text);
            }
        };
        updateTimer = new Timer(500,updateText);
        updateTimer.setRepeats(true);
        updateTimer.start();

        //ActionListener for the buttons that the employee can press which stop the busses calling maintenance for x seconds
        ActionListener maintenanceA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(20,"Maintenance...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //ActionListener for the buttons that the employee can press which stop the busses calling maintenance for x seconds
        ActionListener stopA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(5,"Stopped for 5 seconds");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //ActionListener for the buttons that the employee can press which stop the busses calling maintenance for x seconds
        ActionListener changeDriverA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(10, "Changing driver");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //ActionListener for the buttons that the employee can press which stop the busses calling maintenance for x seconds
        ActionListener refillBusA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(7,"Reffilling Bus");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //Basic setup of buttons for the employee
        JButton stopBus = new JButton();
        stopBus.setPreferredSize(new Dimension(150,20));
        stopBus.setText("Stop Bus (5s)");
        stopBus.addActionListener(stopA);
        lyt.putConstraint(SpringLayout.WEST,stopBus,10,SpringLayout.WEST,mainPanel);
        lyt.putConstraint(SpringLayout.NORTH,stopBus,10,SpringLayout.SOUTH,textPanel);

        //Basic setup of buttons for the employee
        JButton changeDriver = new JButton();
        changeDriver.setPreferredSize(new Dimension(150,20));
        changeDriver.setText("Change Driver (10s)");
        changeDriver.addActionListener(changeDriverA);
        lyt.putConstraint(SpringLayout.WEST,changeDriver,2,SpringLayout.EAST,stopBus);
        lyt.putConstraint(SpringLayout.NORTH,changeDriver,0,SpringLayout.NORTH,stopBus);

        //Basic setup of buttons for the employee
        JButton RefillBus = new JButton();
        RefillBus.setPreferredSize(new Dimension(150,20));
        RefillBus.setText("Refill Bus (7s)");
        RefillBus.addActionListener(refillBusA);
        lyt.putConstraint(SpringLayout.WEST,RefillBus,2,SpringLayout.EAST,changeDriver);
        lyt.putConstraint(SpringLayout.NORTH,RefillBus,0,SpringLayout.NORTH,changeDriver);

        //Basic setup of buttons for the employee
        JButton maintenance = new JButton();
        maintenance.setPreferredSize(new Dimension(150,20));
        maintenance.setText("Maintenance (20s)");
        maintenance.addActionListener(maintenanceA);
        lyt.putConstraint(SpringLayout.WEST,maintenance,2,SpringLayout.EAST,RefillBus);
        lyt.putConstraint(SpringLayout.NORTH,maintenance,0,SpringLayout.NORTH,RefillBus);


        //Add Author Labels that display the developers of the App
        JLabel Author1 = new JLabel();
        Author1.setText("Nuno Cartaxo  30007214");
        Author1.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.WEST,Author1,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,Author1,200,SpringLayout.NORTH,textPanel);

        //Add Author Labels that display the developers of the App
        JLabel Author2 = new JLabel();
        Author2.setText("Andre Martins 30007252");
        Author2.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.WEST,Author2,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,Author2,15,SpringLayout.SOUTH,Author1);

        //Add Author Labels that display the developers of the App
        JLabel Author3 = new JLabel();
        Author3.setText("Andre Santos  30007679");
        Author3.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.WEST,Author3,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,Author3,15,SpringLayout.SOUTH,Author2);


        //Create a combo box to select the bus that will receive the action of the Buttons
        this.bussesComboBox = addBussesComboBox();
        lyt.putConstraint(SpringLayout.WEST,bussesComboBox,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,bussesComboBox,0,SpringLayout.NORTH,textPanel);

        //add every object to the main panel
        mainPanel.add(Author3);
        mainPanel.add(Author2);
        mainPanel.add(Author1);
        mainPanel.add(textPanel);
        mainPanel.add(bussesComboBox);
        mainPanel.add(maintenance);
        mainPanel.add(RefillBus);
        mainPanel.add(stopBus);
        mainPanel.add(changeDriver);


        //finish setup of the main panel
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setTextPanelText(String txt){
        textPanel.setText(txt);
    } //sets the Text of the PanelText to something

    public Timer getUpdateTimer() {
        return updateTimer;
    } //get timer of the text updater

    private void callMaintenance(int time, String Reason) throws InterruptedException { //starts a timer that calls the maintenance for the desired time

        java.util.Timer t2 = new java.util.Timer(); //added a timer to this function because i could not put the bus to sleep ,
                                                    //it would sleep the GUI , by creating a timer i suspend the thread and
                                                    //sleep the timer so that i unsuspend the thread after the timer
        t2.schedule(new TimerTask() {
            @Override
            public void run() {
                String busKey = bussesComboBox.getSelectedItem().toString();
                try {
                    busManager.maintenance(busKey,time,Reason);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },100);


    }

    private JComboBox<String> addBussesComboBox() { //fills the comboBoxes with the keys of the busses in the busManager HashMap
        String[] optionsToChoose = new String[busManager.getBusses().size()];
        int count = 0;
        for(Map.Entry<String, Bus> entry : busManager.getBusses().entrySet()) {
            Bus value = entry.getValue();
            optionsToChoose[count]=value.getBusID()+"_"+ value.getType();
            count++;
        }
        JComboBox<String> bussesComboBox = new JComboBox<>(optionsToChoose);
        return bussesComboBox;
    }
}
