package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ExchangeAmountMenu extends InventoryHandler {

    private final GenCore plugin;
    private Map<UUID, Integer> tokens = new HashMap<>();


    public ExchangeAmountMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        if (tokens.containsKey(player.getUniqueId()))
            tokens.remove(player.getUniqueId());
        tokens.put(player.getUniqueId(), 1);
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&aExchange");
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                switch (e.getSlot()) {
                    case 0:
                        tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId()) + 1);
                        inventory.setItem(4, createMainItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                    case 1:
                        tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId()) + 8);
                        inventory.setItem(4, createMainItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                    case 2:
                        tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId()) + 64);
                        inventory.setItem(4, createMainItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;

                    case 6:
                        if (tokens.get(player.getUniqueId()) > 1) {
                            tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId()) - 1);
                            inventory.setItem(4, createMainItem());
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                        break;
                    case 7:
                        if (tokens.get(player.getUniqueId()) > 8) {
                            tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId()) - 8);
                            if (tokens.get(player.getUniqueId()) == 64) {
                                inventory.setItem(4, createMainItem());
                            } else {
                                inventory.setItem(4, createMainItem());
                            }
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                        break;
                    case 8:
                        if (tokens.get(player.getUniqueId()) > 64) {
                            tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId()) - 64);
                            if (tokens.get(player.getUniqueId()) == 64) {
                                inventory.setItem(4, createMainItem());
                            } else {
                                inventory.setItem(4, createMainItem());
                            }
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                        break;
                    case 4:
                        if (new Double(ecoPlayer.getBalance()) >= tokens.get(player.getUniqueId()) * 25) {
                            new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, (plugin, player1) -> {
                                ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - tokens.get(player.getUniqueId()) * 25);
                                ecoPlayer.setTokens(ecoPlayer.getTokens() + tokens.get(player.getUniqueId()));
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                        .replace("{tokens}", String.valueOf(tokens.get(player.getUniqueId())))
                                        .replace("{money}", String.valueOf(tokens.get(player.getUniqueId()) * 25)));
                                player.closeInventory();
                                new Scoreboard(plugin).createScoreboard(player);
                            }, (plugin, player1) -> {
                                new ExchangeAmountMenu(ecoPlayer.getPlayerMenuUtility(), player1, plugin).open();
                                player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                            }).open();
                        } else {
                            player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            player.closeInventory();
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(4, createMainItem());

        inventory.setItem(0, createPlus1xItem());
        inventory.setItem(1, createPlus8xItem());
        inventory.setItem(2, createPlus64xItem());

        inventory.setItem(6, createMinus1xItem());
        inventory.setItem(7, createMinus8xItem());
        inventory.setItem(8, createMinus64xItem());
    }

    public ItemStack createMainItem() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&5" + tokens.get(player.getUniqueId()) + "x Tokens"));
        if (tokens.get(player.getUniqueId()) > 64) {
            itemStack.setAmount(64);
        } else {
            itemStack.setAmount(tokens.get(player.getUniqueId()));
        }

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&7&oBuy " + tokens.get(player.getUniqueId()) + " tokens for &a" + tokens.get(player.getUniqueId()) * 25 + "$!");
        lore.add(" ");
        lore.add("&bLeft click -> Buy " + tokens.get(player.getUniqueId()));
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createPlus1xItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a+1 Token"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> +1 token");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createPlus8xItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a+8 Tokens"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> +8 tokens");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createPlus64xItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a+64 Tokens"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> +64 tokens");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMinus1xItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&c-1 Tokens"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> -1 tokens");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMinus8xItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&c-8 Tokens"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> -8 tokens");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMinus64xItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&c-64 Tokens"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> -64 tokens");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
