package at.manu.hubpro.hubitem.initializer;

import at.manu.hubpro.hubitem.HubItem;
import at.manu.hubpro.hubitem.fun.TpBow;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HubItemInitializer {
    private static final Map<String, HubItem> hubItemByName = new HashMap<>();
    private static HubItem tpBow;

    public static void initHubItems() {
        tpBow = new TpBow("TpBow", new ItemStack(Material.BOW));
        registerHubItems();
    }

    public static HubItem getTpBow() { return tpBow; }

    public static ItemStack getTpBowItem() { return tpBow.getItem(); }

    private static void registerHubItems() {
        hubItemByName.put(tpBow.getItemName(), tpBow);
    }

    public static HubItem getHubItemByItemStack(ItemStack item) {
        if (item == null) {
            return null;
        }
        Map<HubItem, ItemStack> hubitemMap = new HashMap<>();
        hubitemMap.put(tpBow, getTpBowItem());

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
