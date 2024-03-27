package at.manu.hubpro.listeners;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.permission.PermissionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.hidePlayers;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HubListeners implements Listener {

    @Getter
    private static final HubListeners instance = new HubListeners();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().addItem(HubItemInitializer.getTpBowItem());

        if (hidePlayers != null) {
            if (hidePlayers.contains(p)) {
                Objects.requireNonNull(p.getInventory().getItem(8)).setType(HubItemInitializer.getPlayerShowerItem().getType());
            }

            for (Player player : hidePlayers) {
                player.hidePlayer(HubPro.getInstance(), p);
            }
        }

        HubPro.getGeneralMethods().sendTitle(p);
        HubPro.getGeneralMethods().insertHubItems(p);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        PermissionUtils permissionUtils = PermissionUtils.dropPermission(p);
        if (!permissionUtils.check()) {
            p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            PermissionUtils permissionUtils = PermissionUtils.inventoryClickPermission(p);
            if (!permissionUtils.check()) {
                p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PermissionUtils permissionUtils = PermissionUtils.pickupPermission(p);
            if (!permissionUtils.check()) {
                p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PermissionUtils permissionUtils = PermissionUtils.breakPermission(p);
        if (!permissionUtils.check()) {
            p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        PermissionUtils permissionUtils = PermissionUtils.placePermission(p);
        if (!permissionUtils.check()) {
            p.sendMessage(MessageUtil.noPermissionMessage(permissionUtils));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null) {
                if (e.getItem().isSimilar(HubItemInitializer.getTpBowItem())) {
                    //new ConnectionHelper().movePlayerToOtherServer(e.getPlayer(), "lobby");
                    //e.getPlayer().sendMessage("Yeah yeah");
                }
            }
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType().equals(Material.OAK_DOOR)) {
                        e.setCancelled(true);
                }
            }
        }
    }
}
