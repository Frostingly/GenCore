package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.gendata.Gen;
import me.frostingly.gencore.gendata.UpgradeType;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpgradesMenu extends InventoryHandler {

    private final Gen gen;
    private final GenCore plugin;

    public UpgradesMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin, Gen gen) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        this.gen = gen;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&e&lUpgrades for " + this.gen.getType().getName());
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                switch (e.getSlot()) {
                    case 11:
                        if (this.gen.canUpgrade(gen, UpgradeType.SPEED)) {
                            for (UpgradeType upgrade : this.gen.getType().getPurchasableUpgrades()) {
                                if (upgrade.equals(UpgradeType.SPEED)) {
                                    if (this.gen.getUpgrades().getSpeed() < upgrade.getMaxLevel()) {
                                        if (!player.isOp()) {
                                            if (ecoPlayer.getTokens() >= upgrade.getTokensNeeded()) {
                                                this.gen.getUpgrades().setSpeed(this.gen.getUpgrades().getSpeed() + 1);
                                                List<String> indexedLore = this.gen.getItemStack().getLore();
                                                List<String> newLore = new ArrayList<>();

                                                newLore.add(" ");
                                                newLore.add(indexedLore.get(1));
                                                newLore.add(" ");
                                                newLore.add("&b&lUPGRADES");
                                                if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                    newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                                if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                    newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                                if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                    newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                                if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                    newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                                newLore.add(indexedLore.get(0));
                                                newLore.add(indexedLore.get(indexedLore.size() - 1));
                                                this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                        .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                        .replace("{upgrade}", upgrade.name())
                                                        .replace("{level}", String.valueOf(this.gen.getUpgrades().getSpeed())));
                                                inventory.setItem(11, createSpeedItem(upgrade.getTokensNeeded()));
                                                ecoPlayer.setTokens(ecoPlayer.getTokens() - upgrade.getTokensNeeded());
                                                new Scoreboard(plugin).createScoreboard(player);
                                            } else {
                                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                                player.sendMessage(ConfigVariables.NOT_ENOUGH_TOKENS_MESSAGE);
                                            }
                                        } else {
                                            this.gen.getUpgrades().setSpeed(this.gen.getUpgrades().getSpeed() + 1);
                                            List<String> indexedLore = this.gen.getItemStack().getLore();
                                            List<String> newLore = new ArrayList<>();

                                            newLore.add(" ");
                                            newLore.add(indexedLore.get(1));
                                            newLore.add(" ");
                                            newLore.add("&b&lUPGRADES");
                                            if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                            if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                            if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                            if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                            newLore.add(indexedLore.get(0));
                                            newLore.add(indexedLore.get(indexedLore.size() - 1));
                                            this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                    .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                    .replace("{upgrade}", upgrade.name())
                                                    .replace("{level}", String.valueOf(this.gen.getUpgrades().getSpeed())));
                                            inventory.setItem(11, createSpeedItem(upgrade.getTokensNeeded()));
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                        player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                                .replace("{upgrade}", "speed"));
                                    }
                                }
                            }
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                    .replace("{upgrade}", "money fly"));
                        }
                        break;
                    case 12:
                        if (this.gen.canUpgrade(gen, UpgradeType.QUALITY)) {
                            for (UpgradeType upgrade : this.gen.getType().getPurchasableUpgrades()) {
                                if (upgrade.equals(UpgradeType.QUALITY)) {
                                    if (Double.parseDouble(this.gen.getUpgrades().getQuality()) < upgrade.getMaxLevel()) {
                                        if (!player.isOp()) {
                                            if (ecoPlayer.getTokens() >= upgrade.getTokensNeeded()) {
                                                this.gen.getUpgrades().setQuality(Double.parseDouble(this.gen.getUpgrades().getQuality()) + .1);
                                                List<String> indexedLore = this.gen.getItemStack().getLore();
                                                List<String> newLore = new ArrayList<>();

                                                newLore.add(" ");
                                                newLore.add(indexedLore.get(1));
                                                newLore.add(" ");
                                                newLore.add("&b&lUPGRADES");
                                                if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                    newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                                if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                    newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                                if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                    newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                                if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                    newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                                newLore.add(indexedLore.get(0));
                                                newLore.add(indexedLore.get(indexedLore.size() - 1));
                                                this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                        .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                        .replace("{upgrade}", upgrade.name())
                                                        .replace("{level}", this.gen.getUpgrades().getQuality()));
                                                inventory.setItem(12, createOutputQualityItem(upgrade.getTokensNeeded()));
                                                ecoPlayer.setTokens(ecoPlayer.getTokens() - upgrade.getTokensNeeded());
                                                new Scoreboard(plugin).createScoreboard(player);
                                            } else {
                                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                                player.sendMessage(ConfigVariables.NOT_ENOUGH_TOKENS_MESSAGE);
                                            }
                                        } else {
                                            this.gen.getUpgrades().setQuality(Double.parseDouble(this.gen.getUpgrades().getQuality()) + .1);
                                            List<String> indexedLore = this.gen.getItemStack().getLore();
                                            List<String> newLore = new ArrayList<>();

                                            newLore.add(" ");
                                            newLore.add(indexedLore.get(1));
                                            newLore.add(" ");
                                            newLore.add("&b&lUPGRADES");
                                            if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                            if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                            if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                            if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                            newLore.add(indexedLore.get(0));
                                            newLore.add(indexedLore.get(indexedLore.size() - 1));
                                            this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                    .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                    .replace("{upgrade}", upgrade.name())
                                                    .replace("{level}", this.gen.getUpgrades().getQuality()));
                                            inventory.setItem(12, createOutputQualityItem(upgrade.getTokensNeeded()));
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                        player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                                .replace("{upgrade}", "quality"));
                                    }
                                }
                            }
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                    .replace("{upgrade}", "money fly"));
                        }
                        break;
                    case 13:
                        if (this.gen.canUpgrade(gen, UpgradeType.QUANTITY)) {
                            for (UpgradeType upgrade : this.gen.getType().getPurchasableUpgrades()) {
                                if (upgrade.equals(UpgradeType.QUANTITY)) {
                                    if (this.gen.getUpgrades().getQuantity() < upgrade.getMaxLevel()) {
                                        if (!player.isOp()) {
                                            if (ecoPlayer.getTokens() >= upgrade.getTokensNeeded()) {
                                                this.gen.getUpgrades().setQuantity(this.gen.getUpgrades().getQuantity() + 1);
                                                List<String> indexedLore = this.gen.getItemStack().getLore();
                                                List<String> newLore = new ArrayList<>();

                                                newLore.add(" ");
                                                newLore.add(indexedLore.get(1));
                                                newLore.add(" ");
                                                newLore.add("&b&lUPGRADES");
                                                if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                    newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                                if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                    newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                                if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                    newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                                if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                    newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                                newLore.add(indexedLore.get(0));
                                                newLore.add(indexedLore.get(indexedLore.size() - 1));
                                                this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                        .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                        .replace("{upgrade}", upgrade.name())
                                                        .replace("{level}", String.valueOf(this.gen.getUpgrades().getQuantity())));
                                                inventory.setItem(13, createOutputItem(upgrade.getTokensNeeded()));
                                                ecoPlayer.setTokens(ecoPlayer.getTokens() - upgrade.getTokensNeeded());
                                                new Scoreboard(plugin).createScoreboard(player);
                                            } else {
                                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                                player.sendMessage(ConfigVariables.NOT_ENOUGH_TOKENS_MESSAGE);
                                            }
                                        } else {
                                            this.gen.getUpgrades().setQuantity(this.gen.getUpgrades().getQuantity() + 1);
                                            List<String> indexedLore = this.gen.getItemStack().getLore();
                                            List<String> newLore = new ArrayList<>();

                                            newLore.add(" ");
                                            newLore.add(indexedLore.get(1));
                                            newLore.add(" ");
                                            newLore.add("&b&lUPGRADES");
                                            if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                            if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                            if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                            if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                            newLore.add(indexedLore.get(0));
                                            newLore.add(indexedLore.get(indexedLore.size() - 1));
                                            this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                    .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                    .replace("{upgrade}", upgrade.name())
                                                    .replace("{level}", String.valueOf(this.gen.getUpgrades().getQuantity())));
                                            inventory.setItem(13, createOutputItem(upgrade.getTokensNeeded()));
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                        player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                                .replace("{upgrade}", "quantity"));
                                    }
                                }
                            }
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                    .replace("{upgrade}", "money fly"));
                        }
                        break;
                    case 14:
                        if (this.gen.canUpgrade(gen, UpgradeType.MONEY_FLY)) {
                            for (UpgradeType upgrade : this.gen.getType().getPurchasableUpgrades()) {
                                if (upgrade.equals(UpgradeType.MONEY_FLY)) {
                                    if (this.gen.getUpgrades().getMoneyFly() < upgrade.getMaxLevel()) {
                                        if (!player.isOp()) {
                                            if (ecoPlayer.getTokens() >= upgrade.getTokensNeeded()) {
                                                this.gen.getUpgrades().setMoneyFly(this.gen.getUpgrades().getMoneyFly() + 1);
                                                List<String> indexedLore = this.gen.getItemStack().getLore();
                                                List<String> newLore = new ArrayList<>();

                                                newLore.add(" ");
                                                newLore.add(indexedLore.get(1));
                                                newLore.add(" ");
                                                newLore.add("&b&lUPGRADES");
                                                if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                    newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                                if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                    newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                                if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                    newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                                if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                    newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                                newLore.add(indexedLore.get(0));
                                                newLore.add(indexedLore.get(indexedLore.size() - 1));
                                                this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                        .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                        .replace("{upgrade}", upgrade.name())
                                                        .replace("{level}", String.valueOf(this.gen.getUpgrades().getMoneyFly())));
                                                inventory.setItem(14, createMoneyFlyItem(upgrade.getTokensNeeded()));
                                                ecoPlayer.setTokens(ecoPlayer.getTokens() - upgrade.getTokensNeeded());
                                                new Scoreboard(plugin).createScoreboard(player);
                                            } else {
                                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                                player.sendMessage(ConfigVariables.NOT_ENOUGH_TOKENS_MESSAGE);
                                            }
                                        } else {
                                            this.gen.getUpgrades().setMoneyFly(this.gen.getUpgrades().getMoneyFly() + 1);
                                            List<String> indexedLore = this.gen.getItemStack().getLore();
                                            List<String> newLore = new ArrayList<>();

                                            newLore.add(" ");
                                            newLore.add(indexedLore.get(1));
                                            newLore.add(" ");
                                            newLore.add("&b&lUPGRADES");
                                            if (this.gen.getUpgrades().getSpeed() != this.gen.getType().getDefaultUpgrades().getSpeed())
                                                newLore.add("  &f * &fSpeed " + this.gen.getUpgrades().getSpeed());
                                            if (Double.parseDouble(this.gen.getUpgrades().getQuality()) != Double.parseDouble(this.gen.getType().getDefaultUpgrades().getQuality()))
                                                newLore.add("  &f * &fQuality " + this.gen.getUpgrades().getQuality());
                                            if (this.gen.getUpgrades().getQuantity() != this.gen.getType().getDefaultUpgrades().getQuantity())
                                                newLore.add("  &f * &fQuantity " + this.gen.getUpgrades().getQuantity());
                                            if (this.gen.getUpgrades().getMoneyFly() != this.gen.getType().getDefaultUpgrades().getMoneyFly())
                                                newLore.add("  &f * &fMoney fly " + this.gen.getUpgrades().getMoneyFly());
                                            newLore.add(indexedLore.get(0));
                                            newLore.add(indexedLore.get(indexedLore.size() - 1));
                                            this.gen.getItemStack().setLore(Utilities.formatList(newLore));
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MESSAGE
                                                    .replace("{gen}", Utilities.format(this.gen.getType().getName()))
                                                    .replace("{upgrade}", upgrade.name())
                                                    .replace("{level}", String.valueOf(this.gen.getUpgrades().getMoneyFly())));
                                            inventory.setItem(14, createMoneyFlyItem(upgrade.getTokensNeeded()));
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                        player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                                .replace("{upgrade}", "money fly"));
                                    }
                                }
                            }
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            player.sendMessage(ConfigVariables.GEN_UPGRADE_MAXED_OUT_MESSAGE
                                    .replace("{upgrade}", "money fly"));
                        }
                        break;
                    case 22:
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                }
            }
        }
    }



    @Override
    public void setMenuItems() {
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, plugin.createPane());
        }

        inventory.setItem(11, createSpeedItem(UpgradeType.SPEED.getTokensNeeded()));
        inventory.setItem(12, createOutputQualityItem(UpgradeType.QUALITY.getTokensNeeded()));
        inventory.setItem(13, createOutputItem(UpgradeType.QUANTITY.getTokensNeeded()));
        inventory.setItem(14, createMoneyFlyItem(UpgradeType.MONEY_FLY.getTokensNeeded()));
        inventory.setItem(15, createComingSoonItem());
        inventory.setItem(22, plugin.createCloseItem());
    }

    public ItemStack createComingSoonItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&cComing soon!"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMoneyFlyItem(int tokens) {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a&lMONEY FLY &8[&f" + this.gen.getUpgrades().getMoneyFly() + "&8]"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&8&oIncreases the gen's");
        lore.add("&8&omoney output");
        lore.add(" ");
        lore.add("&dTokens needed: &f" + tokens);
        lore.add(" ");
        if (this.gen.canUpgrade(this.gen, UpgradeType.MONEY_FLY)) {
            lore.add("&6&lCLICK TO UPGRADE");
        } else {
            lore.add("&cThis gen doesn't support further");
            lore.add("&cmoney output upgrades");
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createOutputItem(int tokens) {
        ItemStack itemStack = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&5&lQUANTITY &8[&f" + this.gen.getUpgrades().getQuantity() + "&8]"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&8&oIncreases the gen's");
        lore.add("&8&ooutput amount");
        lore.add(" ");
        lore.add("&dTokens needed: &f" + tokens);
        lore.add(" ");
        if (this.gen.canUpgrade(this.gen, UpgradeType.QUANTITY)) {
            lore.add("&6&lCLICK TO UPGRADE");
        } else {
            lore.add("&cThis gen doesn't support further");
            lore.add("&coutput quantity upgrades");
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createOutputQualityItem(int tokens) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a&lQUALITY &8[&f" + this.gen.getUpgrades().getQuality() + "&8]"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&8&oIncreases the gen's");
        lore.add("&8&ooutput quality");
        lore.add(" ");
        lore.add("&dTokens needed: &f" + tokens);
        lore.add(" ");
        if (this.gen.canUpgrade(this.gen, UpgradeType.QUALITY)) {
            lore.add("&6&lCLICK TO UPGRADE");
        } else {
            lore.add("&cThis gen doesn't support further");
            lore.add("&coutput quality upgrades");
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack createSpeedItem(int tokens) {
        ItemStack itemStack = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&b&lSPEED &8[&f" + this.gen.getUpgrades().getSpeed() + "&8]"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&8&oIncreases the gen's");
        lore.add("&8&ooutput speed");
        lore.add(" ");
        lore.add("&dTokens needed: &f" + tokens);
        lore.add(" ");
        if (this.gen.canUpgrade(this.gen, UpgradeType.SPEED)) {
            lore.add("&6&lCLICK TO UPGRADE");
        } else {
            lore.add("&cThis gen doesn't support further");
            lore.add("&coutput speed upgrades");
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
