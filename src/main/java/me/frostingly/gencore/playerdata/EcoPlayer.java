package me.frostingly.gencore.playerdata;

import me.frostingly.gencore.gendata.Gen;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EcoPlayer {

    private String owner;
    private double balance;
    private int tokens;
    private List<Gen> ownedGens = new ArrayList<>();
    private int maxTotalGens;
    private boolean withdrew;
    private boolean bypass;

    private PlayerMenuUtility playerMenuUtility;

    public EcoPlayer(String owner, double balance, int tokens, int maxTotalGens) {
        this.owner = owner;
        this.balance = balance;
        this.tokens = tokens;
        this.maxTotalGens = maxTotalGens;
    }

    public String getOwner() {
        return owner;
    }

    public String getBalance() {
        DecimalFormat df = new DecimalFormat("0.#");
        return df.format(balance);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public List<Gen> getOwnedGens() {
        return ownedGens;
    }

    public void setOwnedGens(List<Gen> ownedGens) {
        this.ownedGens = ownedGens;
    }

    public int getMaxTotalGens() {
        return maxTotalGens;
    }

    public boolean hasWithdrew() {
        return withdrew;
    }

    public void setWithdrew(boolean withdrew) {
        this.withdrew = withdrew;
    }

    public boolean isBypass() {
        return bypass;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public PlayerMenuUtility getPlayerMenuUtility() {
        return playerMenuUtility;
    }

    public void setPlayerMenuUtility(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }
}
