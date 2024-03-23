package at.manu.hubpro.hubitem.important;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.hubitem.HubItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ServerItem extends HubItem {
    private String serverName;

    public ServerItem(String serverName, String itemName, ItemStack item) {
        super(itemName, item);
        this.serverName =serverName;
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
            String itemNamePath = "HubPro.ServerItems." + getItemName() + ".itemname";
            meta.setDisplayName(ConfigManager.serverItemsConfig.get().getString(itemNamePath));
            meta.setLore(createLore());
            item.setItemMeta(meta);
        }
    }

    @Override
    public ChatColor getItemLoreColor() {
        return ChatColor.AQUA;
    }

    public String getServerName() {
        return this.serverName;
    }
}
