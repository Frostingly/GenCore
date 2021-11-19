package me.frostingly.gencore.gendata;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PurchasableGen {

    private final ItemStack itemStack;
    private double cost;

    private final Upgrades defaultUpgrades;
    private final List<UpgradeType> purchasableUpgrades;

    public PurchasableGen(ItemStack itemStack, double cost, Upgrades defaultUpgrades, List<UpgradeType> purchasableUpgrades) {
        this.itemStack = itemStack;
        this.cost = cost;
        this.defaultUpgrades = defaultUpgrades;
        this.purchasableUpgrades = purchasableUpgrades;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getCost() {
        return cost;
    }

    public Upgrades getDefaultUpgrades() {
        return defaultUpgrades;
    }

    public List<UpgradeType> getPurchasableUpgrades() {
        return purchasableUpgrades;
    }

}
