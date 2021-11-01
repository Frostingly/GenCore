package me.frostingly.gencore.serverCore.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.serverCore.ConfigVariables;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

    private final GenCore plugin;

    public Gamemode(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode") || cmd.getName().equalsIgnoreCase("gm")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gencore.gamemode")) {
                    if (args.length < 1) {
                        player.sendMessage(Utilities.format(ConfigVariables.INCORRECT_CMD_USAGE.getString(".gamemode_cmd.message")));
                    } else {
                        switch (args[0]) {
                            case "creative":
                                switchGamemode(player, GameMode.CREATIVE);
                                break;
                            case "c":
                                switchGamemode(player, GameMode.CREATIVE);
                                break;
                            case "survival":
                                switchGamemode(player, GameMode.SURVIVAL);
                                break;
                            case "s":
                                switchGamemode(player, GameMode.SURVIVAL);
                                break;
                            case "adventure":
                                switchGamemode(player, GameMode.ADVENTURE);
                                break;
                            case "a":
                                switchGamemode(player, GameMode.ADVENTURE);
                                break;
                            case "spectator":
                                switchGamemode(player, GameMode.SPECTATOR);
                                break;
                            case "sp":
                                switchGamemode(player, GameMode.SPECTATOR);
                                break;
                        }
                    }
                } else {
                    player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        } else if (cmd.getName().equalsIgnoreCase("gmc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gencore.gamemode")) {
                    switchGamemode(player, GameMode.CREATIVE);
                } else {
                    player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        } else if (cmd.getName().equalsIgnoreCase("gms")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gencore.gamemode")) {
                    switchGamemode(player, GameMode.SURVIVAL);
                } else {
                    player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        } else if (cmd.getName().equalsIgnoreCase("gma")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gencore.gamemode")) {
                    switchGamemode(player, GameMode.ADVENTURE);
                } else {
                    player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        } else if (cmd.getName().equalsIgnoreCase("gmsp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gencore.gamemode")) {
                    switchGamemode(player, GameMode.SPECTATOR);
                } else {
                    player.sendMessage(ConfigVariables.NO_PERMISSION_CMD);
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        }
        return false;
    }

    public void switchGamemode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
        player.sendMessage(ConfigVariables.GAMEMODE_CHANGE_MESSAGE
                .replace("{gamemode}", String.valueOf(player.getGameMode())));
    }
}
