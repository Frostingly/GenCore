package me.frostingly.gencore.servercore.economy;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.gendata.Type;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

public class SellMethods {

    private final GenCore plugin;

    public SellMethods(GenCore plugin) {
        this.plugin = plugin;
    }

    public void sell(Player player) {
        ItemStack itemStackInHand = player.getInventory().getItemInMainHand();
        if (itemStackInHand.getType() == Material.AIR) {
            player.sendMessage(ConfigVariables.COULD_NOT_SELL);
        } else {
            for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                    for (Type type : plugin.getTypes().values()) {
                        if (type.getType() != null) {
                            ItemStack valuable = null;
                            if (itemStackInHand.hasItemMeta()) {
                                ItemStack sellAbleItem = null;
                                if (type.getOutputItem().contains("function")) {
                                    for (String functionName : plugin.getFunctions().keySet()) {
                                        String rawFunctionName = StringUtils.substringBetween(type.getOutputItem(), "(", ")");
                                        if (functionName.equalsIgnoreCase(rawFunctionName)) {
                                            Configuration config = plugin.getFunctions().get(functionName);
                                            if (config.getString("function.return").equalsIgnoreCase("ItemStack")) {
                                                sellAbleItem = new ItemStack(Material.valueOf(config.getString("function.itemstack.material")));
                                                ItemMeta itemMeta = sellAbleItem.getItemMeta();
                                                itemMeta.setDisplayName(Utilities.format(config.getString("function.itemstack.display_name")));
                                                sellAbleItem.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                }
                                if (sellAbleItem != null) {
                                    if (sellAbleItem.getItemMeta().getDisplayName().equalsIgnoreCase(itemStackInHand.getItemMeta().getDisplayName())) {
                                        valuable = itemStackInHand;
                                        double sellPriceEach = 0;
                                        for (String loreLine : itemStackInHand.getItemMeta().getLore()) {
                                            if (loreLine.contains("price")) {
                                                String[] line = loreLine.split(": ");
                                                sellPriceEach = Double.parseDouble(ChatColor.stripColor(line[1].replace("$", "").trim()));
                                                ecoPlayer.setBalance(Double.parseDouble(ecoPlayer.getBalance()) + (sellPriceEach * valuable.getAmount()));
                                            }
                                        }
                                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
                                        DecimalFormat df = new DecimalFormat("0.##");
                                        player.sendMessage(ConfigVariables.SELL_MESSAGE
                                                .replace("{items}", String.valueOf(valuable.getAmount()))
                                                .replace("{money}", df.format(new Double(sellPriceEach * valuable.getAmount()))));
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                        new Scoreboard(plugin).createScoreboard(player);
                                    }
                                } else {
                                    player.sendMessage(ConfigVariables.COULD_NOT_SELL);
                                }
                            }
                            if (valuable == null) {
                                player.sendMessage(ConfigVariables.COULD_NOT_SELL);
                            }
                        }
                    }
                }
            }
        }
    }

    public void sellAll(Inventory inventory, Player player) {
        if (inventory.getType() == InventoryType.PLAYER || inventory.getType() == InventoryType.CHEST) {
            for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                    Map<Integer, ItemStack> sellableItems = new HashMap<>();
                    if (inventory.getType() == InventoryType.PLAYER) {
                        for (int i = 0; i < 35; i++) {
                            for (Type type : plugin.getTypes().values()) {
                                if (type.getType() != null) {
                                    if (inventory.getItem(i) != null) {
                                        if (inventory.getItem(i).hasItemMeta()) {
                                            ItemStack sellAbleItem = null;
                                            if (type.getOutputItem().contains("function")) {
                                                for (String functionName : plugin.getFunctions().keySet()) {
                                                    String rawFunctionName = StringUtils.substringBetween(type.getOutputItem(), "(", ")");
                                                    if (functionName.equalsIgnoreCase(rawFunctionName)) {
                                                        Configuration config = plugin.getFunctions().get(functionName);
                                                        if (config.getString("function.return").equalsIgnoreCase("ItemStack")) {
                                                            sellAbleItem = new ItemStack(Material.valueOf(config.getString("function.itemstack.material")));
                                                            ItemMeta itemMeta = sellAbleItem.getItemMeta();
                                                            itemMeta.setDisplayName(Utilities.format(config.getString("function.itemstack.display_name")));
                                                            sellAbleItem.setItemMeta(itemMeta);
                                                        }
                                                    }
                                                }
                                            }
                                            if (sellAbleItem != null) {
                                                if (sellAbleItem.getItemMeta().getDisplayName().equalsIgnoreCase(inventory.getItem(i).getItemMeta().getDisplayName())) {
                                                    sellableItems.put(i, inventory.getItem(i));
                                                }
                                            } else {
                                                player.sendMessage(ConfigVariables.COULD_NOT_SELL);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (inventory.getType() == InventoryType.CHEST) {
                        for (int i = 0; i < inventory.getSize(); i++) {
                            for (Type type : plugin.getTypes().values()) {
                                if (type.getType() != null) {
                                    ItemStack sellAbleItem = null;
                                    if (type.getOutputItem().contains("function")) {
                                        for (String functionName : plugin.getFunctions().keySet()) {
                                            String rawFunctionName = StringUtils.substringBetween(type.getOutputItem(), "(", ")");
                                            if (functionName.equalsIgnoreCase(rawFunctionName)) {
                                                Configuration config = plugin.getFunctions().get(functionName);
                                                if (config.getString("function.return").equalsIgnoreCase("ItemStack")) {
                                                    sellAbleItem = new ItemStack(Material.valueOf(config.getString("function.itemstack.material")));
                                                    ItemMeta itemMeta = sellAbleItem.getItemMeta();
                                                    itemMeta.setDisplayName(Utilities.format(config.getString("function.itemstack.display_name")));
                                                    sellAbleItem.setItemMeta(itemMeta);
                                                }
                                            }
                                        }
                                    }
                                    if (sellAbleItem != null) {
                                        if (sellAbleItem.isSimilar(inventory.getItem(i))) {
                                            sellableItems.put(i, inventory.getItem(i));
                                        }
                                    } else {
                                        player.sendMessage(ConfigVariables.COULD_NOT_SELL);
                                    }
                                }
                            }
                        }
                    }
                    if (sellableItems.size() > 0) {
                        int totalItemsSold = 0;
                        double totalItemsSoldForPrice = 0;
                        for (Integer i : sellableItems.keySet()) {
                            for (String loreLine : sellableItems.get(i).getItemMeta().getLore()) {
                                if (loreLine.contains("price")) {
                                    String[] line = loreLine.split(": ");
                                    totalItemsSoldForPrice += Double.parseDouble(ChatColor.stripColor(line[1].replace("$", "").trim())) * sellableItems.get(i).getAmount();
                                }
                            }
                            inventory.remove(sellableItems.get(i));
                            totalItemsSold = totalItemsSold + sellableItems.get(i).getAmount();
                        }
                        DecimalFormat df = new DecimalFormat("0.##");
                        player.sendMessage(ConfigVariables.SELL_MESSAGE
                                .replace("{items}", String.valueOf(totalItemsSold))
                                .replace("{money}", df.format(totalItemsSoldForPrice)));
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        ecoPlayer.setBalance(Double.parseDouble(ecoPlayer.getBalance()) + totalItemsSoldForPrice);
                        new Scoreboard(plugin).createScoreboard(player);
                    } else {
                        player.sendMessage(ConfigVariables.COULD_NOT_SELL);
                    }
                }
            }
        }
    }
}
