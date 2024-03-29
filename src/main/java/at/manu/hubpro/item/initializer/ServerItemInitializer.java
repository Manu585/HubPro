package at.manu.hubpro.item.initializer;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.serveritem.ServerItem;
import at.manu.hubpro.item.serveritem.subitem.ServerConnectItem;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.serverItems;

public class ServerItemInitializer {
    @Getter
    private static ServerItem serverSelect;

    public static void initServerItems() {
        ConfigurationSection serverItemSection = ConfigManager.serverItemsConfig.get().getConfigurationSection("HubPro.ServerItems");
        if (serverItemSection != null) {
            for (String key : serverItemSection.getKeys(false)) {
                ConfigurationSection itemSection = serverItemSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String itemName = itemSection.getString("ItemName");
                    List<String> itemLore = itemSection.getStringList("Lore");
                    itemLore.replaceAll(MessageUtil::format);
                    Material itemStackMaterial = Material.valueOf(itemSection.getString("ItemStack"));
                    serverSelect = new ServerConnectItem(key, MessageUtil.format(itemName), itemLore, new ItemStack(itemStackMaterial));
                    serverItems.put(key, serverSelect);
                }
            }
        }
    }

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
