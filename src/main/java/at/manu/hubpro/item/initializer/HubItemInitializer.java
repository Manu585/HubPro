package at.manu.hubpro.item.initializer;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.item.hubitem.funitems.TpBow;
import at.manu.hubpro.item.important.ServerSelector;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HubItemInitializer {
    private static final Map<String, HubItem> hubItemByName = new HashMap<>();
    private static HubItem tpBow;
    private static HubItem serverSelector;

    public static void initHubItems() {
        tpBow = new TpBow("TpBow", new ItemStack(Material.BOW));
        serverSelector = new ServerSelector("ServerSelector", new ItemStack(Material.COMPASS));

        hubItemByName.put(tpBow.getItemName(), tpBow);
        hubItemByName.put(serverSelector.getItemName(), serverSelector);
    }

    public static HubItem getTpBow() { return tpBow; }
    public static HubItem getServerSelector() { return serverSelector; }

    public static ItemStack getTpBowItem() { return tpBow.getItem(); }
    public static ItemStack getServerSelectorItem() { return serverSelector.getItem(); }

    public static HubItem getServerItemByName(String name) {
        return hubItemByName.get(name);
    }

    public static boolean isHubItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return hubItemByName.values().stream().anyMatch(hubItem -> hubItem.getItem().isSimilar(item));
    }
}
