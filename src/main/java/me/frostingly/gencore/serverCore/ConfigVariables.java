package me.frostingly.gencore.serverCore;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ConfigVariables {

    private final GenCore plugin;

    public ConfigVariables(GenCore plugin) {
        this.plugin = plugin;
    }

    public static String SELL_MESSAGE = "";
    public static String COULD_NOT_SELL = "";
    public static String GAMEMODE_CHANGE_MESSAGE = "";
    public static String NO_PERMISSION_CMD = "";
    public static String ONLY_PLAYERS_CAN_EXEC_CMD = "";
    public static String JOIN_MESSAGE = "";
    public static String QUIT_MESSAGE = "";
    public static String GEN_PLACED_MESSAGE = "";
    public static String GEN_UPGRADE_MESSAGE = "";

    public static String GEN_UPGRADE_MAXED_OUT_MESSAGE = "";
    public static String NOT_ENOUGH_TOKENS_MESSAGE = "";
    public static String NOT_ENOUGH_DOLLARS_MESSAGE = "";
    public static String GEN_CAP_REACHED_MESSAGE = "";

    public static String PURCHASED_TOKENS_MESSAGE = "";

    public static String ECO_PLAYER_NO_EXIST = "";

    public static ConfigurationSection INCORRECT_CMD_USAGE = null;

    public static Map<Integer, String> SCORES = new HashMap<>();

    public void registerVariables() {
        SELL_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.SELL_MESSAGE"));
        COULD_NOT_SELL = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.COULD_NOT_SELL"));
        GAMEMODE_CHANGE_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.GAMEMODE_CHANGE_MESSAGE"));
        NO_PERMISSION_CMD = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.NO_PERMISSION_CMD"));
        ONLY_PLAYERS_CAN_EXEC_CMD = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.ONLY_PLAYERS_CAN_EXEC_CMD"));
        JOIN_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.JOIN_MESSAGE"));
        QUIT_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.QUIT_MESSAGE"));
        GEN_PLACED_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.GEN_PLACED_MESSAGE"));
        GEN_UPGRADE_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.GEN_UPGRADE_MESSAGE"));

        GEN_UPGRADE_MAXED_OUT_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.GEN_UPGRADE_MAXED_OUT_MESSAGE"));
        NOT_ENOUGH_TOKENS_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.NOT_ENOUGH_TOKENS_MESSAGE"));
        NOT_ENOUGH_DOLLARS_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.NOT_ENOUGH_DOLLARS_MESSAGE"));
        GEN_CAP_REACHED_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.GEN_CAP_REACHED_MESSAGE"));

        PURCHASED_TOKENS_MESSAGE = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.PURCHASED_TOKENS_MESSAGE"));

        ECO_PLAYER_NO_EXIST = Utilities.format(plugin.getMessageManager().getConfig().getString("messages.ECO_PLAYER_NO_EXIST"));

        INCORRECT_CMD_USAGE = plugin.getMessageManager().getConfig().getConfigurationSection("messages.INCORRECT_CMD_USAGE");

        plugin.getScoreboardManager().getConfig().getConfigurationSection("scoreboard.scores").getKeys(false).forEach(score -> {
            SCORES.put(Integer.parseInt(score), Utilities.format(plugin.getScoreboardManager().getConfig().getString("scoreboard.scores." + score + ".text")));
        });
    }
}
