package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.inventoryhandler.InventoryHandler;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AmountMenu extends InventoryHandler {

    @FunctionalInterface
    interface Confirmed {
        void handle(GenCore plugin, Player player, int currentAmount, double currentCost, ItemStack good);
    }

    private final GenCore plugin;
    private final ItemStack mainItem;
    private final int rawAmount;
    private int currentAmount;
    private final double rawCost;
    private double currentCost;
    private final String item;
    private final String currency;

    private ItemStack good;
    private final Confirmed confirmed;

    public AmountMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin, ItemStack mainItem, int rawAmount, double rawCost, String item, String currency, ItemStack good, Confirmed confirmed) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        this.mainItem = mainItem;
        this.rawAmount = rawAmount;
        this.rawCost = rawCost;
        this.item = item;
        this.currency = currency;

        this.good = good;
        this.confirmed = confirmed;

        this.currentAmount = rawAmount;
        this.currentCost = rawCost;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&bPick more");
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                switch (e.getSlot()) {
                    case 0:
                        currentAmount++;
                        currentCost = currentCost + rawCost;
                        inventory.setItem(0, createPlus1xItem());
                        inventory.setItem(4, createMainItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                    case 1:
                        currentAmount = currentAmount + 8;
                        currentCost = currentCost + rawCost * 8;
                        inventory.setItem(1, createPlus8xItem());
                        inventory.setItem(4, createMainItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;
                    case 2:
                        currentAmount = currentAmount + 64;
                        currentCost = currentCost + rawCost * 64;
                        inventory.setItem(2, createPlus64xItem());
                        inventory.setItem(4, createMainItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        break;

                    case 4:
                        if (!ecoPlayer.isBypass()) {
                            if (currency.equalsIgnoreCase("money")) {
                                if (new Double(ecoPlayer.getBalance()) >= currentCost) {
                                    if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                    new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, (plugin, player1) -> {
                                        confirmed.handle(plugin, player, currentAmount, currentCost, good);
                                    }, (plugin, player1) -> {
                                        if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                        new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player1, plugin, mainItem, rawAmount, rawCost, item, currency, good, confirmed).open();
                                        player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                    }).open();
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                } else {
                                    player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                }
                            } else if (currency.equalsIgnoreCase("tokens")) {
                                if (ecoPlayer.getTokens() >= new Double(currentCost).intValue()) {
                                    if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                    new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, (plugin, player1) -> {
                                        confirmed.handle(plugin, player, currentAmount, currentCost, good);
                                    }, (plugin, player1) -> {
                                        if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                        new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player1, plugin, mainItem, rawAmount, rawCost, item, currency, good, confirmed).open();
                                        player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                    }).open();
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                } else {
                                    player.sendMessage(ConfigVariables.NOT_ENOUGH_TOKENS_MESSAGE);
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                }
                            }
                        } else {
                            if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                            new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, (plugin, player1) -> {
                                confirmed.handle(plugin, player, currentAmount, currentCost, good);
                            }, (plugin, player1) -> {
                                new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player1, plugin, mainItem, rawAmount, rawCost, item, currency, good, confirmed).open();
                                player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                            }).open();
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        }
                        break;

                    case 6:
                        if (currentAmount > 1) {
                            currentAmount--;
                            currentCost = currentCost - rawCost;
                            inventory.setItem(4, createMainItem());
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                        break;
                    case 7:
                        if (currentAmount > 8) {
                            currentAmount = currentAmount - 8;
                            currentCost = currentCost - rawCost * 8;
                            if (currentAmount == 64) {
                                inventory.setItem(4, createMainItem());
                            } else {
                                inventory.setItem(4, createMainItem());
                            }
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                        break;
                    case 8:
                        if (currentAmount > 64) {
                            currentAmount = currentAmount - 64;
                            currentCost = currentCost - rawCost * 64;
                            if (currentAmount == 64) {
                                inventory.setItem(4, createMainItem());
                            } else {
                                inventory.setItem(4, createMainItem());
                            }
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(4, createMainItem());

        inventory.setItem(0, createPlus1xItem());
        inventory.setItem(1, createPlus8xItem());
        inventory.setItem(2, createPlus64xItem());

        inventory.setItem(6, createMinus1xItem());
        inventory.setItem(7, createMinus8xItem());
        inventory.setItem(8, createMinus64xItem());
    }


    public ItemStack createMainItem() {
        ItemStack itemStack = new ItemStack(mainItem.getType());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (currentAmount == 1) {
            itemMeta.setDisplayName(Utilities.format("&5" + currentAmount + "x " + Utilities.format(item)));
        } else {
            itemMeta.setDisplayName(Utilities.format("&5" + currentAmount + "x " + Utilities.format(item + "&5s")));
        }
        if (currentAmount > 64) {
            itemStack.setAmount(64);
        } else {
            itemStack.setAmount(currentAmount);
        }

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        if (currentAmount == 1) {
            lore.add("&7&oBuy " + currentAmount + "x " + Utilities.format(item) + " &7&ofor &a" + currentCost + "&a$!");
        } else {
            lore.add("&7&oBuy " + currentAmount + "x " + Utilities.format(item + "&7&os ") + "for &a" + currentCost + "&a$!");
        }
        lore.add(" ");
        if (currentAmount == 1) {
            lore.add("&bLeft click -> Buy " + currentAmount + "x " + Utilities.format(item));
        } else {
            lore.add("&bLeft click -> Buy " + currentAmount + "x " + Utilities.format(item + "&bs "));
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createPlus1xItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a+1 " + item));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> +1 " + item);
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createPlus8xItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a+8 " + item + "&as"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> +8 " + item + "&bs");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createPlus64xItem() {
        ItemStack itemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&a+64 " + item + "&as"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> +64 " + item + "&bs");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMinus1xItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&c-1 " + item));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> -1 " + item);
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMinus8xItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&c-8 " + item + "&cs"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> -8 " + item + "&bs");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMinus64xItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&c-64 " + item + "&cs"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&bLeft click -> -64 " + item + "&bs");
        itemMeta.setLore(Utilities.formatList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
