package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ShopMenu extends InventoryHandler {

    private final GenCore plugin;

    public ShopMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&bShop");
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getSlot() == 0) {
            for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                    if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                    new GenShopMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(0, new ItemStack(Material.COAL_BLOCK));
    }
}
