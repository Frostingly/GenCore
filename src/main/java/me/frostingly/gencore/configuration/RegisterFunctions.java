package me.frostingly.gencore.configuration;

import me.frostingly.gencore.GenCore;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class RegisterFunctions {

    private final GenCore plugin;

    public RegisterFunctions(GenCore plugin) {
        this.plugin = plugin;
    }

    public void regFunctions() {
        File root = new File(plugin.getDataFolder(), "functions");
        if (root.listFiles().length > 0) {
            for (File functionFile : root.listFiles()) {
                if (functionFile.isFile()) {
                    Configuration config = YamlConfiguration.loadConfiguration(functionFile);
                    plugin.getFunctions().put(functionFile.getName().replace(".yml", ""), config);
                }
            }
        }
    }
}
