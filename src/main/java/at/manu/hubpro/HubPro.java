package at.manu.hubpro;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.item.initializer.ServerItemInitializer;
import at.manu.hubpro.listeners.HubItemListener;
import at.manu.hubpro.listeners.HubListeners;
import at.manu.hubpro.listeners.PlayerInVoidListener;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class HubPro extends JavaPlugin {

    private static HubPro instance;
    private static GeneralMethods generalMethods = new GeneralMethods();
    // private BukkitTask scoreboardTask;

    @Override
    public void onEnable() {
        instance = this;
        initializer();
        getServer().getConsoleSender().sendMessage(MessageUtil.serverStartMessage());

        // scoreboardTask = getServer().getScheduler().runTaskTimer(this, Board.getInstance(), 0, 1);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(MessageUtil.serverStopMessage());

        // if (scoreboardTask != null && !scoreboardTask.isCancelled()) {
        //    scoreboardTask.cancel();
        //}
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

        HubItemInitializer.initHubItems();
        ServerItemInitializer.initServerItems();

        // LISTENERS
        getServer().getPluginManager().registerEvents(new HubListeners(), this);
        getServer().getPluginManager().registerEvents(new HubItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInVoidListener(), this);
    }

    public static GeneralMethods getGeneralMethods() {
        return generalMethods;
    }

    public static HubPro getInstance() {
        return instance;
    }
}
