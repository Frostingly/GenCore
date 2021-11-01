package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmCancelMenu extends InventoryHandler {

    @FunctionalInterface
    interface Confirm {
        void handle(GenCore plugin, Player player);
    }

    @FunctionalInterface
    interface Deny {
        void handle(GenCore plugin, Player player);
    }


    private final GenCore plugin;

    private final Confirm confirm;
    private final Deny deny;

    public ConfirmCancelMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin, Confirm confirm, Deny deny) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        this.confirm = confirm;
        this.deny = deny;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&a&lConfirm &fMenu");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        e.setCancelled(true);
        if (e.getSlot() < 3) {
            confirm.handle(plugin, player);
        }

        if (e.getSlot() > 8 && e.getSlot() < 12) {
            confirm.handle(plugin, player);
        }

        if (e.getSlot() > 17 && e.getSlot() < 21) {
            confirm.handle(plugin, player);
        }

        if (e.getSlot() > 5 && e.getSlot() < 9) {
            deny.handle(plugin, player);
        }

        if (e.getSlot() > 14 && e.getSlot() < 18) {
            deny.handle(plugin, player);
        }

        if (e.getSlot() > 23) {
            deny.handle(plugin, player);
        }
    }

    @Override
    public void setMenuItems() {
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, plugin.createPane());
        }

        for (int i = 0; i < 27; i++) {
            if (i < 3) {
                inventory.setItem(i, createConfirmItem());
            }
            if (i > 8 && i < 12) {
                inventory.setItem(i, createConfirmItem());
            }
            if (i > 17 && i < 21) {
                inventory.setItem(i, createConfirmItem());
            }

            if (i > 5 && i < 9) {
                inventory.setItem(i, createCancelItem());
            }

            if (i > 14 && i < 18) {
                inventory.setItem(i, createCancelItem());
            }

            if (i > 23) {
                inventory.setItem(i, createCancelItem());
            }
        }
    }

    public ItemStack createConfirmItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&aConfirm"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createCancelItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&cCancel"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
