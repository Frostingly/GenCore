package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.gendata.Gen;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final GenCore plugin;

    public PlayerJoin(GenCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        boolean foundEcoPlayer = false;
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                foundEcoPlayer = true;
                for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                    Gen gen = ecoPlayer.getOwnedGens().get(i);
                    gen.setActive(true);
                }
            }
        }
        if (!foundEcoPlayer) {
            EcoPlayer ecoPlayer = new EcoPlayer(player.getUniqueId().toString(), 0, 0, 20, new PlayerMenuUtility(player));
            plugin.getEcoPlayers().add(ecoPlayer);
        }
        e.setJoinMessage(ConfigVariables.JOIN_MESSAGE
                .replace("{player_name}", player.getName()));
        for (Player online : Bukkit.getOnlinePlayers()) {
            new Scoreboard(plugin).createScoreboard(online);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinAttempt(AsyncPlayerPreLoginEvent e) {
        if (plugin.getBannedUUIDs().containsKey(e.getUniqueId())) {
            Component reasonComponent;
            if (plugin.getBannedUUIDs().get(e.getUniqueId()) != null) {
                reasonComponent = Component.text("You have been banned for " + '"' + plugin.getBannedUUIDs().get(e.getUniqueId()) + '"').color(TextColor.fromHexString("#fc5454"));
            } else {
                reasonComponent = Component.text("You have been banned for undefined. Contact a staff member.").color(TextColor.fromHexString("#fc5454"));
            }
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, reasonComponent);
        }
    }
}
