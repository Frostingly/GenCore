package me.frostingly.gencore.serverCore.commands;

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

public class Ban implements CommandExecutor {

    private final GenCore plugin;

    public Ban(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (sender.hasPermission("server.ban")) {
                Component unsuccessfulBan = Component.text("Could not ban that player either because they don't exist or are already banned").color(TextColor.fromHexString("#fc5454"));
                Component reasonNotSpecified = Component.text("Please specify a reason.").color(TextColor.fromHexString("#fc5454"));
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <reason>");
                } else {
                    if (args.length == 1) {
                        sender.sendMessage(reasonNotSpecified);
                        return true;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; ++i) {
                            sb.append(args[i]).append(' ');
                        }
                        Player target = Bukkit.getPlayer(args[0]);
                        String banReason = sb.toString().trim();
                        if (target == null) {
                            //code if target is offline.
                            OfflinePlayer bannedPlayer = Bukkit.getOfflinePlayer(args[0]);
                            if (!plugin.getBannedUUIDs().containsKey(bannedPlayer.getUniqueId())) {
                                Component bannedMessageForModerator = Component.text("Successfully banned " + bannedPlayer.getName() + " for " + '"' + banReason + '"').color(TextColor.fromHexString("#fc5454"));
                                Component bannedMessageForPlayers = Component.text(bannedPlayer.getName() + " has been banned by " + sender.getName() + " (" + (plugin.getBannedUUIDs().size() + 1) + " bans)").color(TextColor.fromHexString("#fc54fc"));
                                sender.sendMessage(bannedMessageForModerator);

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendMessage(bannedMessageForPlayers);
                                }

                                plugin.getBannedUUIDs().put(bannedPlayer.getUniqueId(), banReason);
                            } else {
                                sender.sendMessage(unsuccessfulBan);
                            }
                        } else {
                            //code if target is online
                            if (!plugin.getBannedUUIDs().containsKey(target.getUniqueId())) {
                                Component bannedMessageForModerator = Component.text("Successfully banned " + target.getName() + " for " + '"' + banReason + '"').color(TextColor.fromHexString("#fc5454"));
                                Component bannedMessageForPlayer = Component.text("You have been banned for " + '"' + banReason + '"').color(TextColor.fromHexString("#fc5454"));
                                Component bannedMessageForPlayers = Component.text(target.getName() + " has been banned by " + sender.getName() + " (" + (plugin.getBannedUUIDs().size() + 1) + " bans)").color(TextColor.fromHexString("#fc54fc"));
                                sender.sendMessage(bannedMessageForModerator);
                                target.kick(bannedMessageForPlayer);

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendMessage(bannedMessageForPlayers);
                                }

                                plugin.getBannedUUIDs().put(target.getUniqueId(), banReason);
                            } else {
                                sender.sendMessage(unsuccessfulBan);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
