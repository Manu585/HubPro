package at.manu.hubpro.manager;

import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.buildModePlayers;

public class BuildMode {
	public static void enterBuildMode(Player p) {
		if (!isInBuildMode(p)) {
			p.getInventory().clear();
			p.setGameMode(GameMode.CREATIVE);
			p.sendTitle(MessageUtil.format("&6Build Mode"), MessageUtil.format("&bActivated"), 5, 40, 5);
			buildModePlayers.add(p);
		}
	}

	public static void leaveBuildMode(Player p) {
		if (isInBuildMode(p)) {
			p.getInventory().clear();
			p.setGameMode(GameMode.SURVIVAL);
			p.sendTitle(MessageUtil.format("&6Build Mode"), MessageUtil.format("&cDeactivated"), 5, 40, 5);
			GeneralMethods.getInstance().insertHubItems(p);
			buildModePlayers.remove(p);
		}
	}

	public static boolean isInBuildMode(Player p) {
		return buildModePlayers.contains(p);
	}
}
