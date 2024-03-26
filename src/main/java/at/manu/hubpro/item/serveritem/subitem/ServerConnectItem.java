package at.manu.hubpro.item.serveritem.subitem;

import at.manu.hubpro.item.serveritem.ServerItem;
import org.bukkit.inventory.ItemStack;

public class ServerConnectItem extends ServerItem {
    public ServerConnectItem(String backendServerName, String serverItemName, ItemStack serverItemstack) {
        super(backendServerName, serverItemName, serverItemstack);
    }
}
