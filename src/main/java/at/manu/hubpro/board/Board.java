package at.manu.hubpro.board;

import at.manu.hubpro.HubPro;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scoreboard.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board implements Runnable, PluginMessageListener {
    private final static Board instance = new Board();
    private int playercount = 0;
    private Map<String, Integer> serverPlayerCounts = new HashMap<>();
    private List<String> listOfServersYouTrack = Arrays.asList("lobby");

    private Board() {
        Bukkit.getMessenger().registerIncomingPluginChannel(HubPro.getInstance(), "BungeeCord", this);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard() != null && player.getScoreboard().getObjective("BendersMV") != null) {
                updateScoreboard(player);
            } else {
                createNewScoreboard(player);
            }
        }
    }

    private void requestServerPlayerCounts() {
        // For each server you want to display, send a request for its player count
        for (String serverName : listOfServersYouTrack) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(serverName); // Server name for which you're requesting the count
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendPluginMessage(HubPro.getInstance(), "BungeeCord", out.toByteArray()));
        }
    }

    private void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("BendersMC", "dummy");


        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "BendersMC");

        objective.getScore(ChatColor.WHITE + " ").setScore(6);
        objective.getScore(ChatColor.DARK_PURPLE + " ").setScore(5);
        objective.getScore(ChatColor.GOLD + "Online: " + ChatColor.WHITE + playercount).setScore(4);
        objective.getScore(ChatColor.YELLOW + " ").setScore(3);
        objective.getScore(ChatColor.WHITE + "Health: " + ChatColor.GREEN + player.getHealth() + ChatColor.WHITE + " hearts").setScore(2);
        objective.getScore(ChatColor.GREEN + " ").setScore(1);
        objective.getScore(ChatColor.WHITE + "Level: " + ChatColor.AQUA + player.getLevel()).setScore(0);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.getObjective("BendersMC") == null) {
            createNewScoreboard(player);
            scoreboard = player.getScoreboard();
        }

        Objective objective = scoreboard.getObjective("BendersMC");
        assert objective != null;

        // Clear previous dynamic scores
        scoreboard.getEntries().forEach(scoreboard::resetScores);

        // Set dynamic scores based on the server player counts
        int score = listOfServersYouTrack.size() + 2; // Starting score, adjust based on your static lines
        for (String server : listOfServersYouTrack) {
            Integer count = serverPlayerCounts.getOrDefault(server, 0);
            Score serverScore = objective.getScore(ChatColor.GOLD + server + ": " + ChatColor.WHITE + count);
            serverScore.setScore(score--);
        }

        // Update static scores here if needed
        // e.g., Health, Level, etc., that you want to always display at the bottom

        player.setScoreboard(scoreboard);
    }

    public static Board getInstance() {
        return instance;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if ("PlayerCount".equals(subchannel)) {
            String server = in.readUTF();
            int count = in.readInt();
            serverPlayerCounts.put(server, count); // Update the map with the latest count
            // Optionally, update the scoreboard immediately for all players
            Bukkit.getOnlinePlayers().forEach(this::updateScoreboard);
        }
    }
}
