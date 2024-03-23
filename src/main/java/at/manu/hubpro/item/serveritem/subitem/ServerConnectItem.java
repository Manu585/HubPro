package at.manu.hubpro.item.serveritem.subitem;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.serveritem.ServerItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerConnectItem extends ServerItem {
    public ServerConnectItem(String backendServerName, String serverItemName, List<String> serverItemLore, ItemStack serverItemstack) {
        super(backendServerName, serverItemName, serverItemLore, serverItemstack);
    }

    @Override
    public List<String> createLore() {
        // Initialize lore list outside of the loop
        List<String> lore = new ArrayList<>();
        // Safely attempt to fetch the lore list from the configuration
        List<?> configLore = ConfigManager.serverItemsConfig.get().getList("HubPro.ServerItems." + getBackendServerName() + ".Lore");
        if (configLore != null) {
            for (Object line : configLore) {
                // Add each line from the configuration to the lore list
                lore.add(line.toString());
            }
        }
        // Set the complete list of lore lines to the item
        setServerItemLore(lore);
        // Return the full lore list
        return new ArrayList<>(getServerItemLore());
    }
}
