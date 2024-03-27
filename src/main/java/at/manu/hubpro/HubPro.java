package at.manu.hubpro;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.item.initializer.ServerItemInitializer;
import at.manu.hubpro.listeners.HubItemListener;
import at.manu.hubpro.listeners.HubListeners;
import at.manu.hubpro.listeners.PlayerInVoidListener;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HubPro extends JavaPlugin {

    @Getter
    private static HubPro instance;

    @Getter
    public static Set<Player> hidePlayers = new HashSet<Player>();

    @Getter
    private static GeneralMethods generalMethods = new GeneralMethods();

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
        // PROXY COMMUNICATION
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        new ConfigManager();

        HubItemInitializer.initHubItems();
        ServerItemInitializer.initServerItems();

        getServer().getPluginManager().registerEvents(HubListeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(HubItemListener.getInstance(), this);
        getServer().getPluginManager().registerEvents(PlayerInVoidListener.getInstance(), this);
    }

}
