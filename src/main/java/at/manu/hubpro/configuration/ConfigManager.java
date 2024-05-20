// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.configuration;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.utils.memoryutil.MemoryUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

	@Getter
	public static Config defaultConfig;

	@Getter
	public static Config languageConfig;

	@Getter
	public static Config itemsConfig;

	@Getter
	public static Config serverItemsConfig;

	@Getter
	public static Config menu_serverSelectorConfig;

	@Getter
	public static File menu_dir;

	public ConfigManager() {
		loadCoreConfigs();
		loadMenusConfigs();
	}


	private void loadCoreConfigs() {
		itemsConfig    = new Config(new File("items.yml"));
		defaultConfig  = new Config(new File("config.yml"));
		languageConfig = new Config(new File("language.yml"));

		configCheck(ConfigType.DEFAULT);
		configCheck(ConfigType.LANGUAGE);
		configCheck(ConfigType.ITEMS);
	}


	public static void loadMenusConfigs() {
		menu_dir = new File(HubPro.getInstance().getDataFolder(), "menus");

		if (!menu_dir.exists()) {
			boolean isCreated = menu_dir.mkdirs();
			if (isCreated) {
				HubPro.getInstance().getLogger().info("Created menus directory");
			} else {
				HubPro.getInstance().getLogger().severe("Failed to create menus directory");
				return;
			}
		}

		File serverSelectorMenuFile = new File(menu_dir, "serverselector_menu.yml");

		menu_serverSelectorConfig = new Config(serverSelectorMenuFile);
		configCheck(ConfigType.MENU);

		File[] menuFiles = menu_dir.listFiles((directory, name) -> name.endsWith("_menu.yml"));
		HubPro.getInstance().getLogger().info("Checking menu files in directory: " + menu_dir.getAbsolutePath());
		if (menuFiles != null) {
			for (File file : menuFiles) {
				Config config = new Config(file);
				String menuName = file.getName().replace(".yml", "");
				MemoryUtil.menusConfigs.put(menuName, config);
				HubPro.getInstance().getLogger().info("Loaded menu config for: " + menuName);
			}
		} else {
			HubPro.getInstance().getLogger().info("No menu files found.");
		}
		HubPro.getInstance().getLogger().info("Loaded menus: " + MemoryUtil.menusConfigs.keySet());
	}


	public static void configCheck(final ConfigType type) {
		FileConfiguration config;
		if (type == ConfigType.DEFAULT) {
			config = defaultConfig.get();
			config.addDefault("HubPro.VoidTP.Enabled", true);
			config.addDefault("HubPro.VoidTP.VoidY", -65);

			config.addDefault("HubPro.Items.PlayerHider.Cooldown", 4);
			config.addDefault("HubPro.Items.PlayerHider.SpamCooldown", 3);
			config.options().copyDefaults(true);
			defaultConfig.save();
		} else if (type == ConfigType.LANGUAGE) {
			config = languageConfig.get();
			config.addDefault("HubPro.Chat.Prefix", "&7[&dHub&5Pro&7]");

			config.addDefault("HubPro.Title.Enabled", true);
			config.addDefault("HubPro.Title.MainTitle", "&b&lWelcome");
			config.addDefault("HubPro.Title.SubTitle", "&5%player%");
			config.addDefault("HubPro.Title.BlendIn", 5);
			config.addDefault("HubPro.Title.Stay", 60);
			config.addDefault("HubPro.Title.BlendOut", 5);

			config.addDefault("HubPro.Items.PlayerHider.HideMessage", "&cPlayer visibility disabled");
			config.addDefault("HubPro.Items.PlayerHider.SpamMessage", "&cYou have to wait &2%seconds% &cseconds before being able to toggle again!");

			config.addDefault("HubPro.Items.PlayerShower.ShowMessage", "&aPlayer visibility enabled");

			languageConfig.save();
		} else if (type == ConfigType.ITEMS) {
			config = itemsConfig.get();
			// -- TP-BOW --
			config.addDefault("HubPro.HubItems.TpBow.Name", "&#bf227bTp-Bow");
			List<String> tpBowLore = new ArrayList<>();
			tpBowLore.add("&#bf22b2Shoot an &#5b28d1arrow &#bf22b2and find out what will happen!");
			config.addDefault("HubPro.HubItems.TpBow.Lore", tpBowLore);
			config.addDefault("HubPro.HubItems.TpBow.ItemStack", "BOW");

			// -- MIGHTY ARROW --
			config.addDefault("HubPro.HubItems.TpBow.Arrow.Name", "&#5b28d1Mighty Arrow");
			List<String> mightyArrowLore = new ArrayList<>();
			mightyArrowLore.add("&#285bd1You found the &#5b28d1arrow&#285bd1!");
			config.addDefault("HubPro.HubItems.TpBow.Arrow.Lore", mightyArrowLore);
			config.addDefault("HubPro.HubItems.TpBow.Arrow.ItemStack", "ARROW");

			// -- SERVER SELECTOR --
			config.addDefault("HubPro.HubItems.ServerSelector.Name", "&#edd924Server Selector");
			List<String> serverSelectorLore = new ArrayList<>();
			serverSelectorLore.add("&#ed9324Right-Click &7to open the &#edd924Server Selector&7!");
			config.addDefault("HubPro.HubItems.ServerSelector.Lore", serverSelectorLore);
			config.addDefault("HubPro.HubItems.ServerSelector.ItemStack", "COMPASS");
			config.addDefault("HubPro.HubItems.ServerSelector.Action", "OPEN_MENU");
			config.addDefault("HubPro.HubItems.ServerSelector.Menu", "serverselector_menu");

			// -- PLAYER HIDER --
			config.addDefault("HubPro.HubItems.PlayerHider.Name", "&#b228d1Players &7> &#52d128Shown &7(Right-Click)");
			List<String> playerHiderLore = new ArrayList<>();
			playerHiderLore.add("&7Click to hide all players!");
			config.addDefault("HubPro.HubItems.PlayerHider.Lore", playerHiderLore);
			config.addDefault("HubPro.HubItems.PlayerHider.ItemStack", "LIME_DYE");

			// -- PLAYER SHOWER --
			config.addDefault("HubPro.HubItems.PlayerShower.Name", "&#b228d1Players &7> &#d12828Hidden &7(Right-Click)");
			List<String> showPlayerLore = new ArrayList<>();
			showPlayerLore.add("&7Click to show all players!");
			config.addDefault("HubPro.HubItems.PlayerShower.Lore", showPlayerLore);
			config.addDefault("HubPro.HubItems.PlayerShower.ItemStack", "RED_DYE");
			itemsConfig.save();
		} else if (type == ConfigType.MENU) {
			config = menu_serverSelectorConfig.get();
			config.addDefault("Title", "&6Server Selector");
			config.addDefault("Size", 3);
			config.addDefault("Fill_rest_with_items", true);
			config.addDefault("rest_items", "GRAY_STAINED_GLASS_PANE");
			config.addDefault("rest_item_name", "fill");
			config.addDefault("items.survival.Action", "CONNECT");
			config.addDefault("items.survival.Server", "survival");
			config.addDefault("items.survival.ItemName", "&6Survival");
			List<String> survivalLore = new ArrayList<>();
			survivalLore.add("Click to connect to survival!");
			config.addDefault("items.survival.ItemLore", survivalLore);
			config.addDefault("items.survival.ItemStack", "STONE");
			config.addDefault("items.survival.MenuPlace", 14);
			menu_serverSelectorConfig.save();
		}
	}
}
