package at.manu.hubpro.item.initializer;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.serveritem.ServerItem;
import at.manu.hubpro.item.serveritem.subitem.ServerConnectItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerItemInitializer {
    private static final Map<String, ServerItem> serverItems = new HashMap<>();
    private static ServerItem serverSelect;

    public static void initServerItems() {
        ConfigurationSection serverItemSection = ConfigManager.serverItemsConfig.get().getConfigurationSection("HubPro.ServerItems");
        if (serverItemSection != null) {
            for (String key : serverItemSection.getKeys(false)) {
                ConfigurationSection itemSection = serverItemSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String itemName = itemSection.getString("ItemName");
                    Material itemStackMaterial = Material.valueOf(itemSection.getString("ItemStack"));
                    List<?> itemLore = itemSection.getList("Lore");
                    serverSelect = new ServerConnectItem(key, itemName, (List<String>) itemLore, new ItemStack(itemStackMaterial));
                    serverItems.put(key, serverSelect);
                }
            }
        }
    }

    public static ServerItem getServerSelect() { return serverSelect; }
    public static ServerItem getServerItemByName(String name) { return serverItems.get(name); }
    public static ItemStack getServerSelectItem() { return serverSelect.getServerItemstack(); }
    public static ItemStack getServerItemStackByName(String name) {
        ServerItem serverItem = serverItems.get(name);
        if (serverItem != null) {
            return serverItem.getServerItemstack();
        }
        return null;
    }

}
