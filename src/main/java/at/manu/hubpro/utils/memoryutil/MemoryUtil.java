// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.utils.memoryutil;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.Config;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class MemoryUtil {

	// -- PLAYER RELATED "MEMORY" --
	public static Set<Player> hidePlayers;
	public static Set<Player> buildModePlayers;
	public static Map<UUID, Long> lastToggleTimestamp;
	public static Map<UUID, Long> movement_cooldown;

	// -- ITEM UTILITY "MEMORY" --
	public static Map<String, HubItem> hubItemByName;

	// -- UTILITY "MEMORY" --
	public static Map<String, Boolean> permissionsMap;
	public static Map<UUID, Map<String, Long>> cooldowns;

	// -- CONFIG "MEMORY" --
	public static Map<String, Config> menusConfigs;


	public MemoryUtil() {
		initiateLists();
	}

	private void initiateLists() {
		lastToggleTimestamp = new HashMap<>();
		movement_cooldown 	= new HashMap<>();
		buildModePlayers	= new HashSet<>();
		permissionsMap 		= new HashMap<>();
		hubItemByName 		= new HashMap<>();
		menusConfigs 		= new HashMap<>();
		hidePlayers 		= new HashSet<>();
		cooldowns 			= new HashMap<>();
	}

	public static void reloadMemoryAndPlayerItems() {
		lastToggleTimestamp	.clear();
		movement_cooldown	.clear();
		buildModePlayers	.clear();
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
		ConfigManager.itemsConfig.reload();
		ConfigManager.menu_serverSelectorConfig.reload();
		ConfigManager.loadMenusConfigs();
		HubPro.getInstance().getLogger().info("All menu configurations have been reloaded.");
	}

	public static Config getConfigByName(String name) {
		return menusConfigs.get(name);
	}

	public static Config findMenuConfigByTitle(String title) {
		for (Map.Entry<String, Config> entry : MemoryUtil.menusConfigs.entrySet()) {
			String configTitle = entry.getValue().get().getString("Title");
			if (MessageUtil.format(configTitle).equals(MessageUtil.format(title))) {
				return entry.getValue();
			}
		}
		return null;
	}
}
