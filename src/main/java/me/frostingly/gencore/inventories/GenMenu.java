package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.gendata.Gen;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.Scoreboard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GenMenu extends InventoryHandler {

    private final GenCore plugin;
    private final Gen gen;

    public GenMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin, Gen gen) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        this.gen = gen;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format(this.gen.getType().getName() + " &fMenu");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                switch (e.getSlot()) {
                    case 12:
                        new UpgradesMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, gen).open();
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                    case 14:
                        gen.setActive(!gen.isActive());
                        inventory.setItem(14, createActiveItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                    case 17:
                        player.getInventory().addItem(this.gen.getItemStack());

                        this.gen.getArmorStand().remove();
                        this.gen.getLocation().getBlock().setType(Material.AIR);
                        ecoPlayer.getOwnedGens().remove(this.gen);

                        inventory.close();
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        new Scoreboard(plugin).createScoreboard(player);
                        break;
                    case 22:
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, plugin.createPane());
        }
        inventory.setItem(12, createUpgradesItem());
        inventory.setItem(14, createActiveItem());
        inventory.setItem(17, createPickupItem());
        inventory.setItem(22, plugin.createCloseItem());
    }

    public ItemStack createPickupItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&cPickup"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createActiveItem() {
        ItemStack itemStack = new ItemStack(Material.REDSTONE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.gen.isActive()) {
            itemMeta.setDisplayName(Utilities.format("&a&lACTIVE"));
        } else {
            itemMeta.setDisplayName(Utilities.format("&c&lNOT ACTIVE"));
        }
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Utilities.format("&7Click this item to change the gen's state."));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createUpgradesItem() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&b&lUpgrades"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
