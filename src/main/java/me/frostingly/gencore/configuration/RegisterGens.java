package me.frostingly.gencore.configuration;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.gendata.Gen;
import me.frostingly.gencore.gendata.Type;
import me.frostingly.gencore.gendata.Upgrades;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegisterGens {

    private final GenCore plugin;

    public RegisterGens(GenCore plugin) {
        this.plugin = plugin;
    }

    public void regGens() {
        File root = new File(plugin.getDataFolder(), "gens");
        if (root.listFiles() != null) {
            for (File genFile : root.listFiles()) {
                Configuration config = YamlConfiguration.loadConfiguration(genFile);
                boolean genTypeExists = false;
                for (Type type : plugin.getTypes().values()) {
                    if (type.getType().equalsIgnoreCase(config.getString("gen.type"))) {
                        genTypeExists = true;
                        Gen gen = new Gen(type, type.getDefaultUpgrades());

                        ItemStack itemStack = new ItemStack(type.getBlock());
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(Utilities.format(type.getName()));

                        List<String> indexedLore = type.getLore();
                        List<String> newLore = new ArrayList<>();

                        newLore.add(" ");
                        newLore.add(indexedLore.get(1));
                        newLore.add(" ");

                        int speed = 1;
                        double quality = 1;
                        int quantity = 1;
                        int moneyFly = 1;

                        if (type.getDefaultUpgrades().getSpeed() != 1) speed = type.getDefaultUpgrades().getSpeed();
                        if (new Double(type.getDefaultUpgrades().getQuality()) != 1) quality = new Double(type.getDefaultUpgrades().getQuality());
                        if (type.getDefaultUpgrades().getQuantity() != 1) quantity = type.getDefaultUpgrades().getQuantity();
                        if (type.getDefaultUpgrades().getMoneyFly() != 1) moneyFly = type.getDefaultUpgrades().getMoneyFly();

                        ConfigurationSection upgradesSection = config.getConfigurationSection("gen.upgrades");
                        if (config.contains("gen.upgrades")) {
                            assert upgradesSection != null;
                            if (upgradesSection.contains("SPEED")) speed = upgradesSection.getInt(".SPEED");
                            if (upgradesSection.contains("QUALITY")) quality = new Double(upgradesSection.getString(".QUALITY"));
                            if (upgradesSection.contains("QUANTITY")) quantity = upgradesSection.getInt(".QUANTITY");
                            if (upgradesSection.contains("MONEY_FLY")) moneyFly = upgradesSection.getInt(".MONEY_FLY");
                        }

                        if (speed != 1
                                || quality != 1
                                || quantity != 1
                                || moneyFly != 1) {
                            newLore.add("&b&lUPGRADES");
                            if (speed != 1)
                                newLore.add("  &f * &fSpeed " + speed);
                            if (quality != 1)
                                newLore.add("  &f * &fQuality " + quality);
                            if (quantity != 1)
                                newLore.add("  &f * &fQuantity " + quantity);
                            if (moneyFly != 1)
                                newLore.add("  &f * &fMoney fly " + moneyFly);
                            newLore.add(indexedLore.get(0));
                        }
                        newLore.add(indexedLore.get(indexedLore.size() - 1));
                        itemMeta.setLore(Utilities.formatList(newLore));
                        itemStack.setItemMeta(itemMeta);
                        gen.setItemStack(itemStack);

                        gen.setUpgrades(new Upgrades(speed, quality, quantity, moneyFly));
                        plugin.getGens().put(config.getString("gen.id"), gen);
                    }
                }
                if (!genTypeExists) {
                    System.out.println(Utilities.format("&cError: Cannot find type " + config.getString("gen.type") + " in our indexes, gens who rely on type type shall not be indexed as well."));
                }
            }
        } else {
            File configFile = null;
            configFile = new File(this.plugin.getDataFolder(), "gens/starter_gen.yml");

            if (!configFile.exists()) {
                this.plugin.saveResource("gens/starter_gen.yml", false);
            }
            regGens();
            System.out.println(Utilities.format("&cCouldn't find any gen files, preloaded default files."));
        }
    }
}
