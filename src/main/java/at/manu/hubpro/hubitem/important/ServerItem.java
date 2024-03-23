package at.manu.hubpro.hubitem.important;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.hubitem.HubItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ServerItem extends HubItem {
    public ServerItem(String itemName, ItemStack item) {
        super(itemName, item);
    }

    @Override
    protected void initialize() {
        createLore();
        configureItemMeta();
    }

    @Override
    protected List<String> createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("Connect to lobby server!");
        return lore;
    }

    @Override
    protected void configureItemMeta() {
        ItemStack item = getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ConfigManager.serverItemsConfig.get().getString("HubPro.ServerItems.lobby.itemname"));
            meta.setLore(createLore());
            item.setItemMeta(meta);
        }
    }

    @Override
    public ChatColor getItemLoreColor() {
        return ChatColor.AQUA;
    }
}
