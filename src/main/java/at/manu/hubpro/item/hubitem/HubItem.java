// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.item.hubitem;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;

@Setter
@Getter
public abstract class HubItem {
    private String itemName;
    private List<String> itemLore;
    private ItemStack item;

    @Nullable
    private String action;
    @Nullable
    private String menu;

    public HubItem(String itemName, List<String> itemLore, ItemStack item, @Nullable String action, @Nullable String menu) {
        this.itemName = itemName;
        this.itemLore = itemLore;
        this.item     = item;

        this.action   = action;
        this.menu     = menu;
        configureItem();
    }

    public HubItem(String itemName, List<String> itemLore, ItemStack item) {
        this.itemName = itemName;
        this.itemLore = itemLore;
        this.item     = item;

        configureItem();
    }

    private void configureItem() {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(itemName);
            if (itemLore != null && !itemLore.isEmpty()) {
                meta.setLore(itemLore);
            }
            addCustomItemFlags(meta);
            configureItemMeta(meta);
            item.setItemMeta(meta);
        }
    }

    protected abstract void configureItemMeta(ItemMeta meta);

    private void addCustomItemFlags(ItemMeta meta) {
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
    }
}
