package at.manu.hubpro.utils.memoryutil;

import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.item.serveritem.ServerItem;
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
	public static Map<String, ServerItem> serverItems;

	// -- UTILITY "MEMORY" --
	public static Map<String, Boolean> permissionsMap;
	public static Map<UUID, Map<String, Long>> cooldowns;


	public MemoryUtil() {
		lastToggleTimestamp = new HashMap<>();
		movement_cooldown = new HashMap<>();
		permissionsMap = new HashMap<>();
		hubItemByName = new HashMap<>();
		hidePlayers = new HashSet<>();
    	serverItems = new HashMap<>();
		cooldowns = new HashMap<>();
	}
}
