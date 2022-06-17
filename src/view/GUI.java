package view;

import controller.controller;
import model.Bus;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TimerTask;

public class GUI extends JFrame {
    private final SpringLayout lyt;
    private final int width;
    private final int heigth;
    private final JPanel gamePanel;
    private final JPanel mainPanel;
    private final controller busManager;
    private JComboBox bussesComboBox;

    public GUI(controller busManager){
        this.busManager = busManager;
        //basic setup of the JFrame
        this.setSize(740, 400);
        this.width =this.getSize().width;
        this.heigth = this.getSize().height;
        var lyt = new SpringLayout();
        this.lyt = lyt;
        this.setTitle("Conway's Game of Life");

        //basic setup of the 2 JPanels , main and game
        this.mainPanel = new JPanel();
        this.gamePanel = new JPanel();
        this.setContentPane(mainPanel);
        gamePanel.setPreferredSize(new Dimension(this.width-100, this.heigth-100));
        mainPanel.setBackground(Color.DARK_GRAY);
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setLayout(lyt);
        mainPanel.setLayout(lyt);

        //constraints for those panels in springs
        lyt.putConstraint(SpringLayout.WEST,gamePanel,10,SpringLayout.WEST,mainPanel);
        lyt.putConstraint(SpringLayout.EAST,gamePanel,-120,SpringLayout.EAST,mainPanel);
        lyt.putConstraint(SpringLayout.NORTH,gamePanel,10,SpringLayout.NORTH,mainPanel);

        //timer for the Actions that need repetition


        int delay = 500; //milliseconds
        JTextPane textPanel = new JTextPane();
        textPanel.setPreferredSize(new Dimension(this.width-200, this.heigth-100));
        gamePanel.add(textPanel);
        mainPanel.add(textPanel);

        lyt.putConstraint(SpringLayout.NORTH,textPanel,0,SpringLayout.NORTH,gamePanel);
        lyt.putConstraint(SpringLayout.WEST,textPanel,0,SpringLayout.WEST,gamePanel);
        //JLabel visor = new JLabel();
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String text="";
                text = busManager.getAllText();
                textPanel.setText(text);
            }
        };
        Timer t1 = new Timer(500,taskPerformer);
        t1.setRepeats(true);
        t1.start();
        //Various ActionListeners for the different buttons in game
        ActionListener maintenanceA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ActionListener stopA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ActionListener changeDriverA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ActionListener refillBusA = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    callMaintenance(7);
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




        /*
        visor.setForeground(Color.white);
        lyt.putConstraint(SpringLayout.NORTH,visor,0,SpringLayout.NORTH,gamePanel);
        lyt.putConstraint(SpringLayout.WEST,visor,0,SpringLayout.WEST,gamePanel);*/


        this.bussesComboBox = addBussesComboBox();
        lyt.putConstraint(SpringLayout.WEST,bussesComboBox,-2,SpringLayout.EAST,maintenance);
        lyt.putConstraint(SpringLayout.NORTH,bussesComboBox,-100,SpringLayout.NORTH,maintenance);

        mainPanel.add(bussesComboBox);
        //gamePanel.add(visor);
        mainPanel.add(maintenance);
        mainPanel.add(RefillBus);
        mainPanel.add(stopBus);
        mainPanel.add(changeDriver);
        mainPanel.add(gamePanel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); //Sets all elements to be visible
    }

    private void callMaintenance(int time) throws InterruptedException {

        java.util.Timer t2 = new java.util.Timer();
        t2.schedule(new TimerTask() {
            @Override
            public void run() {
                String busKey = bussesComboBox.getSelectedItem().toString();
                try {
                    busManager.maintenance(busKey,time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },100);


    }

    private JComboBox<String> addBussesComboBox() {
        String[] optionsToChoose = new String[busManager.getBusses().size()];
        Bus[] busses;
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
