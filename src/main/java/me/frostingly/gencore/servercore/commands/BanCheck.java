package me.frostingly.gencore.servercore.commands;

import me.frostingly.gencore.GenCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BanCheck implements CommandExecutor {

    private final GenCore plugin;

    public BanCheck(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("bancheck")) {
            if (sender.hasPermission("gencore.command.bancheck")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bancheck <player>");
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    Component banCheck;
                    if (target == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (plugin.getBannedUUIDs().containsKey(offlinePlayer.getUniqueId())) {
                            if (plugin.getBannedUUIDs().get(offlinePlayer.getUniqueId()) != null) {
                                banCheck = Component.text(offlinePlayer.getName() + " is banned for the reason of " + '"' + plugin.getBannedUUIDs().get(offlinePlayer.getUniqueId()) + '"').color(TextColor.fromHexString("#fc5454"));
                            } else {
                                banCheck = Component.text(offlinePlayer.getName() + " is banned for the reason of " + '"' + "undefined. Contact a staff member." + '"').color(TextColor.fromHexString("#fc5454"));
                            }
                        } else {
                            banCheck = Component.text(offlinePlayer.getName() + " is not banned").color(TextColor.fromHexString("#fc5454"));
                        }
                        sender.sendMessage(banCheck);
                        //code if target is offline
                    } else {
                        if (plugin.getBannedUUIDs().containsKey(target.getUniqueId())) {
                            if (plugin.getBannedUUIDs().get(target.getUniqueId()) != null) {
                                banCheck = Component.text(target.getName() + " is banned for the reason of " + '"' + plugin.getBannedUUIDs().get(target.getUniqueId()) + '"').color(TextColor.fromHexString("#fc5454"));
                            } else {
                                banCheck = Component.text(target.getName() + " is banned for the reason of " + '"' + "undefined. Contact a staff member." + '"').color(TextColor.fromHexString("#fc5454"));
                            }
                        } else {
                            banCheck = Component.text(target.getName() + " is not banned").color(TextColor.fromHexString("#fc5454"));
                        }
                        sender.sendMessage(banCheck);
                        //code if target is online
                    }
                }
            }
        }
        return false;
    }
}
