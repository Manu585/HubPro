package at.manu.hubpro;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.hubitem.initializer.HubItemInitializer;
import at.manu.hubpro.listeners.HubListeners;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class HubPro extends JavaPlugin {

    private static HubPro instance;

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

    // Initializers
    private void initializer() {
        // FOR PROXY COMMUNICATION
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // CONFIG
        new ConfigManager();

        // LISTENERS
        getServer().getPluginManager().registerEvents(new HubListeners(), this);


        HubItemInitializer.initHubItems();
    }

    public static HubPro getInstance() {
        return instance;
    }
}
