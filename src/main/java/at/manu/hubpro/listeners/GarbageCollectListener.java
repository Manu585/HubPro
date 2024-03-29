package at.manu.hubpro.listeners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.hidePlayers;
import static at.manu.hubpro.utils.memoryutil.MemoryUtil.lastToggleTimestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GarbageCollectListener implements Listener {

	@Getter
	private static GarbageCollectListener instance = new GarbageCollectListener();

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		hidePlayers.remove(e.getPlayer());
		lastToggleTimestamp.remove(e.getPlayer().getUniqueId());
	}
}
