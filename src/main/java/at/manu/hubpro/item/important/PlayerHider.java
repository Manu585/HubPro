package at.manu.hubpro.item.important;

import at.manu.hubpro.item.hubitem.HubItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerHider extends HubItem {
    public PlayerHider(String itemName, List<String> itemLore, ItemStack item) {
        super(itemName, itemLore, item);
    }

    @Override
    protected void configureItemMeta(ItemMeta meta) {}
}
