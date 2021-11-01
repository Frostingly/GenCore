package me.frostingly.gencore.events;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.genData.Gen;
import me.frostingly.gencore.genData.Upgrades;
import me.frostingly.gencore.playerData.EcoPlayer;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.serverCore.ConfigVariables;
import me.frostingly.gencore.serverCore.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GenPlace implements Listener {

    private final GenCore plugin;

    public GenPlace(GenCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void GenPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                for (Gen gen : plugin.getGens().values()) {
                    if (Utilities.format(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase(Utilities.format(gen.getType().getName()))) {
                        int speed = 1;
                        double quality = 1;
                        int quantity = 1;
                        int moneyFly = 1;

                        if (gen.getType().getDefaultUpgrades().getSpeed() != 1) speed = gen.getType().getDefaultUpgrades().getSpeed();
                        if (new Double(gen.getType().getDefaultUpgrades().getQuality()) != 1) quality = new Double(gen.getType().getDefaultUpgrades().getQuality());
                        if (gen.getType().getDefaultUpgrades().getQuantity() != 1) quantity = gen.getType().getDefaultUpgrades().getQuantity();
                        if (gen.getType().getDefaultUpgrades().getMoneyFly() != 1) moneyFly = gen.getType().getDefaultUpgrades().getMoneyFly();

                        if (gen.getUpgrades().getSpeed() != speed) speed = gen.getUpgrades().getSpeed();
                        if (new Double(gen.getUpgrades().getQuality()) != quality) quality = new Double(gen.getUpgrades().getQuality());
                        if (gen.getUpgrades().getQuantity() != quantity) quantity = gen.getUpgrades().getQuantity();
                        if (gen.getUpgrades().getMoneyFly() != moneyFly) moneyFly = gen.getUpgrades().getMoneyFly();

                        Gen playersGen = new Gen(gen.getType(), new Upgrades(speed, quality, quantity, moneyFly));
                        ItemStack itemStack = new ItemStack(e.getItemInHand().getType());
                        ItemMeta itemMeta = itemStack.getItemMeta();

                        itemMeta.setDisplayName(Utilities.format(e.getItemInHand().getItemMeta().getDisplayName()));
                        itemMeta.setLore(Utilities.formatList(e.getItemInHand().getItemMeta().getLore()));
                        itemStack.setAmount(1);
                        itemStack.setItemMeta(itemMeta);

                        playersGen.setItemStack(itemStack);
                        playersGen.setLocation(e.getBlock().getLocation());

                        playersGen.getItemStack().getLore().forEach(loreLine -> {
                            if (loreLine.contains("Speed")) {
                                String[] line = loreLine.split(" ");
                                for (int i = 0; i < line.length; i++) {
                                    if (line[i].contains("Speed")) {
                                        playersGen.getUpgrades().setSpeed(Integer.parseInt(ChatColor.stripColor(line[i + 1].trim())));
                                    }
                                }
                            } else if (loreLine.contains("Quality")) {
                                String[] line = loreLine.split(" ");
                                for (int i = 0; i < line.length; i++) {
                                    if (line[i].contains("Quality")) {
                                        playersGen.getUpgrades().setQuality(Double.parseDouble(ChatColor.stripColor(line[i + 1].trim())));
                                    }
                                }
                            } else if (loreLine.contains("Quantity")) {
                                String[] line = loreLine.split(" ");
                                for (int i = 0; i < line.length; i++) {
                                    if (line[i].contains("Quantity")) {
                                        playersGen.getUpgrades().setQuantity(Integer.parseInt(ChatColor.stripColor(line[i + 1].trim())));
                                    }
                                }
                            } else if (loreLine.contains("Money")) {
                                String[] line = loreLine.split(" ");
                                for (int i = 0; i < line.length; i++) {
                                    if (line[i].contains("Money")) {
                                        playersGen.getUpgrades().setMoneyFly(Integer.parseInt(ChatColor.stripColor(line[i + 2].trim())));
                                    }
                                }
                            }
                        });

                        if (!player.isOp()) {
                            if (ecoPlayer.getOwnedGens().size() < ecoPlayer.getMaxTotalGens()) {
                                Location hologramLocation = new Location(e.getBlock().getLocation().getWorld(),
                                        e.getBlock().getLocation().getX() + 0.5,
                                        e.getBlock().getLocation().getY() - 1,
                                        e.getBlock().getLocation().getZ() + 0.5);
                                ArmorStand hologram = (ArmorStand) e.getBlock().getLocation().getWorld().spawnEntity(hologramLocation, EntityType.ARMOR_STAND);
                                hologram.setCustomName(Utilities.format(playersGen.getType().getName()));
                                hologram.setCustomNameVisible(true);
                                hologram.setGravity(false);
                                hologram.setInvisible(true);
                                hologram.setInvulnerable(true);
                                playersGen.setArmorStand(hologram);
                                ecoPlayer.getOwnedGens().add(playersGen);
                                plugin.output(playersGen);
                                player.sendMessage(ConfigVariables.GEN_PLACED_MESSAGE
                                        .replace("{gen}", Utilities.format(playersGen.getType().getName())));
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                new Scoreboard(plugin).createScoreboard(player);
                            } else {
                                player.sendMessage(ConfigVariables.GEN_CAP_REACHED_MESSAGE);
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                e.setCancelled(true);
                            }
                        } else if (player.isOp()) {
                            Location hologramLocation = new Location(e.getBlock().getLocation().getWorld(),
                                    e.getBlock().getLocation().getX() + 0.5,
                                    e.getBlock().getLocation().getY() - 1,
                                    e.getBlock().getLocation().getZ() + 0.5);
                            ArmorStand hologram = (ArmorStand) e.getBlock().getLocation().getWorld().spawnEntity(hologramLocation, EntityType.ARMOR_STAND);
                            hologram.setCustomName(Utilities.format(playersGen.getType().getName()));
                            hologram.setCustomNameVisible(true);
                            hologram.setGravity(false);
                            hologram.setInvisible(true);
                            hologram.setInvulnerable(true);
                            playersGen.setArmorStand(hologram);
                            ecoPlayer.getOwnedGens().add(playersGen);
                            plugin.output(playersGen);
                            player.sendMessage(ConfigVariables.GEN_PLACED_MESSAGE
                                    .replace("{gen}", Utilities.format(playersGen.getType().getName())));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                            new Scoreboard(plugin).createScoreboard(player);
                        }
                    }
                }
            }
        }
    }
}
