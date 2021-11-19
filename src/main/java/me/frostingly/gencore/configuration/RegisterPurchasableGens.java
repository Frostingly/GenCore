package me.frostingly.gencore.configuration;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.gendata.*;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegisterPurchasableGens {

    private final GenCore plugin;

    public RegisterPurchasableGens(GenCore plugin) {
        this.plugin = plugin;
    }

    public void regPurchasableGens() {
        File root = new File(plugin.getDataFolder(), "purchasable_gens");
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

                ItemStack itemStack = new ItemStack(Material.valueOf(config.getString("gen.display_block")));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(Utilities.format(config.getString("gen.display_name")));
                itemMeta.setLore(Utilities.formatList(config.getStringList("gen.default_lore")));
                itemStack.setItemMeta(itemMeta);
                PurchasableGen purchasableGen = new PurchasableGen(
                        itemStack,
                        config.getDouble("gen.cost"),
                        new Upgrades(speed, quality, quantity, moneyFly),
                        purchasableUpgrades
                );
                plugin.getPurchasableGens().put(config.getString("gen.id"), purchasableGen);
            }
        } else {
            File configFile = null;
            configFile = new File(this.plugin.getDataFolder(), "purchasable_gens/coal_gen.yml");

            if (!configFile.exists()) {
                this.plugin.saveResource("purchasable_gens/coal_gen.yml", false);
            }
            regPurchasableGens();
            System.out.println(Utilities.format("&cCouldn't find any purchasable gen files, preloaded default files."));
        }
    }

}
