package me.frostingly.gencore.servercore.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bypass implements CommandExecutor {

    private final GenCore plugin;

    public Bypass(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("bypass")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gencore.command.bypass")) {
                    for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                        if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                            ecoPlayer.setBypass(!ecoPlayer.isBypass());
                            player.sendMessage(Utilities.format("&aSuccessfully set bypass to " + ecoPlayer.isBypass()));
                        }
                    }
                } else {
                    player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        }
        return false;
    }
}
