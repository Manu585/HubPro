// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.commands;

import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.manager.BuildMode;
import at.manu.hubpro.methods.GeneralMethods;
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
			Player p;
			if ("reloadconfig".equalsIgnoreCase(args[0])) {
				if (!sender.hasPermission("hubpro.command.reload")) {
					sender.sendMessage("You do not have permission to execute this command.");
					return true;
				}
				MemoryUtil.reloadAllConfigs();
				HubItemInitializer.initHubItems();
				MemoryUtil.reloadMemoryAndPlayerItems();
				sender.sendMessage(MessageUtil.format(MessageUtil.getPrefix() + "&#38a120Configuration and memory have been reloaded."));
				return true;
			} else if ("buildmode".equalsIgnoreCase(args[0])) {
				if (!sender.hasPermission("hubpro.buildmode")) {
					sender.sendMessage("You do not have permission to execute this command.");
					return true;
				}
				p = (Player) sender;
				if (BuildMode.isInBuildMode(p)) {
					BuildMode.leaveBuildMode(p);
				} else {
					BuildMode.enterBuildMode(p);
				}
				return true;
			} else if ("setspawn".equalsIgnoreCase(args[0])) {
				if (!sender.hasPermission("hubpro.setspawn")) {
					sender.sendMessage("You do not have permission to execute this command.");
					return true;
				}
				p = (Player) sender;
				GeneralMethods.getInstance().setPlayerSpawn(p);
				sender.sendMessage(MessageUtil.getPrefix() + MessageUtil.format("&6Successfully set new hub spawn point!"));
			}
		}
		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			List<String> subCommands = new ArrayList<>();
			subCommands.add("buildmode");
			subCommands.add("reloadconfig");
			subCommands.add("setspawn");
			String arg = args[0].toLowerCase();
			for (String subCommand : subCommands) {
				if (subCommand.toLowerCase().startsWith(arg)) {
					if (subCommand.toLowerCase().equalsIgnoreCase(arg)) {
						return List.of(args);
					}
					return subCommands;
				}
			}
		}
		return null;
	}
}
