package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.inventories.GenMenu;
import me.frostingly.gencore.playerData.EcoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GenMenuOpen implements Listener {

    private final GenCore plugin;

    public GenMenuOpen(GenCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void GenMenuOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                if (player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                        if (ecoPlayer.getOwnedGens().get(i).getLocation().equals(e.getClickedBlock().getLocation())) {
                            new GenMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, ecoPlayer.getOwnedGens().get(i)).open();
                        }
                    }
                }
            }
        }
    }

}
