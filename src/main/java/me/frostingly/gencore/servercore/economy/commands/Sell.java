package me.frostingly.gencore.servercore.economy.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.economy.SellMethods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Sell implements CommandExecutor {

    private final GenCore plugin;

    public Sell(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("sell")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    switch (args[0]) {
                        case "all":
                            if (player.hasPermission("sell.all")) {
                                new SellMethods(plugin).sellAll(player.getInventory(), player);
                            } else {
                                player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                            }
                            break;
                        default:
                            player.sendMessage(ConfigVariables.INCORRECT_CMD_USAGE.getString(".sell_cmd.message"));
                            break;
                    }
                } else {
                    new SellMethods(plugin).sell(player);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        }
        return false;
    }
}
