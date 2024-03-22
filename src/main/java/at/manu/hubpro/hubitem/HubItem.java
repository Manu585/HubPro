package at.manu.hubpro.hubitem;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class HubItem {
    private String itemName;
    private List<String> itemLore = new ArrayList<>();
    private ItemStack item;

    public HubItem(String itemName, ItemStack item) {
        this.itemName = itemName;
        this.item = item;
        initialize();
    }

    protected abstract void initialize();

    protected abstract List<String> createLore();

    protected abstract void configureItemMeta();

    public String getItemName() {
        return this.itemName;
    }

    public List<String> getItemLore() {
        return this.itemLore;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemLore(List<String> itemLore) {
        this.itemLore = itemLore;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public abstract ChatColor getItemLoreColor();
}
