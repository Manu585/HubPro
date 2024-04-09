// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.item.hubitem.funitems.tpbow;

import at.manu.hubpro.item.hubitem.HubItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TpArrow extends HubItem {
    public TpArrow(String itemName, List<String> itemLore, ItemStack item) {
        super(itemName, itemLore, item);
    }

    @Override
    protected void configureItemMeta(ItemMeta meta) {}
}
