package com.SpringFrameIntro;

public class Laptop {

    private String make;
    private Chip chip;

    public Laptop(String make,Chip chip)
    {
        this.make = make;
        this.chip=chip;
    }

    public void complie()
    {
        System.out.println("code complied");
    }
}
