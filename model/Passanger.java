package model;

public class Passanger {
    private String id;
    private String destination;
    private Boolean isOnBus = false;
    private Boolean arrived = false;

    public Passanger(String id, String destination) {
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

    @Override
    public String toString() {
        return "Passanger{" +
                "id='" + id + '\'' +
                ", destination='" + destination + '\'' +
                ", isOnBus=" + isOnBus +
                ", arrived=" + arrived +
                '}';
    }
}
