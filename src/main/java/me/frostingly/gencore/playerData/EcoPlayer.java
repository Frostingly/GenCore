package me.frostingly.gencore.playerData;

import me.frostingly.gencore.genData.Gen;
import me.frostingly.gencore.inventoryHandler.PlayerMenuUtility;

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

    private PlayerMenuUtility playerMenuUtility;

    public EcoPlayer(String owner, double balance, int tokens, int maxTotalGens, PlayerMenuUtility playerMenuUtility) {
        this.owner = owner;
        this.balance = balance;
        this.tokens = tokens;
        this.maxTotalGens = maxTotalGens;
        this.playerMenuUtility = playerMenuUtility;
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

    public PlayerMenuUtility getPlayerMenuUtility() {
        return playerMenuUtility;
    }

    public void setPlayerMenuUtility(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }
}
