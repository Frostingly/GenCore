package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.servercore.commands.PluginCMD;
import me.frostingly.gencore.servercore.economy.SellMethods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class ChestRightClick implements Listener {

    private final GenCore plugin;

    public ChestRightClick(GenCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void ChestRightClick(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (player.getInventory().getItemInMainHand().isSimilar(new PluginCMD(plugin).createSellWand())) {
                e.setCancelled(true);
                new SellMethods(plugin).sellAll(e.getInventory(), player);
            }
        }
    }
}
