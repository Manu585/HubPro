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

            config.options().copyDefaults(true);
            defaultConfig.save();
        } else if (type == ConfigType.LANGUAGE) {
            config = languageConfig.get();
            config.addDefault("HubPro.Chat.Prefix", MessageUtil.format("&7[&dHub&5Pro&7]"));

            config.addDefault("HubPro.Title.Enabled", true);
            config.addDefault("HubPro.Title.MainTitle", MessageUtil.format("&b&lWelcome"));
            config.addDefault("HubPro.Title.SubTitle", MessageUtil.format("&5%player%"));
            config.addDefault("HubPro.Title.BlendIn", 5);
            config.addDefault("HubPro.Title.Stay", 60);
            config.addDefault("HubPro.Title.BlendOut", 5);

            config.addDefault("HubPro.HubItems.TpBow.Name", MessageUtil.format("&6TpBow"));
            config.addDefault("HubPro.HubItems.TpBow.Arrow.Name", MessageUtil.format("&4Mighty Arrow"));

            config.addDefault("HubPro.HubItems.ServerSelector", MessageUtil.format("&2Server Selector"));

            config.addDefault("HubPro.HubItems.PlayerHider.Name", MessageUtil.format("&7Hide Players"));
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
            config.set(basePath + ".Lobby.Server", "lobbyServerName"); // Server to connect
            config.set(basePath + ".Lobby.ItemName", "&6Lobby");
            List<String> lobbyLore = new ArrayList<>();
            lobbyLore.add(MessageUtil.format("&6YeahYeah"));
            lobbyLore.add(MessageUtil.format("&4I am modular"));
            config.set(basePath + ".Lobby.Lore", lobbyLore);
            config.set(basePath + ".Lobby.ItemStack", "GRASS_BLOCK");
            config.set(basePath + ".Lobby.menuplace", 12);

            config.set(basePath + ".Survival.Server", "survivalServerName");
            config.set(basePath + ".Survival.ItemName", "&2Survival");
            List<String> survivalLore = new ArrayList<>();
            survivalLore.add(MessageUtil.format("&6YeahYeah"));
            survivalLore.add(MessageUtil.format("&4I am modular"));
            config.set(basePath + ".Survival.Lore", survivalLore);
            config.set(basePath + ".Survival.ItemStack", "DIRT");
            config.set(basePath + ".Survival.menuplace", 14);
        }
    }

    public static FileConfiguration getConfig() {
        return defaultConfig.get();
    }
}
