package me.frostingly.gencore.serverCore.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.configuration.RegisterFunctions;
import me.frostingly.gencore.configuration.RegisterGens;
import me.frostingly.gencore.configuration.RegisterTypes;
import me.frostingly.gencore.playerData.EcoPlayer;
import me.frostingly.gencore.serverCore.ConfigVariables;
import me.frostingly.gencore.serverCore.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PluginCMD implements CommandExecutor {

    private final GenCore plugin;

    public PluginCMD(GenCore plugin) {
        this.plugin = plugin;
    }

    public ItemStack createSellWand() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&aSell Wand"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&8&oRight click on chests to");
        lore.add("&8&osell the contents inside them.");
        lore.add(" ");
        lore.add("&b&lRARE");
        itemMeta.setLore(Utilities.formatList(lore));
        itemMeta.addEnchant(Enchantment.LUCK, 1, true);

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gencore") || cmd.getName().equalsIgnoreCase("gc")) {
            if (sender.hasPermission("gencore.command")) {
                switch (args[0]) {
                    case "wand":
                        if (sender.hasPermission("gencore.command.wand")) {
                            switch (args[1]) {
                                case "give":
                                    String playerName = args[2];
                                    if (playerName != null || Bukkit.getPlayer(playerName) != null) {
                                        Player player = Bukkit.getPlayer(playerName);

                                        player.getInventory().addItem(createSellWand());
                                        sender.sendMessage(Utilities.format("&aSuccessfully given " + player.getName() + " a sell wand!"));

                                    } else {
                                        //code if playername doesn't match
                                    }
                                    break;
                            }
                        } else {
                            sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                        }
                        break;
                    case "gen":
                        if (sender.hasPermission("gencore.gens")) {
                            switch (args[1]) {
                                case "give":
                                    String playerName = args[3];
                                    if (playerName != null || Bukkit.getPlayer(playerName) != null) {
                                        Player player = Bukkit.getPlayer(playerName);

                                        player.getInventory().addItem(plugin.getGens().get(args[2]).getItemStack());
                                    } else {
                                        //code if playername doesn't match
                                    }
                                    break;
                            }
                        } else {
                            sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                        }
                        break;
                    case "reload":
                        if (sender.hasPermission("gencore.command.reload")) {
                            new ConfigVariables(plugin).registerVariables();
                            new RegisterFunctions(plugin).regFunctions();
                            new RegisterTypes(plugin).regTypes();
                            new RegisterGens(plugin).regGens();
                            sender.sendMessage(Utilities.format("&aSuccessfully reloaded."));
                        } else {
                            sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                        }
                        break;
                    case "wipe":
                        if (sender.hasPermission("gencore.command.wipe")) {
                            String playerName = args[1];
                            if (playerName != null) {
                                Player player = Bukkit.getPlayer(playerName);
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                                boolean found = false;
                                boolean resetSuccessful = false;

                                for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                                    if (player != null) {
                                        if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                                            found = true;
                                            if (ecoPlayer.getOwnedGens().size() == 0 && new Double(ecoPlayer.getBalance()) == 0 && ecoPlayer.getTokens() == 0) {
                                                resetSuccessful = false;
                                            } else {
                                                resetSuccessful = true;
                                                if (ecoPlayer.getOwnedGens().size() > 0) {
                                                    for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                                                        ecoPlayer.getOwnedGens().get(i).getLocation().getBlock().setType(Material.AIR);
                                                        ecoPlayer.getOwnedGens().get(i).setActive(true);
                                                        ecoPlayer.getOwnedGens().get(i).getArmorStand().remove();
                                                    }
                                                    ecoPlayer.getOwnedGens().clear();
                                                }
                                                ecoPlayer.setBalance(0);
                                                ecoPlayer.setTokens(0);
                                                ecoPlayer.setWithdrew(false);
                                            }
                                        }
                                    } else {
                                        if (ecoPlayer.getOwner().equalsIgnoreCase(offlinePlayer.getUniqueId().toString())) {
                                            found = true;
                                            if (ecoPlayer.getOwnedGens().size() == 0 && new Double(ecoPlayer.getBalance()) == 0 && ecoPlayer.getTokens() == 0) {
                                                resetSuccessful = false;
                                            } else {
                                                resetSuccessful = true;
                                                if (ecoPlayer.getOwnedGens().size() > 0) {
                                                    for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                                                        ecoPlayer.getOwnedGens().get(i).getLocation().getBlock().setType(Material.AIR);
                                                        ecoPlayer.getOwnedGens().get(i).setActive(true);
                                                        ecoPlayer.getOwnedGens().get(i).getArmorStand().remove();
                                                    }
                                                    ecoPlayer.getOwnedGens().clear();
                                                }
                                                ecoPlayer.setBalance(0);
                                                ecoPlayer.setTokens(0);
                                                ecoPlayer.setWithdrew(false);
                                            }
                                        }
                                    }
                                }

                                if (found) {
                                    if (resetSuccessful) {
                                        sender.sendMessage(Utilities.format("&aSuccessfully reset " + playerName));
                                        if (player != null) {
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    } else {
                                        sender.sendMessage(Utilities.format("&cCould not find anything to reset for " + playerName));
                                    }
                                } else {
                                    sender.sendMessage(Utilities.format("&cCould not find a player by the name " + playerName));
                                }
                            } else {
                                //code if playername doesn't match
                            }
                        } else {
                            sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                        }
                        break;
                }
            }
        } else {
            sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
        }
        return false;
    }
}
