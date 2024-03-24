package at.manu.hubpro.listeners;

import at.manu.hubpro.configuration.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class PlayerInVoidListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        int voidY = ConfigManager.defaultConfig.get().getInt("HubPro.VoidY");
        Player player = e.getPlayer();
        if (player.getLocation().getY() < voidY) {
            player.sendMessage(Objects.requireNonNull(ConfigManager.languageConfig.get().getString("HubPro.Chat.VoidMessage")));
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }
}
