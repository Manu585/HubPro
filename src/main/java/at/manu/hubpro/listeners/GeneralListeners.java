// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.listeners;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.manager.BuildMode;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.permission.PermissionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;
import java.util.UUID;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.hidePlayers;
import static at.manu.hubpro.utils.memoryutil.MemoryUtil.movement_cooldown;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralListeners implements Listener {

	@Getter
	private static GeneralListeners instance = new GeneralListeners();

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (hidePlayers != null) {
			if (hidePlayers.contains(p)) {
				Objects.requireNonNull(p.getInventory().getItem(8)).setType(HubItemInitializer.getPlayerShowerItem().getType());
			}

			for (Player player : hidePlayers) {
				player.hidePlayer(HubPro.getInstance(), p);
			}
		}

		GeneralMethods.getInstance().sendTitle(p);
		GeneralMethods.getInstance().insertHubItems(p);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getFrom().getY() == Objects.requireNonNull(event.getTo()).getY()) return;

		UUID playerId = event.getPlayer().getUniqueId();
		if (movement_cooldown.containsKey(playerId)) {
			long timeSinceLastCheck = System.currentTimeMillis() - movement_cooldown.get(playerId);
			if (timeSinceLastCheck < 2000) {
				return;
			}
		}

		if (!ConfigManager.defaultConfig.get().getBoolean("HubPro.VoidTP.Enabled")) {
			return;
		}

		int voidY = ConfigManager.defaultConfig.get().getInt("HubPro.VoidTP.VoidY");
		if (event.getPlayer().getLocation().getY() < voidY) {
			event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
			movement_cooldown.put(playerId, System.currentTimeMillis());
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		PermissionUtils permissionUtils = PermissionUtils.dropPermission(p);
		if (permissionUtils.check() && !BuildMode.isInBuildMode(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickupItem(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player p) {
			PermissionUtils permissionUtils = PermissionUtils.pickupPermission(p);
			if (permissionUtils.check() && !BuildMode.isInBuildMode(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player p) {
			PermissionUtils permissionUtils = PermissionUtils.inventoryClickPermission(p);
			if (permissionUtils.check() && !BuildMode.isInBuildMode(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityHurt(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player p) {
			PermissionUtils permissionUtils = PermissionUtils.entityHurtPermission(p);
    		if (permissionUtils.check() && !BuildMode.isInBuildMode(p)) {
    			e.setCancelled(true);
    		}
    	}
	}
}
