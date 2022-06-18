package model;

public class Passenger {
    private String id;
    private String destination;
    private Boolean arrived = false;

    public Passenger(String id, String destination) {
        this.id = id;
        this.destination = destination;
    }

    public Boolean getArrived() {
        return arrived;
    }

    public String getDestination() {
        return destination;
    }

    public void setArrived(Boolean arrived) {
        this.arrived = arrived;
    }


}
