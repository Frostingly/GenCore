package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.gendata.Gen;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final GenCore plugin;

    public PlayerQuit(GenCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        boolean foundEcoPlayer = false;
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                foundEcoPlayer = true;
                for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                    Gen gen = ecoPlayer.getOwnedGens().get(i);
                    gen.setActive(false);
                }
            }
        }
        if (!foundEcoPlayer) {
            EcoPlayer ecoPlayer = new EcoPlayer(player.getUniqueId().toString(), 0, 0, 20);
            plugin.getEcoPlayers().add(ecoPlayer);
        }
        e.setQuitMessage(ConfigVariables.QUIT_MESSAGE
                .replace("{player_name}", player.getName()));
        for (Player online : Bukkit.getOnlinePlayers()) {
            new Scoreboard(plugin).createScoreboard(online);
        }
    }

}
