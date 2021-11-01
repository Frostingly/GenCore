package me.frostingly.gencore.genData;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class Gen {

    private Type type;
    private ItemStack itemStack;
    private Upgrades upgrades;
    private boolean active = true;

    private ArmorStand armorStand;
    private Location location;

    public Gen(Type type, Upgrades upgrades) {
        this.type = type;
        this.upgrades = upgrades;
    }

    public Type getType() {
        return type;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(Upgrades upgrades) {
        this.upgrades = upgrades;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean canUpgrade(Gen gen, UpgradeType type) {
        if (gen.getType().getPurchasableUpgrades().contains(type)) return true;
        return false;
    }
}
