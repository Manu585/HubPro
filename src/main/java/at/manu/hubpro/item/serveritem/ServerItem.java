package at.manu.hubpro.item.serveritem;

import at.manu.hubpro.configuration.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Setter
@Getter
public abstract class ServerItem {
    private String backendServerName;
    private String serverItemName;
    private List<String> serverItemLore;
    private ItemStack serverItemstack;

    public ServerItem(String backendServerName, String serverItemName, List<String> serverItemLore, ItemStack serverItemstack)
    {
        this.backendServerName  = backendServerName;
        this.serverItemName     = serverItemName;
        this.serverItemLore     = serverItemLore;
        this.serverItemstack    = serverItemstack;

        configureItemMeta();
    }

    protected void configureItemMeta() {
        ItemMeta meta = serverItemstack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ConfigManager.serverItemsConfig.get().getString(serverItemName + ".Name"));
        serverItemstack.setItemMeta(meta);
    }
}
