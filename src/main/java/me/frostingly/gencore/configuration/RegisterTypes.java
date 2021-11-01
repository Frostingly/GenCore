package me.frostingly.gencore.configuration;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.gendata.Type;
import me.frostingly.gencore.gendata.UpgradeType;
import me.frostingly.gencore.gendata.Upgrades;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegisterTypes {

    private final GenCore plugin;

    public RegisterTypes(GenCore plugin) {
        this.plugin = plugin;
    }

    public void regTypes() {
        File root = new File(plugin.getDataFolder(), "types");
        if (root.listFiles() != null) {
            for (File typeFile : root.listFiles()) {
                Configuration config = YamlConfiguration.loadConfiguration(typeFile);
                int speed = 1;
                double quality = 1;
                int quantity = 1;
                int moneyFly = 1;
                ConfigurationSection defaultUpgradesSection = config.getConfigurationSection("type.defaultUpgrades");
                if (config.contains("type.defaultUpgrades")) {
                    assert defaultUpgradesSection != null;
                    if (defaultUpgradesSection.contains("SPEED")) speed = defaultUpgradesSection.getInt(".SPEED");
                    if (defaultUpgradesSection.contains("QUALITY")) quality = defaultUpgradesSection.getDouble(".QUALITY");
                    if (defaultUpgradesSection.contains("QUANTITY")) quantity = defaultUpgradesSection.getInt(".QUANTITY");
                    if (defaultUpgradesSection.contains("MONEY_FLY")) moneyFly = defaultUpgradesSection.getInt(".MONEY_FLY");
                }

                List<String> stringifiedPurchasableUpgrades = config.getStringList("type.purchasableUpgrades");
                List<UpgradeType> purchasableUpgrades = new ArrayList<>();
                stringifiedPurchasableUpgrades.forEach(upgradeType -> {
                    purchasableUpgrades.add(UpgradeType.valueOf(upgradeType));
                });

                Type type = new Type(
                        config.getString("type.display_name"),
                        config.getString("type.type"),
                        Material.valueOf(config.getString("type.display_block")),
                        config.getString("type.output_item"),
                        config.getStringList("type.default_lore"),
                        new Upgrades(speed, quality, quantity, moneyFly),
                        purchasableUpgrades
                        );
                plugin.getTypes().put(type.getType(), type);
            }
        } else {
            File configFile = null;
            configFile = new File(this.plugin.getDataFolder(), "types/starter_gen_type.yml");

            if (!configFile.exists()) {
                this.plugin.saveResource("types/starter_gen_type.yml", false);
            }
            regTypes();
        }
    }
 }
