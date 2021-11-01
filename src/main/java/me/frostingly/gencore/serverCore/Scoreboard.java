package me.frostingly.gencore.serverCore;

import me.frostingly.gencore.GenCore;
import me.frostingly.gencore.Utilities;
import me.frostingly.gencore.playerData.EcoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import java.text.DecimalFormat;

public class Scoreboard {

    private final GenCore plugin;

    public Scoreboard(GenCore plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("server-sb", "dummy", Utilities.format(plugin.getScoreboardManager().getConfig().getString("scoreboard.title")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (EcoPlayer ecoPlayer : plugin.getEcoPlayers()) {
            if (ecoPlayer.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                DecimalFormat df = new DecimalFormat("0.#");
                for (Integer key : ConfigVariables.SCORES.keySet()) {
                    Score score = null;
                    if (Utilities.canFormat(new Double(ecoPlayer.getBalance()).longValue())) {
                        score = obj.getScore(ConfigVariables.SCORES.get(key)
                                .replace("{player_name}", player.getName())
                                .replace("{balance}", Utilities.formatNumber(new Double(ecoPlayer.getBalance()).longValue()))
                                .replace("{tokens}", String.valueOf(ecoPlayer.getTokens()))
                                .replace("{total_gens}", String.valueOf(ecoPlayer.getOwnedGens().size()))
                                .replace("{max_gens}", String.valueOf(ecoPlayer.getMaxTotalGens()))
                                .replace("{tps}", df.format(Bukkit.getServer().getTPS()[0]))
                                .replace("{online_players}", String.valueOf(Bukkit.getOnlinePlayers().size())));
                    } else {
                        score = obj.getScore(ConfigVariables.SCORES.get(key)
                                .replace("{player_name}", player.getName())
                                .replace("{balance}", df.format(new Double(ecoPlayer.getBalance())))
                                .replace("{tokens}", String.valueOf(ecoPlayer.getTokens()))
                                .replace("{total_gens}", String.valueOf(ecoPlayer.getOwnedGens().size()))
                                .replace("{max_gens}", String.valueOf(ecoPlayer.getMaxTotalGens()))
                                .replace("{tps}", df.format(Bukkit.getServer().getTPS()[0]))
                                .replace("{online_players}", String.valueOf(Bukkit.getOnlinePlayers().size())));
                    }
                    score.setScore(key);
                }
            }
        }
        player.setScoreboard(board);
    }
}
