package at.manu.hubpro.item.hubitem.funitems.tpbow;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.hubitem.HubItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TpArrow extends HubItem {
    public TpArrow(String itemName, ItemStack item) {
        super(itemName, item);
    }

    @Override
    protected void initialize() {
        setItemLore(createLore());
        configureItemMeta();
    }

    @Override
    protected List<String> createLore() {
        List<String> lore = new ArrayList<>();
        lore.add(getItemLoreColor() + "Arrow!");
        return lore;
    }

    @Override
    protected void configureItemMeta() {
        ItemStack item = getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ConfigManager.languageConfig.get().getString("HubPro.HubItems.TpBow.Arrow.Name"));
            meta.setLore(getItemLore());
            item.setItemMeta(meta);
        }
    }

    @Override
    public ChatColor getItemLoreColor() {
        return ChatColor.GOLD;
    }
}
