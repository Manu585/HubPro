package at.manu.hubpro.hubitem.initializer;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.hubitem.HubItem;
import at.manu.hubpro.hubitem.fun.TpBow;
import at.manu.hubpro.hubitem.important.ServerItem;
import at.manu.hubpro.hubitem.important.ServerSelector;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HubItemInitializer {
    private static final Map<String, HubItem> hubItemByName = new HashMap<>();
    private static final Map<String, ServerItem> serverItems = new HashMap<>();
    private static HubItem tpBow;
    private static HubItem serverSelector;

    public static void initHubItems() {
        tpBow = new TpBow("TpBow", new ItemStack(Material.BOW));
        serverSelector = new ServerSelector("ServerSelector", new ItemStack(Material.COMPASS));

        ConfigurationSection serverItemSection = ConfigManager.serverItemsConfig.get().getConfigurationSection("HubPro.ServerItems");
        if (serverItemSection != null) {
            for (String key : serverItemSection.getKeys(false)) {
                ConfigurationSection itemSection = serverItemSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String itemName = itemSection.getString("ItemName");
                    Material itemStackMaterial = Material.valueOf(itemSection.getString("ItemStack"));
                    ServerItem serverItem = new ServerItem(key, itemName, new ItemStack(itemStackMaterial));
                    serverItems.put(key, serverItem);
                    // Register each server item individually
                    hubItemByName.put(itemName, serverItem);
                }
            }
        }
        // Now, directly register tpBow and serverSelector without using serverItem
        hubItemByName.put(tpBow.getItemName(), tpBow);
        hubItemByName.put(serverSelector.getItemName(), serverSelector);
    }

    public static HubItem getTpBow() { return tpBow; }
    public static HubItem getServerSelector() { return serverSelector; }

    public static ItemStack getTpBowItem() { return tpBow.getItem(); }
    public static ItemStack getServerSelectorItem() { return serverSelector.getItem(); }

    public static ItemStack getServerItemStackByName(String serverItemName) {
        ServerItem serverItem = serverItems.get(serverItemName);
        if(serverItem != null) {
            return serverItem.getItem();
        }
        return null;
    }

    public static HubItem getServerItemByName(String name) {
        return hubItemByName.get(name);
    }

    public static boolean isHubItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return hubItemByName.values().stream().anyMatch(hubItem -> hubItem.getItem().isSimilar(item));
    }
}
