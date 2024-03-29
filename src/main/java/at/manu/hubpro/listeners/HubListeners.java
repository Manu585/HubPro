package at.manu.hubpro.listeners;

import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.permission.PermissionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HubListeners implements Listener {

    @Getter
    private static final HubListeners instance = new HubListeners();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PermissionUtils permissionUtils = PermissionUtils.breakPermission(p);
        if (permissionUtils.check()) {
            p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        PermissionUtils permissionUtils = PermissionUtils.placePermission(p);
        if (permissionUtils.check()) {
            p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
            e.setCancelled(true);
        }
    }
}
