package at.manu.hubpro.item.serveritem;

import at.manu.hubpro.configuration.ConfigManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class ServerItem {
    private String backendServerName;
    private String serverItemName;
    private List<String> serverItemLore = new ArrayList<>();
    private ItemStack serverItemstack;

    public ServerItem(String backendServerName, String serverItemName,
                      List<String> serverItemLore, ItemStack serverItemstack)
    {
        this.backendServerName  = backendServerName;
        this.serverItemName     = serverItemName;
        this.serverItemLore     = serverItemLore;
        this.serverItemstack    = serverItemstack;

        configureItemMeta();
    }

    public abstract List<String> createLore();

    protected void configureItemMeta() {
        ItemMeta meta = serverItemstack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ConfigManager.serverItemsConfig.get().getString(serverItemName + ".Name"));
        meta.setLore(createLore());
        serverItemstack.setItemMeta(meta);
    }

    public String getBackendServerName() {
        return this.backendServerName;
    }

    public String getServerItemName() {
        return this.serverItemName;
    }

    public List<String> getServerItemLore() {
        return this.serverItemLore;
    }

    public ItemStack getServerItemstack() {
        return this.serverItemstack;
    }

    public void setBackendServerName(String backendServerName) { this.backendServerName = backendServerName; }
    public void setServerItemName   (String serverItemName) { this.serverItemName = serverItemName; }
    public void setServerItemLore   (List<String> serverItemLore) { this.serverItemLore = serverItemLore; }
    public void setServerItemstack  (ItemStack serverItemstack) { this.serverItemstack = serverItemstack; }
}
