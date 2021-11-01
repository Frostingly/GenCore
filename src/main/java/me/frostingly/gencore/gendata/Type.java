package me.frostingly.gencore.gendata;

import org.bukkit.Material;

import java.util.List;

public class Type {

    private final String name;
    private final String type;
    private final Material block;
    private final String outputItem;
    private List<String> defaultLore;

    private final Upgrades defaultUpgrades;
    private final List<UpgradeType> purchasableUpgrades;

    public Type(String name, String type, Material block, String outputItem, List<String> defaultLore, Upgrades defaultUpgrades, List<UpgradeType> purchasableUpgrades) {
        this.name = name;
        this.type = type;
        this.block = block;
        this.outputItem = outputItem;
        this.defaultLore = defaultLore;
        this.defaultUpgrades = defaultUpgrades;
        this.purchasableUpgrades = purchasableUpgrades;
    }

    public String getName() {
        return name;
    }

    public Material getBlock() {
        return block;
    }

    public String getOutputItem() {
        return outputItem;
    }

    public List<String> getLore() {
        return defaultLore;
    }

    public void setLore(List<String> defaultLore) {
        this.defaultLore = defaultLore;
    }

    public String getType() {
        return type;
    }

    public Upgrades getDefaultUpgrades() {
        return defaultUpgrades;
    }

    public List<UpgradeType> getPurchasableUpgrades() {
        return purchasableUpgrades;
    }
}
