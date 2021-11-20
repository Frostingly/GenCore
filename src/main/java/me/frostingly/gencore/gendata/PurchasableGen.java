package me.frostingly.gencore.gendata;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PurchasableGen {

    private final ItemStack itemStack;
    private final List<String> displayLore;
    private final double cost;

    public PurchasableGen(ItemStack itemStack, List<String> displayLore, double cost) {
        this.itemStack = itemStack;
        this.displayLore = displayLore;
        this.cost = cost;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getDisplayLore() {
        return displayLore;
    }

    public double getCost() {
        return cost;
    }

}
