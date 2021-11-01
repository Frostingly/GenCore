package me.frostingly.gencore.serverCore.economy.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.inventories.ExchangeMenu;
import me.frostingly.gencore.playerData.EcoPlayer;
import me.frostingly.gencore.serverCore.ConfigVariables;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Exchange implements CommandExecutor {

    private final GenCore plugin;

    public Exchange(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("exchange")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                    if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                        new ExchangeMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    }
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        }
        return false;
    }
}
