package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.inventoryHandler.InventoryHandler;
import me.frostingly.gencore.inventoryHandler.PlayerMenuUtility;
import me.frostingly.gencore.playerData.EcoPlayer;
import me.frostingly.gencore.serverCore.ConfigVariables;
import me.frostingly.gencore.serverCore.Scoreboard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ExchangeMenu extends InventoryHandler {

    private final GenCore plugin;

    public ExchangeMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&aExchange");
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
                    case 13:
                        switch (e.getClick()) {
                            case LEFT:
                                if (new Double(ecoPlayer.getBalance()) >= 10) {
                                    new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, new ConfirmCancelMenu.Confirm() {
                                        @Override
                                        public void handle(GenCore plugin, Player player1) {
                                            ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - 25);
                                            ecoPlayer.setTokens(ecoPlayer.getTokens() + 1);
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                    .replace("{tokens}", String.valueOf(1))
                                                    .replace("{money}", String.valueOf(25)));
                                            player.closeInventory();
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    }, new ConfirmCancelMenu.Deny() {
                                        @Override
                                        public void handle(GenCore plugin, Player player) {
                                            new ExchangeMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                        }
                                    }).open();
                                } else {
                                    player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                    player.closeInventory();
                                }
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                break;
                            case RIGHT:
                                if (new Double(ecoPlayer.getBalance()) >= 10) {
                                    new ExchangeAmountMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                } else {
                                    player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                    player.closeInventory();
                                }
                                break;
                        }
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

        inventory.setItem(13, createTokenItem());
        inventory.setItem(22, plugin.createCloseItem());
    }

    public ItemStack createTokenItem() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&5Token"));

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&7&oBuy 1 tokens for &a25$!");
        lore.add(" ");
        lore.add("&bLeft click -> Buy 1");
        lore.add("&bRight click -> Buy more");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");

        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
