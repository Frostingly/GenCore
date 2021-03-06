package me.frostingly.gencore.servercore.economy.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.inventories.ExchangeMenu;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Exchange implements CommandExecutor {

    private final GenCore plugin;

    public Exchange(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("exchange")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                    if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                        if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
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
