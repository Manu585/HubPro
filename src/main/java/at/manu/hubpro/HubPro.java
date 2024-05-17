// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro;

import at.manu.hubpro.commands.HubProCommand;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.listeners.GarbageCollectListener;
import at.manu.hubpro.listeners.GeneralListeners;
import at.manu.hubpro.listeners.HubItemListener;
import at.manu.hubpro.manager.CooldownManager;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.memoryutil.MemoryUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
        new MemoryUtil();
        new ConfigManager();

        MemoryUtil.reloadMemoryAndPlayerItems();

        // PROXY COMMUNICATION
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // ITEM INITIALIZER
        HubItemInitializer.initHubItems();

        // EVENT REGISTRATION
        registerListener(
                HubItemListener.getInstance(),
                GeneralListeners.getInstance(),
                GarbageCollectListener.getInstance()
        );

        // COMMAND REGISTRATION
        Objects.requireNonNull(getCommand("hubpro")).setExecutor(new HubProCommand());
        Objects.requireNonNull(getCommand("hubpro")).setTabCompleter(new HubProCommand());
    }

    private void registerListener(Listener... listeners) {
        for (final Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
