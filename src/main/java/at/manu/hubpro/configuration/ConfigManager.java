package at.manu.hubpro.configuration;

import at.manu.hubpro.utils.chatutil.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {
    public static Config defaultConfig;
    public static Config languageConfig;

    public ConfigManager() {
        defaultConfig = new Config(new File("config.yml"));
        languageConfig = new Config(new File("language.yml"));
        configCheck(ConfigType.DEFAULT);
        configCheck(ConfigType.LANGUAGE);
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
            config.addDefault("HubPro.HubItems.TpBow", MessageUtil.format("&6TpBow"));
            languageConfig.save();
        }
    }

    public static FileConfiguration getConfig() {
        return ConfigManager.defaultConfig.get();
    }
}
