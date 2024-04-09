// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.utils.memoryutil;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.Config;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.methods.GeneralMethods;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class MemoryUtil {

	// -- PLAYER RELATED "MEMORY" --
	public static Set<Player> hidePlayers;
	public static Map<UUID, Long> lastToggleTimestamp;
	public static HashMap<UUID, Long> movement_cooldown;

	// -- ITEM UTILITY "MEMORY" --
	public static Map<String, HubItem> hubItemByName;

	// -- UTILITY "MEMORY" --
	public static Map<String, Boolean> permissionsMap;
	public static Map<UUID, Map<String, Long>> cooldowns;

	// -- CONFIG "MEMORY" --
	public static HashMap<String, Config> menusConfigs;


	public MemoryUtil() {
		initiateLists();
	}

	private void initiateLists() {
		lastToggleTimestamp = new HashMap<>();
		movement_cooldown 	= new HashMap<>();
		permissionsMap 		= new HashMap<>();
		hubItemByName 		= new HashMap<>();
		menusConfigs 		= new HashMap<>();
		hidePlayers 		= new HashSet<>();
		cooldowns 			= new HashMap<>();
	}

	public static void reloadMemoryAndPlayerItems() {
		lastToggleTimestamp	.clear();
		movement_cooldown	.clear();
		permissionsMap		.clear();
		hubItemByName		.clear();
		hidePlayers			.clear();
		cooldowns			.clear();

		for (Player player : HubPro.getInstance().getServer().getOnlinePlayers()) {
			player.getInventory().clear();
			GeneralMethods.getInstance().insertHubItems(player);
		}
	}

	public static void reloadAllConfigs() {
		for (Config config : menusConfigs.values()) {
			config.reload();
		}
		ConfigManager.defaultConfig.reload();
		ConfigManager.languageConfig.reload();
		ConfigManager.menu_serverSelectorConfig.reload();
		HubPro.getInstance().getLogger().info("All menu configurations have been reloaded.");
	}
}
