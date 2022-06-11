package model;

public class Passenger {
    private String id;
    private String destination;
    private Boolean isOnBus = false;
    private Boolean arrived = false;

    public Passenger(String id, String destination) {
        this.id = id;
        this.destination = destination;
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

    public void setArrived(Boolean arrived) {
        this.arrived = arrived;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id='" + id + '\'' +
                ", destination='" + destination + '\'' +
                ", isOnBus=" + isOnBus +
                ", arrived=" + arrived +
                '}';
    }
}
