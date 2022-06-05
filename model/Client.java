package model;

public class Client {
    private String destination;
    private Boolean isOnBus;
    private Boolean arrived;

    public Client(String destination, Boolean isOnBus, Boolean arrived) {
        this.destination = destination;
        this.isOnBus = isOnBus;
        this.arrived = arrived;
    }

    public Boolean getArrived() {
        return arrived;
    }

    public Boolean getOnBus() {
        return isOnBus;
    }

    public String getDestination() {
        return destination;
    }
}
