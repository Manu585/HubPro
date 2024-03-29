package at.manu.hubpro.item.serveritem.subitem;

import at.manu.hubpro.item.serveritem.ServerItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ServerConnectItem extends ServerItem {
    public ServerConnectItem(String backendServerName, String serverItemName, List<String> serverItemLore, ItemStack serverItemstack) {
        super(backendServerName, serverItemName, serverItemLore, serverItemstack);
    }
}
