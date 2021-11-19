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

public class Unban implements CommandExecutor {

    private final GenCore plugin;

    public Unban(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("unban")) {
            if (sender.hasPermission("gencore.command.unban")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Usage: /unban <player>");
                } else {
                    Component unsuccessfulUnBan = Component.text("Could not unban that player either because they don't exist or aren't banned").color(TextColor.fromHexString("#fc5454"));
                    OfflinePlayer unBannedPlayer = Bukkit.getOfflinePlayer(args[0]);

                    if (plugin.getBannedUUIDs().containsKey(unBannedPlayer.getUniqueId())) {
                        Component unBannedMessageForModerator = Component.text("Successfully unbanned " + unBannedPlayer.getName()).color(TextColor.fromHexString("#fc5454"));
                        Component unBannedMessageForPlayers = Component.text(unBannedPlayer.getName() + " has been unbanned by " + sender.getName()).color(TextColor.fromHexString("#fc54fc"));
                        sender.sendMessage(unBannedMessageForModerator);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(unBannedMessageForPlayers);
                        }

                        plugin.getBannedUUIDs().remove(unBannedPlayer.getUniqueId(), plugin.getBannedUUIDs().get(unBannedPlayer.getUniqueId()));
                    } else {
                        sender.sendMessage(unsuccessfulUnBan);
                    }
                }
            }
        }
        return false;
    }
}
