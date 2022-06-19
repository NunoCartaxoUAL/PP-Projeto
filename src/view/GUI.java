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
        this.busManager = busManager;
        this.setSize(740, 400);
        this.width =this.getSize().width;
        this.heigth = this.getSize().height;
        var lyt = new SpringLayout();
        this.lyt = lyt;
        this.setTitle("PP-Projeto");

        //basic setup of the 2 JPanels , main and game
        this.mainPanel = new JPanel();
        this.setContentPane(mainPanel);

        mainPanel.setBackground(Color.DARK_GRAY);


        mainPanel.setLayout(lyt);

        //timer for the Actions that need repetition

        textPanel = new JTextPane();
        textPanel.setPreferredSize(new Dimension(this.width-200, this.heigth-100));
        textPanel.setBackground(Color.DARK_GRAY);
        textPanel.setForeground(Color.white);
        textPanel.setFont(new Font("Calibri",0, 15));
        mainPanel.add(textPanel);
        lyt.putConstraint(SpringLayout.NORTH,textPanel,10,SpringLayout.NORTH,mainPanel);
        lyt.putConstraint(SpringLayout.WEST,textPanel,10,SpringLayout.WEST,mainPanel);

        //JLabel visor = new JLabel();
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

        //Various ActionListeners for the different buttons in game
        ActionListener maintenanceA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(20,"Maintenance...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ActionListener stopA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(5,"Stopped for 5 seconds");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ActionListener changeDriverA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(10, "Changing driver");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ActionListener refillBusA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(7,"Reffilling Bus");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //Basic setup of buttons

        JButton stopBus = new JButton();
        stopBus.setPreferredSize(new Dimension(150,20));
        stopBus.setText("Stop Bus (5s)");
        stopBus.addActionListener(stopA);
        lyt.putConstraint(SpringLayout.WEST,stopBus,10,SpringLayout.WEST,mainPanel);
        lyt.putConstraint(SpringLayout.NORTH,stopBus,10,SpringLayout.SOUTH,textPanel);

        JButton changeDriver = new JButton();
        changeDriver.setPreferredSize(new Dimension(150,20));
        changeDriver.setText("Change Driver (10s)");
        changeDriver.addActionListener(changeDriverA);
        lyt.putConstraint(SpringLayout.WEST,changeDriver,2,SpringLayout.EAST,stopBus);
        lyt.putConstraint(SpringLayout.NORTH,changeDriver,0,SpringLayout.NORTH,stopBus);

        JButton RefillBus = new JButton();
        RefillBus.setPreferredSize(new Dimension(150,20));
        RefillBus.setText("Refill Bus (7s)");
        RefillBus.addActionListener(refillBusA);
        lyt.putConstraint(SpringLayout.WEST,RefillBus,2,SpringLayout.EAST,changeDriver);
        lyt.putConstraint(SpringLayout.NORTH,RefillBus,0,SpringLayout.NORTH,changeDriver);

        JButton maintenance = new JButton();
        maintenance.setPreferredSize(new Dimension(150,20));
        maintenance.setText("Maintenance (20s)");
        maintenance.addActionListener(maintenanceA);
        lyt.putConstraint(SpringLayout.WEST,maintenance,2,SpringLayout.EAST,RefillBus);
        lyt.putConstraint(SpringLayout.NORTH,maintenance,0,SpringLayout.NORTH,RefillBus);


        JLabel Author1 = new JLabel();
        Author1.setText("Nuno Cartaxo  30007214");
        Author1.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.WEST,Author1,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,Author1,200,SpringLayout.NORTH,textPanel);
        mainPanel.add(Author1);
        JLabel Author2 = new JLabel();
        Author2.setText("Andre Martins 30007252");
        Author2.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.WEST,Author2,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,Author2,15,SpringLayout.SOUTH,Author1);
        mainPanel.add(Author2);
        JLabel Author3 = new JLabel();
        Author3.setText("Andre Santos  30007679");
        Author3.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.WEST,Author3,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,Author3,15,SpringLayout.SOUTH,Author2);
        mainPanel.add(Author3);



        /*
        visor.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.NORTH,visor,0,SpringLayout.NORTH,gamePanel);
        lyt.putConstraint(SpringLayout.WEST,visor,0,SpringLayout.WEST,gamePanel);*/


        this.bussesComboBox = addBussesComboBox();
        lyt.putConstraint(SpringLayout.WEST,bussesComboBox,10,SpringLayout.EAST,textPanel);
        lyt.putConstraint(SpringLayout.NORTH,bussesComboBox,0,SpringLayout.NORTH,textPanel);

        mainPanel.add(bussesComboBox);
        //gamePanel.add(visor);
        mainPanel.add(maintenance);
        mainPanel.add(RefillBus);
        mainPanel.add(stopBus);
        mainPanel.add(changeDriver);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); //Sets all elements to be visible
    }

    public void setTextPanelText(String txt){
        textPanel.setText(txt);
    }

    public Timer getUpdateTimer() {
        return updateTimer;
    }

    private void callMaintenance(int time, String Reason) throws InterruptedException {

        java.util.Timer t2 = new java.util.Timer();
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

    private JComboBox<String> addBussesComboBox() {
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
