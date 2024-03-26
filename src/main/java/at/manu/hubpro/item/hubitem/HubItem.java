package at.manu.hubpro.item.hubitem;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    public abstract ChatColor getItemLoreColor();
}
