package me.frostingly.gencore.genData;

import java.text.DecimalFormat;

public class Upgrades {

    private int speed;
    private double quality;
    private int quantity;
    private int moneyFly;

    public Upgrades(int speed, double quality, int quantity, int moneyFly) {
        this.speed = speed;
        this.quality = quality;
        this.quantity = quantity;
        this.moneyFly = moneyFly;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getQuality() {
        DecimalFormat df = new DecimalFormat("0.0#");
        return df.format(quality);
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMoneyFly() {
        return moneyFly;
    }

    public void setMoneyFly(int moneyFly) {
        this.moneyFly = moneyFly;
    }

}
