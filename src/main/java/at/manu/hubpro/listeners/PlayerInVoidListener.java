package at.manu.hubpro.listeners;

import at.manu.hubpro.configuration.ConfigManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerInVoidListener implements Listener {

    @Getter
    private static final PlayerInVoidListener instance = new PlayerInVoidListener();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!ConfigManager.defaultConfig.get().getBoolean("HubPro.VoidTP.Enabled")){
            return;
        }
        int voidY = ConfigManager.defaultConfig.get().getInt("HubPro.VoidTP.VoidY");
        Player player = e.getPlayer();
        if (player.getLocation().getY() < voidY) {
            String voidMessage = ConfigManager.languageConfig.get().getString("HubPro.Chat.VoidTPMessage");
            if (voidMessage != null) {
                player.sendMessage(voidMessage);
            }
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }
}
