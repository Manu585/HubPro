// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.commands;

import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.manager.BuildMode;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.memoryutil.MemoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HubProCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length > 0) {
			if ("reloadconfig".equalsIgnoreCase(args[0])) {
				if (!sender.hasPermission("hubpro.command.reload")) {
					sender.sendMessage("You do not have permission to execute this command.");
					return true;
				}
				MemoryUtil.reloadAllConfigs();
				HubItemInitializer.initHubItems();
				MemoryUtil.reloadMemoryAndPlayerItems();
				sender.sendMessage(MessageUtil.format("&#38a120Configuration and memory have been reloaded."));
				return true;
			} else if ("buildmode".equalsIgnoreCase(args[0])) {
				if (!sender.hasPermission("hubpro.buildmode")) {
					sender.sendMessage("You do not have permission to execute this command.");
					return true;
				}
				Player p = (Player) sender;
				if (BuildMode.isInBuildMode(p)) {
					BuildMode.leaveBuildMode(p);
				} else {
					BuildMode.enterBuildMode(p);
				}
				return true;
			}
		}
		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			List<String> subCommands = new ArrayList<>();
			subCommands.add("reloadconfig");
			subCommands.add("buildmode");
			return subCommands;
		}
		return null;
	}
}
