package me.frostingly.gencore.inventories;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.gendata.PurchasableGen;
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

public class GenShopMenu extends InventoryHandler {

    private final GenCore plugin;

    public GenShopMenu(PlayerMenuUtility playerMenuUtility, Player player, GenCore plugin) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
    }

    @Override
    public String getInventoryName() {
        return Utilities.format("&bGen Shop");
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
                for (int i = 0; i < 9; i++) {
                    if (e.getSlot() == i) {
                        for (PurchasableGen gen : plugin.getPurchasableGens().values()) {
                            if (Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()).equalsIgnoreCase(Utilities.format(e.getCurrentItem().getItemMeta().getDisplayName()))) {
                                switch (e.getClick()) {
                                    case LEFT:
                                        if (!ecoPlayer.isBypass()) {
                                            if (new Double(ecoPlayer.getBalance()) >= gen.getCost()) {
                                                if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                                new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, new ConfirmCancelMenu.Confirm() {
                                                    @Override
                                                    public void handle(GenCore plugin, Player player) {
                                                        player.getInventory().addItem(gen.getItemStack());
                                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                        player.closeInventory();
                                                    }
                                                }, new ConfirmCancelMenu.Deny() {
                                                    @Override
                                                    public void handle(GenCore plugin, Player player) {
                                                        new GenShopMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                    }
                                                }).open();
                                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                            } else {
                                                player.sendMessage(ConfigVariables.NOT_ENOUGH_DOLLARS_MESSAGE);
                                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                            }
                                        } else {
                                            if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                            new ConfirmCancelMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, new ConfirmCancelMenu.Confirm() {
                                                @Override
                                                public void handle(GenCore plugin, Player player) {
                                                    player.getInventory().addItem(gen.getItemStack());
                                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                    player.closeInventory();
                                                }
                                            }, new ConfirmCancelMenu.Deny() {
                                                @Override
                                                public void handle(GenCore plugin, Player player) {
                                                    new GenShopMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin).open();
                                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                }
                                            }).open();
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                        }
                                        break;
                                    case RIGHT:
                                        if (!ecoPlayer.isBypass()) {
                                            if (new Double(ecoPlayer.getBalance()) >= gen.getCost()) {
                                                if (ecoPlayer.getPlayerMenuUtility() == null) ecoPlayer.setPlayerMenuUtility(new PlayerMenuUtility(player));
                                                new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, createMainItem(
                                                        Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()),
                                                        gen.getItemStack().getType(),
                                                        1,
                                                        gen.getCost()),
                                                        1,
                                                        gen.getCost(),
                                                        Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()),
                                                        "money", gen.getItemStack(), new AmountMenu.Confirmed() {
                                                    @Override
                                                    public void handle(GenCore plugin, Player player, int currentAmount, double currentCost, ItemStack good) {
                                                        ecoPlayer.setBalance(new Double(ecoPlayer.getBalance()) - currentCost);
                                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                        if (currentAmount == 1) {
                                                            player.sendMessage(ConfigVariables.PURCHASED_STORE_ITEM_MESSAGE
                                                                    .replace("{gens}", String.valueOf(currentAmount))
                                                                    .replace("{gen_name}", Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()))
                                                                    .replace("{money}", String.valueOf(currentCost)));
                                                        } else {
                                                            player.sendMessage(ConfigVariables.PURCHASED_STORE_ITEM_MESSAGE
                                                                    .replace("{gens}", String.valueOf(currentAmount))
                                                                    .replace("{gen_name}", Utilities.format(gen.getItemStack().getItemMeta().getDisplayName() + "&as"))
                                                                    .replace("{money}", String.valueOf(currentCost)));
                                                        }
                                                        ItemStack itemStack = good;
                                                        itemStack.setAmount(currentAmount);

                                                        player.getInventory().addItem(itemStack);
                                                        player.closeInventory();
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
                                            new AmountMenu(ecoPlayer.getPlayerMenuUtility(), player, plugin, createMainItem(
                                                    Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()),
                                                    gen.getItemStack().getType(),
                                                    1,
                                                    gen.getCost()),
                                                    1,
                                                    100,
                                                    Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()),
                                                    "money", gen.getItemStack(), new AmountMenu.Confirmed() {
                                                        @Override
                                                        public void handle(GenCore plugin, Player player, int currentAmount, double currentCost, ItemStack good) {
                                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                                            if (currentAmount == 1) {
                                                                player.sendMessage(ConfigVariables.PURCHASED_STORE_ITEM_MESSAGE
                                                                        .replace("{gens}", String.valueOf(currentAmount))
                                                                        .replace("{gen_name}", Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()))
                                                                        .replace("{money}", String.valueOf(currentCost)));
                                                            } else {
                                                                player.sendMessage(ConfigVariables.PURCHASED_STORE_ITEM_MESSAGE
                                                                        .replace("{gens}", String.valueOf(currentAmount))
                                                                        .replace("{gen_name}", Utilities.format(gen.getItemStack().getItemMeta().getDisplayName() + "&as"))
                                                                        .replace("{money}", String.valueOf(currentCost)));
                                                            }


                                                            ItemStack itemStack = good;
                                                            itemStack.setAmount(currentAmount);

                                                            player.getInventory().addItem(itemStack);
                                                            player.closeInventory();
                                                            new Scoreboard(plugin).createScoreboard(player);
                                                        }
                                            }).open();
                                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        int slot = 0;
        for (PurchasableGen gen : plugin.getPurchasableGens().values()) {
            ItemStack itemStack = new ItemStack(gen.getItemStack().getType());
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(Utilities.format(gen.getItemStack().getItemMeta().getDisplayName()));
            itemMeta.setLore(Utilities.formatList(gen.getItemStack().getItemMeta().getLore()));

            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot, itemStack);
            slot++;
        }
    }

    public ItemStack createMainItem(String genName, Material material, int amount, double cost) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (amount == 1) {
            itemMeta.setDisplayName(Utilities.format("&5" + amount + "x " + Utilities.format(genName)));
        } else {
            itemMeta.setDisplayName(Utilities.format("&5" + amount + "x " + Utilities.format(genName + "&5s")));
        }
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        if (amount == 1) {
            lore.add("&7&oBuy " + amount + "x " + Utilities.format(genName) + " &7&ofor &a" + cost + "&a$!");
        } else {
            lore.add("&7&oBuy " + amount + "x " + Utilities.format(genName + "&7&os ") + "for &a" + cost + "&a$!");
        }
        lore.add(" ");
        if (amount == 1) {
            lore.add("&bLeft click -> Buy " + amount + "x " + Utilities.format(genName));
        } else {
            lore.add("&bLeft click -> Buy " + amount + "x " + Utilities.format(genName + "&bs "));
        }
        itemMeta.setLore(Utilities.formatList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
