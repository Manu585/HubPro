// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.cooldowns;

public class CooldownManager {
	/**
	 * Starts or updates a cooldown for a given player and action.
	 *
	 * @param player The player to set the cooldown for.
	 * @param action The name of the action or item to cooldown.
	 * @param durationInSeconds The duration of the cooldown in seconds.
	 */
	public void setCooldown(Player player, String action, int durationInSeconds) {
		cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(action, System.currentTimeMillis() + durationInSeconds * 1000L);
	}

	/**
	 * Checks if a given player is currently on cooldown for a specified action.
	 *
	 * @param player The player to check the cooldown for.
	 * @param action The name of the action or item to check.
	 * @return true if the player is currently on cooldown, false otherwise.
	 */
	public boolean isOnCooldown(Player player, String action) {
		Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
		if (playerCooldowns == null) {
			return false;
		}
		Long cooldownTime = playerCooldowns.get(action);
		if (cooldownTime == null) {
			return false;
		}
		return System.currentTimeMillis() < cooldownTime;
	}

	/**
	 * Gets the remaining cooldown time in seconds for a given player and action.
	 * If the player is not on cooldown, returns 0.
	 *
	 * @param player The player to get the cooldown time for.
	 * @param action The name of the action or item to check.
	 * @return The remaining cooldown time in seconds, or 0 if not on cooldown.
	 */
	public long getRemainingCooldown(Player player, String action) {
		if (!isOnCooldown(player, action)) {
			return 0;
		}
		long currentTime = System.currentTimeMillis();
		long cooldownEnd = cooldowns.get(player.getUniqueId()).get(action);
		return (cooldownEnd - currentTime) / 1000L;
	}
}
