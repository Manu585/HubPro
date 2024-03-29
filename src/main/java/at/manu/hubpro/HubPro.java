package at.manu.hubpro;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.item.initializer.ServerItemInitializer;
import at.manu.hubpro.listeners.GarbageCollectListener;
import at.manu.hubpro.listeners.GeneralListeners;
import at.manu.hubpro.listeners.HubItemListener;
import at.manu.hubpro.listeners.HubListeners;
import at.manu.hubpro.manager.CooldownManager;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.memoryutil.MemoryUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HubPro extends JavaPlugin {
    @Getter
    private static HubPro instance;
    @Getter
    private static CooldownManager cooldownManager = new CooldownManager();

    @Override
    public void onEnable() {
        instance = this;

        initializer();
        getServer().getConsoleSender().sendMessage(MessageUtil.serverStartMessage());
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(MessageUtil.serverStopMessage());
    }


    private void initializer() {
        // CONFIG MANAGEMENT
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        new ConfigManager();
        new MemoryUtil();

        // Doing that to make GeneralMethods.generateItemStacksFromConfig();
        // load the items when no config was there at the start of the server.
        Bukkit.getScheduler().runTaskLater(this, () -> ConfigManager.serverItemsConfig.reload(), 50L);

        // PROXY COMMUNICATION
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // ITEM INITIALIZER
        HubItemInitializer.initHubItems();
        ServerItemInitializer.initServerItems();

        // EVENT REGISTRATION
        if (ConfigManager.defaultConfig.get().getBoolean("HubPro.Permissions.Enabled")) {
            getServer().getPluginManager().registerEvents(HubListeners.getInstance(), this);
        }
        getServer().getPluginManager().registerEvents(HubItemListener.getInstance(), this);
        getServer().getPluginManager().registerEvents(GeneralListeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(GarbageCollectListener.getInstance(), this);
    }
}
