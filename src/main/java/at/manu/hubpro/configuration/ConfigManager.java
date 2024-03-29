package at.manu.hubpro.configuration;

import at.manu.hubpro.utils.chatutil.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    public static Config defaultConfig;
    public static Config languageConfig;
    public static Config serverItemsConfig;

    public ConfigManager() {
        defaultConfig = new Config(new File("config.yml"));
        languageConfig = new Config(new File("language.yml"));
        serverItemsConfig = new Config(new File("serveritemsconfig.yml"));
        configCheck(ConfigType.DEFAULT);
        configCheck(ConfigType.LANGUAGE);
        configCheck(ConfigType.SERVERITEMS);
    }

    public static void configCheck(final ConfigType type) {
        FileConfiguration config;
        if (type == ConfigType.DEFAULT) {
            config = defaultConfig.get();
            config.addDefault("HubPro.Permissions.Enabled", false);
            config.addDefault("HubPro.VoidTP.Enabled", true);
            config.addDefault("HubPro.VoidTP.VoidY", -65);
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

            // -- PLAYER HIDER --
            config.addDefault("HubPro.HubItems.PlayerHider.Name", "&#b228d1Players &7> &#52d128Shown &7(Right-Click)");
            List<String> playerHiderLore = new ArrayList<>();
            playerHiderLore.add("&7Click to hide all players!");
            config.addDefault("HubPro.HubItems.PlayerHider.Lore", playerHiderLore);
            config.addDefault("HubPro.HubItems.PlayerHider.ItemStack", "LIME_DYE");
            config.addDefault("HubPro.HubItems.PlayerHider.HideMessage", MessageUtil.getPrefix() + " " + "&cPlayer visibility disabled");
            config.addDefault("HubPro.HubItems.PlayerHider.spamCooldown", 3);
            config.addDefault("HubPro.HubItems.PlayerHider.SpamMessage", "&cYou have to wait &2%seconds% &cseconds before being able to toggle again!");

            // -- PLAYER SHOWER --
            config.addDefault("HubPro.HubItems.PlayerShower.Name", "&#b228d1Players &7> &#d12828Hidden &7(Right-Click)");
        	List<String> showPlayerLore = new ArrayList<>();
        	showPlayerLore.add("&7Click to show all players!");
        	config.addDefault("HubPro.HubItems.PlayerShower.Lore", showPlayerLore);
            config.addDefault("HubPro.HubItems.PlayerShower.ItemStack", "RED_DYE");
            config.addDefault("HubPro.HubItems.PlayerShower.ShowMessage", MessageUtil.getPrefix() + " " + "&aPlayer visibility enabled");
            languageConfig.save();
        }  else if (type == ConfigType.SERVERITEMS) {
            config = serverItemsConfig.get();
            ensureServerItemsConfig(config);
            serverItemsConfig.save();
        }
    }
    private static void ensureServerItemsConfig(FileConfiguration config) {
        String basePath = "HubPro.ServerItems";
        if (!config.isConfigurationSection(basePath)) {
            config.addDefault(basePath + ".Lobby.Server", "Lobby"); // Server to connect
            config.addDefault(basePath + ".Lobby.ItemName", "&#314894Lobby");
            List<String> lobbyLore = new ArrayList<>();
            lobbyLore.add("&7Server Info");
            lobbyLore.add("");
            lobbyLore.add("&fExplore the world, play with your friends,");
            lobbyLore.add("&fand bend the elements on &#314894Lobby&f!");
            lobbyLore.add("");
            lobbyLore.add("&7[ &#6cab2cClick to join! &7]");
            config.addDefault(basePath + ".Lobby.Lore", lobbyLore);
            config.addDefault(basePath + ".Lobby.ItemStack", "GRASS_BLOCK");
            config.addDefault(basePath + ".Lobby.menuplace", 12);

            config.addDefault(basePath + ".Survival.Server", "survivalServerName");
            config.addDefault(basePath + ".Survival.ItemName", "&#70cf89Survival");
            List<String> survivalLore = new ArrayList<>();
            survivalLore.add("&7Server Info");
            survivalLore.add("");
            survivalLore.add("&fExplore the world, play with your friends,");
            survivalLore.add("&fand bend the elements on &#70cf89Survival&f!");
            survivalLore.add("");
            survivalLore.add("&7[ &#6cab2cClick to join! &7]");
            config.addDefault(basePath + ".Survival.Lore", survivalLore);
            config.addDefault(basePath + ".Survival.ItemStack", "DIRT");
            config.addDefault(basePath + ".Survival.menuplace", 14);
        }
    }

    public static FileConfiguration getConfig() {
        return defaultConfig.get();
    }
}
