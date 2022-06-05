package model;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private String name;
    private Integer nextStop;
    private Integer previousStop;
    private List<String> clientes=new ArrayList<String>();

    public Location(String name, Integer nextStop, Integer previousStop) {
        this.name = name;
        this.nextStop = nextStop;
        this.previousStop = previousStop;
    }

    public String getName() {
        return name;
    }

    public Integer getNextStop() {
        return nextStop;
    }

    public Integer getPreviousStop() {
        return previousStop;
    }

    public List<String> getClientes() {
        return clientes;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", nextStop=" + nextStop +
                ", previousStop=" + previousStop +
                ", clientes=" + clientes +
                '}';
    }
}
