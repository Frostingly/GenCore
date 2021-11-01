package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClick implements Listener {

    private final GenCore plugin;

    public InventoryClick(GenCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) throws CloneNotSupportedException {
        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;
        if (e.getClickedInventory().getHolder() == null) return;
        if (e.getClickedInventory().getType() != InventoryType.CHEST) return;
        InventoryHolder holder = e.getClickedInventory().getHolder();

        if (holder instanceof InventoryHandler) {
            InventoryHandler inventoryHandler = (InventoryHandler) holder;
            inventoryHandler.handleMenu(e);
        }
    }
}
