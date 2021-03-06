package me.frostingly.gencore.servercore.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.configuration.RegisterFunctions;
import me.frostingly.gencore.configuration.RegisterGens;
import me.frostingly.gencore.configuration.RegisterPurchasableGens;
import me.frostingly.gencore.configuration.RegisterTypes;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
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
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("gencore") || cmd.getName().equalsIgnoreCase("gc")) {
            if (sender.hasPermission("gencore.command")) {
                if (args.length == 0) {
                    sender.sendMessage(Utilities.format(ConfigVariables.INCORRECT_CMD_USAGE.getString(".plugin_cmd.message")));
                } else {
                    switch (args[0]) {
                        case "wand":
                            if (sender.hasPermission("gencore.command.wand")) {
                                if (args.length == 1) {
                                    sender.sendMessage(Utilities.format("&cUsage: /gc wand give <player>"));
                                } else {
                                    switch (args[1]) {
                                        case "give":
                                            if (args.length == 2 || args.length >= 4) {
                                                sender.sendMessage(Utilities.format("&cUsage: /gc wand give <player>"));
                                            } else {
                                                String playerName = args[2];
                                                if (playerName != null) {
                                                    if (Bukkit.getPlayer(playerName) != null) {
                                                        Player player = Bukkit.getPlayer(playerName);

                                                        player.getInventory().addItem(createSellWand());
                                                        sender.sendMessage(Utilities.format("&aSuccessfully given " + player.getName() + " a sell wand!"));
                                                    } else {
                                                        sender.sendMessage(Utilities.format("&cUsage: /gc wand give <player>"));
                                                    }
                                                } else {
                                                    //code if playername doesn't match
                                                    sender.sendMessage(Utilities.format("&cUsage: /gc wand give <player>"));
                                                }
                                            }
                                            break;
                                        default:
                                            sender.sendMessage(Utilities.format("&cUsage: /gc wand give <player>"));
                                            break;
                                    }
                                }
                            } else {
                                sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                            }
                            break;
                        case "gen":
                            if (sender.hasPermission("gencore.gens")) {
                                if (args.length == 1) {
                                    sender.sendMessage(Utilities.format("&cUsage: /gc gen give <gen_id> <player>"));
                                } else {
                                    switch (args[1]) {
                                        case "give":
                                            if (args.length == 2) {
                                                sender.sendMessage(Utilities.format("&cUsage: /gc gen give <gen_id> <player>"));
                                            } else {
                                                if (args.length == 3 || args.length >= 5) {
                                                    sender.sendMessage(Utilities.format("&cUsage: /gc gen give <gen_id> <player>"));
                                                } else {
                                                    String playerName = args[3];
                                                    if (playerName != null) {
                                                        if (Bukkit.getPlayer(playerName) != null) {
                                                            Player player = Bukkit.getPlayer(playerName);

                                                            if (plugin.getGens().get(args[2]) != null) {
                                                                player.getInventory().addItem(plugin.getGens().get(args[2]).getItemStack());
                                                            } else {
                                                                sender.sendMessage(Utilities.format("&c" + "\"" + args[2] + "\"" + " is not a gen."));
                                                            }
                                                        } else {
                                                            sender.sendMessage(Utilities.format("&cUsage: /gc gen give <gen_id> <player>"));
                                                        }
                                                    } else {
                                                        sender.sendMessage(Utilities.format("&cUsage: /gc gen give <gen_id> <player>"));
                                                    }
                                                }
                                            }
                                            break;
                                        default:
                                            sender.sendMessage(Utilities.format("&cUsage: /gc gen give <gen_id> <player>"));
                                            break;
                                    }
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
                                new RegisterPurchasableGens(plugin).regPurchasableGens();
                                sender.sendMessage(Utilities.format("&aSuccessfully reloaded."));
                            } else {
                                sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                            }
                            break;
                        case "balance":
                            if (sender.hasPermission("gencore.command.balance")) {
                                if (args.length == 1) {
                                    sender.sendMessage(Utilities.format("&cUsage: /gc balance <give|remove> <player> <amount>"));
                                } else {
                                    switch (args[1]) {
                                        case "give":
                                            if (args.length == 2) {
                                                sender.sendMessage(Utilities.format("&cUsage: /gc balance <give|remove> <player> <amount>"));
                                            } else {
                                                if (args.length == 3 || args.length >= 5) {
                                                    sender.sendMessage(Utilities.format("&cUsage: /gc balance <give|remove> <player> <amount>"));
                                                } else {
                                                    String playerName = args[2];
                                                    String amount = args[3];
                                                    Player player = Bukkit.getPlayer(playerName);
                                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                                                    for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                                                        if (player != null) {
                                                            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                                                                ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) + new Double(amount));
                                                                sender.sendMessage(Utilities.format("&aSuccessfully given " + Utilities.formatNumber(new Double(amount).longValue()) + "$ to " + playerName));
                                                                new Scoreboard(plugin).createScoreboard(player);
                                                            }
                                                        } else {
                                                            if (offlinePlayer.hasPlayedBefore()) {
                                                                if (ecoPlayer.getOwner().equalsIgnoreCase(offlinePlayer.getUniqueId().toString())) {
                                                                    ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) + new Double(amount));
                                                                    sender.sendMessage(Utilities.format("&aSuccessfully given " + Utilities.formatNumber(new Double(amount).longValue()) + "$ to " + playerName));
                                                                }
                                                            } else {
                                                                sender.sendMessage(Utilities.format("&cCould not find a player by the name " + args[2]));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case "remove":
                                            if (args.length == 2) {
                                                sender.sendMessage(Utilities.format("&cUsage: /gc balance <give|remove> <player> <amount>"));
                                            } else {
                                                if (args.length == 3 || args.length >= 5) {
                                                    sender.sendMessage(Utilities.format("&cUsage: /gc balance <give|remove> <player> <amount>"));
                                                } else {
                                                    String playerName = args[2];
                                                    String amount = args[3];
                                                    Player player = Bukkit.getPlayer(playerName);
                                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                                                    for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                                                        if (player != null) {
                                                            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                                                                if (new Double(amount) <= new Double(ecoPlayer.getBalance())) {
                                                                    ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - new Double(amount));
                                                                    sender.sendMessage(Utilities.format("&aSuccessfully removed " + Utilities.formatNumber(new Double(amount).longValue()) + "$ from " + playerName));
                                                                    new Scoreboard(plugin).createScoreboard(player);
                                                                } else {
                                                                    sender.sendMessage(Utilities.format("&c" + playerName + " doesn't have enough money."));
                                                                }
                                                            }
                                                        } else {
                                                            if (offlinePlayer.hasPlayedBefore()) {
                                                                if (ecoPlayer.getOwner().equalsIgnoreCase(offlinePlayer.getUniqueId().toString())) {
                                                                    if (new Double(amount) <= new Double(ecoPlayer.getBalance())) {
                                                                        ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - new Double(amount));
                                                                        sender.sendMessage(Utilities.format("&aSuccessfully removed " + Utilities.formatNumber(new Double(amount).longValue()) + "$ from " + playerName));
                                                                    } else {
                                                                        sender.sendMessage(Utilities.format("&c" + playerName + " does not have enough money."));
                                                                    }
                                                                }
                                                            } else {
                                                                sender.sendMessage(Utilities.format("&cCould not find a player by the name " + args[2]));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        default:
                                            sender.sendMessage(Utilities.format("&cUsage: /gc balance <give|remove> <amount>"));
                                            break;
                                    }
                                }
                            }
                            break;
                        case "wipe":
                            if (sender.hasPermission("gencore.command.wipe")) {
                                if (args.length == 1 || args.length >= 3) {
                                    sender.sendMessage(Utilities.format("&cUsage: /gc wipe <player>"));
                                } else {
                                    String playerName = args[1];
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
                                                player.getInventory().clear();
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
                                        }
                                    }
                                }
                            } else {
                                sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                            }
                            break;
                        default:
                            sender.sendMessage(Utilities.format(ConfigVariables.INCORRECT_CMD_USAGE.getString(".plugin_cmd.message")));
                            break;
                    }
                }
            }
        } else {
            sender.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
        }
        return false;
    }
}
