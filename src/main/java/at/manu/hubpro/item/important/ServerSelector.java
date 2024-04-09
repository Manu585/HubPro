// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.item.important;

import at.manu.hubpro.item.hubitem.HubItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ServerSelector extends HubItem {
    public ServerSelector(String itemName, List<String> itemLore, ItemStack item, String action, String menu) {
        super(itemName, itemLore, item, action, menu);
    }

    @Override
    protected void configureItemMeta(ItemMeta meta) {}
}
