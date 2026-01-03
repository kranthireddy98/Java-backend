package com.SpringFrameIntro;

public class Computer {

    private int model;
    private Laptop laptop;

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        System.out.println("model assigned by spring");
        this.model = model;
    }

    public  Computer(){
        System.out.println("Computing...");
    }
}
