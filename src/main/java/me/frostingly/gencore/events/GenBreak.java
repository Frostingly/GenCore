package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.Scoreboard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GenBreak implements Listener {

    private final GenCore plugin;

    public GenBreak(GenCore plugin) {
        this.plugin = plugin;
    }

    private final List<UUID> onCooldown = new ArrayList<>();

    @EventHandler
    public void GenPickup(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() == EquipmentSlot.HAND) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!onCooldown.contains(player.getUniqueId())) {
                        if (player.isSneaking() && e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
                                if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                                    for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                                        if (e.getClickedBlock().getLocation().equals(ecoPlayer.getOwnedGens().get(i).getLocation())) {
                                            onCooldown.add(player.getUniqueId());

                                            player.getInventory().addItem(ecoPlayer.getOwnedGens().get(i).getItemStack());
                                            ecoPlayer.getOwnedGens().get(i).getArmorStand().remove();
                                            ecoPlayer.getOwnedGens().remove(ecoPlayer.getOwnedGens().get(i));

                                            e.getClickedBlock().getDrops().clear();
                                            e.getClickedBlock().setType(Material.AIR);
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            new Scoreboard(plugin).createScoreboard(player);

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    onCooldown.remove(player.getUniqueId());
                                                }
                                            }.runTaskLater(plugin, 3L);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(Utilities.format("&cSlow down! Please wait before removing another gen."));
                    }
                }
            }.runTaskLater(plugin, 3L);
        }
    }

    @EventHandler
    public void GenBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            for (int genId = 0; genId < ecoPlayer.getOwnedGens().size(); genId++) {
                if (ecoPlayer.getOwnedGens().get(genId).getLocation().equals(e.getBlock().getLocation()) && !(ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString()))) {
                    e.setCancelled(true);
                } else {
                    if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                        for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                            if (e.getBlock().getLocation().equals(ecoPlayer.getOwnedGens().get(i).getLocation())) {
                                e.getBlock().getDrops().clear();
                                ecoPlayer.getOwnedGens().get(i).getArmorStand().remove();
                                player.getInventory().addItem(ecoPlayer.getOwnedGens().get(i).getItemStack());
                                ecoPlayer.getOwnedGens().remove(ecoPlayer.getOwnedGens().get(i));
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                new Scoreboard(plugin).createScoreboard(player);
                            }
                        }
                    }
                }
            }
        }
    }
}
