package at.manu.hubpro.item.initializer;

import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.item.hubitem.funitems.tpbow.TpArrow;
import at.manu.hubpro.item.hubitem.funitems.tpbow.TpBow;
import at.manu.hubpro.item.important.PlayerHider;
import at.manu.hubpro.item.important.ServerSelector;
import at.manu.hubpro.utils.memoryutil.MemoryUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.hubItemByName;

public class HubItemInitializer {
    @Getter
    private static HubItem tpBow;
    @Getter
    private static HubItem arrow;
    @Getter
    private static HubItem serverSelector;
    @Getter
    private static HubItem playerHider;
    @Getter
    private static HubItem playerShower;

    public static void initHubItems() {
        tpBow = new TpBow("TpBow", new ItemStack(Material.BOW));
        arrow = new TpArrow("Arrow", new ItemStack(Material.ARROW));
        serverSelector = new ServerSelector("ServerSelector", new ItemStack(Material.COMPASS));
        playerHider = new PlayerHider("PlayerHider", new ItemStack(Material.LIME_DYE));
        playerShower = new PlayerHider("PlayerShower", new ItemStack(Material.RED_DYE));

        hubItemByName.put(tpBow.getItemName(), tpBow);
        hubItemByName.put(arrow.getItemName(), arrow);
        hubItemByName.put(serverSelector.getItemName(), serverSelector);
        hubItemByName.put(playerHider.getItemName(), playerHider);
        hubItemByName.put(playerShower.getItemName(), playerShower);
    }

    public static ItemStack getTpBowItem() { return tpBow.getItem(); }
    public static ItemStack getArrowItem() { return arrow.getItem(); }
    public static ItemStack getServerSelectorItem() { return serverSelector.getItem(); }
    public static ItemStack getPlayerHiderItem() { return playerHider.getItem(); }
    public static ItemStack getPlayerShowerItem() { return playerShower.getItem(); }

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
