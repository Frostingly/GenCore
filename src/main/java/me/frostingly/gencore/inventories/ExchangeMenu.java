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

public class ExchangeMenu extends InventoryHandler {

    private final GenCore plugin;

    public ExchangeMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&aExchange");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                switch (e.getSlot()) {
                    case 13:
                        switch (e.getClick()) {
                            case LEFT:
                                if (!ecoPlayer.isBypass()) {
                                    if (new Double(ecoPlayer.getBalance()) >= 25) {
                                        if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                        new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, new ConfirmCancelMenu.Confirm() {
                                            @Override
                                            public void handle(GenCore plugin, Player player1) {
                                                ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - 25);
                                                ecoPlayer.setTokens(ecoPlayer.getTokens() + 1);
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                        .replace("{tokens}", String.valueOf(1))
                                                        .replace("{money}", String.valueOf(25)));
                                                player.closeInventory();
                                                new Scoreboard(plugin).createScoreboard(player);
                                            }
                                        }, new ConfirmCancelMenu.Deny() {
                                            @Override
                                            public void handle(GenCore plugin, Player player) {
                                                if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                                new ExchangeMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            }
                                        }).open();
                                    } else {
                                        player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                        player.closeInventory();
                                    }
                                } else {
                                    if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                    new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, new ConfirmCancelMenu.Confirm() {
                                        @Override
                                        public void handle(GenCore plugin, Player player1) {
                                            ecoPlayer.setTokens(ecoPlayer.getTokens() + 1);
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                    .replace("{tokens}", String.valueOf(1))
                                                    .replace("{money}", String.valueOf(25)));
                                            player.closeInventory();
                                            new Scoreboard(plugin).createScoreboard(player);
                                        }
                                    }, new ConfirmCancelMenu.Deny() {
                                        @Override
                                        public void handle(GenCore plugin, Player player) {
                                            if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                            new ExchangeMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                        }
                                    }).open();
                                }
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                break;
                            case RIGHT:
                                if (!ecoPlayer.isBypass()) {
                                    if (new Double(ecoPlayer.getBalance()) >= 25) {
                                        if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                        new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin,
                                                createMainItem(1, 100),
                                                1,
                                                100,
                                                "token",
                                                "money",
                                                null,
                                                new AmountMenu.Confirmed() {
                                                    @Override
                                                    public void handle(GenCore plugin, Player player, int currentAmount, double currentCost, ItemStack good) {
                                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                        if (currentAmount == 1) {
                                                            player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                                    .replace("{tokens}", String.valueOf(currentAmount))
                                                                    .replace("{money}", String.valueOf(currentCost)));
                                                        } else {
                                                            player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                                    .replace("{tokens}", String.valueOf(currentAmount))
                                                                    .replace("{money}", String.valueOf(currentCost)));
                                                        }
                                                        ecoPlayer.setTokens(ecoPlayer.getTokens() + currentAmount);
                                                        ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - currentCost);

                                                        player.closeInventory();
                                                        new Scoreboard(plugin).createScoreboard(player);
                                                    }
                                                }).open();
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                    } else {
                                        player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                        player.closeInventory();
                                    }
                                } else {
                                    if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                    new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin,
                                            createMainItem(1, 100),
                                            1,
                                            100,
                                            "token",
                                            "money",
                                            null,
                                            new AmountMenu.Confirmed() {
                                                @Override
                                                public void handle(GenCore plugin, Player player, int currentAmount, double currentCost, ItemStack good) {
                                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                    if (currentAmount == 1) {
                                                        player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                                .replace("{tokens}", String.valueOf(currentAmount))
                                                                .replace("{money}", String.valueOf(currentCost)));
                                                    } else {
                                                        player.sendMessage(ConfigVariables.PURCHASED_TOKENS_MESSAGE
                                                                .replace("{tokens}", String.valueOf(currentAmount))
                                                                .replace("{money}", String.valueOf(currentCost)));
                                                    }
                                                    ecoPlayer.setTokens(ecoPlayer.getTokens() + currentAmount);

                                                    player.closeInventory();
                                                    new Scoreboard(plugin).createScoreboard(player);
                                                }
                                            }).open();
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                }
                                break;
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

        inventory.setItem(13, createTokenItem());
        inventory.setItem(22, plugin.createCloseItem());
    }

    public ItemStack createMainItem(int amount, double cost) {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (amount == 1) {
            itemMeta.setDisplayName(Utilities.format("&5" + amount + "x " + Utilities.format("token")));
        } else {
            itemMeta.setDisplayName(Utilities.format("&5" + amount + "x " + Utilities.format("token " + "&5s")));
        }
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        if (amount == 1) {
            lore.add("&7&oBuy " + amount + "x " + Utilities.format("token") + " &7&ofor &a" + cost + "&a$!");
        } else {
            lore.add("&7&oBuy " + amount + "x " + Utilities.format("token" + "&7&os ") + "for &a" + cost + "&a$!");
        }
        lore.add(" ");
        if (amount == 1) {
            lore.add("&bLeft click -> Buy " + amount + "x " + "token");
        } else {
            lore.add("&bLeft click -> Buy " + amount + "x " + Utilities.format("token" + "&bs "));
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createTokenItem() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&5Token"));

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("&7&oBuy 1 tokens for &a25$!");
        lore.add(" ");
        lore.add("&bLeft click -> Buy 1");
        lore.add("&bRight click -> Buy more");
        lore.add(" ");
        lore.add("&6Exchange rate: 1 token = 25$");

        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
