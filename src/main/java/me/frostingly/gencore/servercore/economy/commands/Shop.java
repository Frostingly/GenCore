package me.frostingly.gencore.servercore.economy.commands;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.inventories.ShopMenu;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Shop implements CommandExecutor {

    private final GenCore plugin;

    public Shop(GenCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("shop")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                    if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                        if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                        new ShopMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                    }
                }
            } else {
                sender.sendMessage(ConfigVariables.ONLY_PLAYERS_CAN_EXEC_CMD);
            }
        }
        return false;
    }
}
