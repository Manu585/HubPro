package at.manu.hubpro.hubitem.initializer;

import at.manu.hubpro.hubitem.HubItem;
import at.manu.hubpro.hubitem.fun.TpBow;
import at.manu.hubpro.hubitem.important.ServerItem;
import at.manu.hubpro.hubitem.important.ServerSelector;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HubItemInitializer {
    private static final Map<String, HubItem> hubItemByName = new HashMap<>();
    private static HubItem tpBow;
    private static HubItem serverSelector;
    private static HubItem serverItem;

    public static void initHubItems() {
        tpBow = new TpBow("TpBow", new ItemStack(Material.BOW));
        serverSelector = new ServerSelector("ServerSelector", new ItemStack(Material.COMPASS));
        serverItem = new ServerItem("Lobby", new ItemStack(Material.GRASS_BLOCK));
        registerHubItems();
    }

    public static HubItem getTpBow() { return tpBow; }
    public static HubItem getServerSelector() { return serverSelector; }
    public static HubItem getServerItem() { return serverItem; }

    public static ItemStack getTpBowItem() { return tpBow.getItem(); }
    public static ItemStack getServerSelectorItem() { return serverSelector.getItem(); }
    public static ItemStack getServeritemItem() { return serverItem.getItem(); }

    private static void registerHubItems() {
        hubItemByName.put(tpBow.getItemName(), tpBow);
        hubItemByName.put(serverSelector.getItemName(), serverSelector);
        hubItemByName.put(serverItem.getItemName(), serverItem);
    }

    public static HubItem getHubItemByItemStack(ItemStack item) {
        if (item == null) {
            return null;
        }
        Map<HubItem, ItemStack> hubitemMap = new HashMap<>();
        hubitemMap.put(tpBow, getTpBowItem());
        hubitemMap.put(serverSelector, getServerSelectorItem());
        hubitemMap.put(serverItem, getServeritemItem());

        for (Map.Entry<HubItem, ItemStack> entry : hubitemMap.entrySet()) {
            if (isHubItem(item, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static HubItem getHubItemByName(String name) {
        return hubItemByName.get(name);
    }

    private static boolean isHubItem(ItemStack item, ItemStack hubItem) {
        return item.isSimilar(hubItem);
    }
}
