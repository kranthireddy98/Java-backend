package com.cache.factory;

public class Main {

    public static void main(String[] args) {
        // We decide which logistics type to use based on configuration or environment
        Logistics logistics;

        String transportType = "sea"; // This could come from a config file

        if (transportType.equals("road")) {
            logistics = new RoadLogistics();
        } else {
            logistics = new SeaLogistics();
        }

        // The client code stays clean and decoupled from concrete classes
        logistics.planDelivery();
    }
}
