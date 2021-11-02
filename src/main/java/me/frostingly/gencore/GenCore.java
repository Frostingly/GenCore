package me.frostingly.gencore;

import me.frostingly.gencore.servercore.commands.PluginCMD;
import me.frostingly.gencore.configuration.*;
import me.frostingly.gencore.events.*;
import me.frostingly.gencore.gendata.*;
import me.frostingly.gencore.inventoryhandler.PlayerMenuUtility;
import me.frostingly.gencore.playerdata.EcoPlayer;
import me.frostingly.gencore.servercore.ConfigVariables;
import me.frostingly.gencore.servercore.Scoreboard;
import me.frostingly.gencore.servercore.economy.commands.Exchange;
import me.frostingly.gencore.servercore.economy.commands.Sell;
import me.frostingly.gencore.servercore.commands.Ban;
import me.frostingly.gencore.servercore.commands.BanCheck;
import me.frostingly.gencore.servercore.commands.Gamemode;
import me.frostingly.gencore.servercore.commands.Unban;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public final class GenCore extends JavaPlugin {

    private final Utilities utilities = new Utilities();
    private final List<EcoPlayer> ecoPlayers = new ArrayList<>();
    private final Map<String, Type> types = new HashMap();
    private final Map<String, Configuration> functions = new HashMap<>();
    private final Map<String, Gen> gens = new HashMap<>();
    private final Map<UUID, String> bannedUUIDs = new HashMap<>();

    private final DataManager data = new DataManager(this);
    private final MessageManager messageManager = new MessageManager(this);
    private final ScoreboardManager scoreboardManager = new ScoreboardManager(this);
    private final BanDataManager banData = new BanDataManager(this);

    private static GenCore instance;

    public void checkTPS() {
        DecimalFormat df = new DecimalFormat("0.#");
        String indexedTPS = df.format(Bukkit.getServer().getTPS()[0]);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(df.format(Bukkit.getServer().getTPS()[0]).equalsIgnoreCase(indexedTPS))) {
                    this.cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        new Scoreboard(getInstance()).createScoreboard(player);
                    }
                    checkTPS();
                }
            }
        }.runTaskTimer(this, 0L, 600L);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        new DataManager(this).saveDefaultConfig();
        new MessageManager(this).saveDefaultConfig();
        new ScoreboardManager(this).saveDefaultConfig();
        new BanDataManager(this).saveDefaultConfig();

        new ConfigVariables(this).registerVariables();

        checkTPS();

        new RegisterFunctions(this).regFunctions();
        new RegisterTypes(this).regTypes();
        new RegisterGens(this).regGens();

        regEvents(Bukkit.getPluginManager());
        regCMDs();

        if (this.data.getConfig().contains("data"))
            loadData();
        if (this.banData.getConfig().contains("bans"))
            loadBans();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ecoPlayers.size() == 0) {
                EcoPlayer ecoPlayer = new EcoPlayer(player.getUniqueId().toString(), 0, 0, 20, new PlayerMenuUtility(player));
                ecoPlayers.add(ecoPlayer);
            } else {
                boolean foundEcoPlayer = false;
                for (EcoPlayer ecoPlayer : ecoPlayers) {
                    if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                        foundEcoPlayer = true;
                        for (int i = 0; i < ecoPlayer.getOwnedGens().size(); i++) {
                            Gen gen = ecoPlayer.getOwnedGens().get(i);
                            output(gen);
                        }
                    }
                }
                if (!foundEcoPlayer) {
                    EcoPlayer ecoPlayer = new EcoPlayer(player.getUniqueId().toString(), 0, 0, 20, new PlayerMenuUtility(player));
                    ecoPlayers.add(ecoPlayer);
                }
            }
            new Scoreboard(this).createScoreboard(player);
        }
    }

    private void regEvents(PluginManager pm) {
        pm.registerEvents(new GenPlace(this), this);
        pm.registerEvents(new GenBreak(this), this);
        pm.registerEvents(new GenMenuOpen(this), this);
        pm.registerEvents(new InventoryClick(this), this);
        pm.registerEvents(new ChestRightClick(this), this);

        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerQuit(this), this);
    }

    private void regCMDs() {
        getCommand("exchange").setExecutor(new Exchange(this));
        getCommand("sell").setExecutor(new Sell(this));

        getCommand("gamemode").setExecutor(new Gamemode(this));
        getCommand("gms").setExecutor(new Gamemode(this));
        getCommand("gmc").setExecutor(new Gamemode(this));
        getCommand("gma").setExecutor(new Gamemode(this));
        getCommand("gmsp").setExecutor(new Gamemode(this));
        getCommand("gencore").setExecutor(new PluginCMD(this));

        getCommand("ban").setExecutor(new Ban(this));
        getCommand("bancheck").setExecutor(new BanCheck(this));
        getCommand("unban").setExecutor(new Unban(this));
    }

    public void output(Gen gen) {
        final int indexedSpeed = gen.getUpgrades().getSpeed();
        final String indexedQuality = gen.getUpgrades().getQuality();
        final int indexedQuantity = gen.getUpgrades().getQuantity();
        final int indexedMoneyFly = gen.getUpgrades().getMoneyFly();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gen.isActive()) {
                    if (gen.getUpgrades().getSpeed() != indexedSpeed
                            || !new Double(gen.getUpgrades().getQuality()).equals(new Double(indexedQuality))
                            || gen.getUpgrades().getQuantity() != indexedQuantity
                            || gen.getUpgrades().getMoneyFly() != indexedMoneyFly) {
                        this.cancel();
                        output(gen);
                    } else {
                        if (Bukkit.getWorld(gen.getLocation().getWorld().getName()).getBlockAt(gen.getLocation()).getType() != Material.AIR) {
                            ItemStack outputItem = null;
                            if (gen.getType().getOutputItem().contains("function")) {
                                for (String functionName : getFunctions().keySet()) {
                                    String rawFunctionName = StringUtils.substringBetween(gen.getType().getOutputItem(), "(", ")");
                                    if (functionName.equalsIgnoreCase(rawFunctionName)) {
                                        Configuration config = getFunctions().get(functionName);
                                        if (config.getString("function.return").equalsIgnoreCase("ItemStack")) {
                                            outputItem = new ItemStack(Material.valueOf(config.getString("function.itemstack.material")));
                                            ItemMeta itemMeta = outputItem.getItemMeta();
                                            itemMeta.setDisplayName(Utilities.format(config.getString("function.itemstack.display_name")));
                                            outputItem.setItemMeta(itemMeta);
                                        }
                                    }
                                }
                            }

                            ItemMeta itemMeta = outputItem.getItemMeta();

                            List<String> lore = new ArrayList<>();
                            lore.add(" ");
                            lore.add("&aSell price: " + indexedQuality + "&a$");
                            itemMeta.setLore(Utilities.formatList(lore));
                            outputItem.setAmount(indexedQuantity);
                            outputItem.setItemMeta(itemMeta);

                            Location newLocation = new Location(gen.getLocation().getWorld(), gen.getLocation().getX() + 0.5, gen.getLocation().getY() + 1, gen.getLocation().getZ() + 0.5);

                            Bukkit.getWorld(gen.getLocation().getWorld().getName()).dropItem(newLocation, outputItem);
                        } else {
                            this.cancel();
                        }
                    }
                }
            }
        }.runTaskTimer(getInstance(), 20L, 100L / indexedSpeed);
    }

    public ItemStack createPane() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(" ");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createCloseItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utilities.format("&cClose"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.data.getConfig().set("data", null);
        this.data.saveConfig();
        this.banData.getConfig().set("bans", null);
        this.banData.saveConfig();
        if (!(ecoPlayers.isEmpty())) {
            this.saveData();
            this.data.saveConfig();
        }
        if (!(bannedUUIDs.isEmpty())) {
            this.saveBans();;
            this.banData.saveConfig();
        }
    }

    private void saveData() {
        if (!ecoPlayers.isEmpty()) {
            for (EcoPlayer ecoPlayer : ecoPlayers) {
                this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".balance", ecoPlayer.getBalance());
                this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".tokens", ecoPlayer.getTokens());
                this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".maxTotalGens", ecoPlayer.getMaxTotalGens());
                this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".withdrew", ecoPlayer.hasWithdrew());

                for (int genID = 0; genID < ecoPlayer.getOwnedGens().size(); genID++) {
                    ecoPlayer.getOwnedGens().get(genID).getArmorStand().remove();
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".type", ecoPlayer.getOwnedGens().get(genID).getType().getType());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".active", ecoPlayer.getOwnedGens().get(genID).isActive());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".location", ecoPlayer.getOwnedGens().get(genID).getLocation());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".itemstack.material", ecoPlayer.getOwnedGens().get(genID).getItemStack().getType().name());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".itemstack.itemmeta.display_name", ecoPlayer.getOwnedGens().get(genID).getItemStack().getItemMeta().getDisplayName());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".itemstack.itemmeta.lore", ecoPlayer.getOwnedGens().get(genID).getItemStack().getItemMeta().getLore());

                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".upgrades.speed", ecoPlayer.getOwnedGens().get(genID).getUpgrades().getSpeed());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".upgrades.quality", ecoPlayer.getOwnedGens().get(genID).getUpgrades().getQuality());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".upgrades.quantity", ecoPlayer.getOwnedGens().get(genID).getUpgrades().getQuantity());
                    this.data.getConfig().set("data.players." + ecoPlayer.getOwner() + ".ownedGens." + genID + ".upgrades.money_fly", ecoPlayer.getOwnedGens().get(genID).getUpgrades().getMoneyFly());
                }
            }
        }
    }

    private void loadData() {
        ConfigurationSection dataSection = this.data.getConfig().getConfigurationSection("data.players");
        dataSection.getKeys(false).forEach(uuid -> {
            EcoPlayer ecoPlayer = new EcoPlayer(
                    uuid,
                    new Double(dataSection.getString(uuid + ".balance")),
                    dataSection.getInt(uuid + ".tokens"),
                    dataSection.getInt(uuid + ".maxTotalGens"),
                    new PlayerMenuUtility(Bukkit.getPlayer(UUID.fromString(uuid))));

            List<Gen> playerGens = new ArrayList<>();
            if (dataSection.contains(uuid + ".ownedGens")) {
                ConfigurationSection genSection = dataSection.getConfigurationSection(uuid + ".ownedGens");
                genSection.getKeys(false).forEach(genID -> {
                    int speed = genSection.getInt(genID + ".upgrades.speed");
                    String quality = genSection.getString(genID + ".upgrades.quality");
                    int quantity = genSection.getInt(genID + ".upgrades.quantity");
                    int moneyFly = genSection.getInt(genID + ".upgrades.money_fly");
                    Gen gen = new Gen(getTypes().get(genSection.getString(genID + ".type")), new Upgrades(speed, new Double(quality), quantity, moneyFly));
                    gen.setUpgrades(new Upgrades(speed, new Double(quality), quantity, moneyFly));
                    Location location = (Location) genSection.get(genID + ".location");
                    gen.setActive(genSection.getBoolean(genID + ".active"));
                    gen.setLocation(location);

                    ItemStack itemStack = new ItemStack(Material.valueOf(genSection.getString(genID + ".itemstack.material")));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(Utilities.format(genSection.getString(genID + ".itemstack.itemmeta.display_name")));
                    itemMeta.setLore(Utilities.formatList(genSection.getStringList(genID + ".itemstack.itemmeta.lore")));
                    itemStack.setItemMeta(itemMeta);
                    gen.setItemStack(itemStack);

                    Location hologramLocation = new Location(gen.getLocation().getWorld(), gen.getLocation().getX() +
                             0.5, gen.getLocation().getY() - 1, gen.getLocation().getZ() + 0.5);
                    ArmorStand hologram = (ArmorStand) Bukkit.getWorld(gen.getLocation().getWorld().getName()).spawnEntity(hologramLocation, EntityType.ARMOR_STAND);
                    hologram.setCustomName(Utilities.format(gen.getType().getName()));
                    hologram.setCustomNameVisible(true);
                    hologram.setGravity(false);
                    hologram.setInvulnerable(true);
                    hologram.setInvisible(true);
                    gen.setArmorStand(hologram);
                    playerGens.add(gen);
                });
                ecoPlayer.setOwnedGens(playerGens);
            }
            ecoPlayers.add(ecoPlayer);
        });
    }

    private void saveBans() {
        if (!bannedUUIDs.isEmpty()) {
            for (UUID uuid : bannedUUIDs.keySet()) {
                this.banData.getConfig().set("bans." + uuid.toString() + ".reason", bannedUUIDs.get(uuid).toString());
            }
        }
    }

    private void loadBans() {
        ConfigurationSection dataSection = this.banData.getConfig().getConfigurationSection("bans");
        dataSection.getKeys(false).forEach(uuid -> {
            UUID uuidConverted = UUID.fromString(uuid);
            bannedUUIDs.put(uuidConverted, dataSection.getString(uuid + ".reason"));
        });
    }

    public Utilities getUtilities() {
        return utilities;
    }

    public List<EcoPlayer> getEcoPlayers() {
        return ecoPlayers;
    }

    public Map<String, Type> getTypes() {
        return types;
    }

    public Map<String, Configuration> getFunctions() {
        return functions;
    }

    public Map<String, Gen> getGens() {
        return gens;
    }

    public Map<UUID, String> getBannedUUIDs() {
        return bannedUUIDs;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static GenCore getInstance() {
        return instance;
    }
}
