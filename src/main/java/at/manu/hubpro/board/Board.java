//package at.manu.hubpro.board;
//
//import at.manu.hubpro.HubPro;
//import com.google.common.io.ByteArrayDataInput;
//import com.google.common.io.ByteArrayDataOutput;
//import com.google.common.io.ByteStreams;
//import lombok.Getter;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.plugin.messaging.PluginMessageListener;
//import org.bukkit.scoreboard.*;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.*;
//
//public class Board implements Runnable, PluginMessageListener {
//    @Getter
//    private final static Board instance = new Board();
//    private final Map<String, Integer> serverPlayerCounts = new HashMap<>();
//    private final List<String> listOfServersYouTrack = Collections.singletonList("lobby");
//
//    private Board() {
//        HubPro.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(HubPro.getInstance(), "BungeeCord");
//        HubPro.getInstance().getServer().getMessenger().registerIncomingPluginChannel(HubPro.getInstance(), "BungeeCord", this);
//        requestServerPlayerCounts();
//    }
//
//    @Override
//    public void run() {
//        for (Player player : Bukkit.getOnlinePlayers()) {
//			player.getScoreboard();
//			if (player.getScoreboard().getObjective("BendersMC") != null) {
//                updateScoreboard(player);
//            } else {
//                createNewScoreboard(player);
//            }
//        }
//    }
//
//    private void requestServerPlayerCounts() {
//        for (String serverName : listOfServersYouTrack) {
//            ByteArrayDataOutput out = ByteStreams.newDataOutput();
//            out.writeUTF("PlayerCount");
//            out.writeUTF(serverName); // Server name for which you're requesting the count
//            Bukkit.getOnlinePlayers().forEach(player ->
//                    player.sendPluginMessage(HubPro.getInstance(), "BungeeCord", out.toByteArray()));
//        }
//    }
//
//    private void createNewScoreboard(Player player) {
//        requestServerPlayerCounts();
//        String server = listOfServersYouTrack.get(0);
//        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
//        Objective objective = scoreboard.registerNewObjective("BendersMC", "dummy");
//
//
//        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "BendersMC");
//
//        objective.getScore(ChatColor.WHITE + " ").setScore(6);
//        objective.getScore(ChatColor.DARK_PURPLE + " ").setScore(5);
//        objective.getScore(ChatColor.GOLD + server + ": " + ChatColor.WHITE + serverPlayerCounts.get(server)).setScore(4);
//        objective.getScore(ChatColor.YELLOW + " ").setScore(3);
//        objective.getScore(ChatColor.WHITE + "Health: " + ChatColor.GREEN + player.getHealth() + ChatColor.WHITE + " hearts").setScore(2);
//        objective.getScore(ChatColor.GREEN + " ").setScore(1);
//        objective.getScore(ChatColor.WHITE + "Level: " + ChatColor.AQUA + player.getLevel()).setScore(0);
//
//        player.setScoreboard(scoreboard);
//    }
//
//    private void updateScoreboard(Player player) {
//        Scoreboard scoreboard = player.getScoreboard();
//        if (scoreboard.getObjective("BendersMC") == null) {
//            createNewScoreboard(player);
//            scoreboard = player.getScoreboard();
//        }
//        player.setScoreboard(scoreboard);
//    }
//
//    @Override
//    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
//        if (!channel.equals("BungeeCord")) {
//            return;
//        }
//        ByteArrayDataInput in = ByteStreams.newDataInput(message);
//        String subchannel = in.readUTF();
//        if (subchannel.equals("PlayerCount")) {
//            String server = in.readUTF();
//            int playerCount = in.readInt();
//            serverPlayerCounts.put("lobby", playerCount);
//        }
//    }
//}
