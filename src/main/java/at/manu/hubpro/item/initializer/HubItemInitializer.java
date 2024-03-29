package at.manu.hubpro.item.initializer;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.item.hubitem.funitems.tpbow.TpArrow;
import at.manu.hubpro.item.hubitem.funitems.tpbow.TpBow;
import at.manu.hubpro.item.important.PlayerHider;
import at.manu.hubpro.item.important.ServerSelector;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

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
        FileConfiguration config = ConfigManager.languageConfig.get();
        String path = "HubPro.HubItems.";
        if (config != null) {
            try {
                Material tpBowMaterial = Material.getMaterial(config.getString(path + "TpBow.ItemStack", "BOW"));
                if (tpBowMaterial == null) {
                    tpBowMaterial = Material.BOW;
                }
                tpBow = new TpBow(MessageUtil.format(config.getString(path + "TpBow.Name")),
                        config.getStringList(path + "TpBow.Lore").stream()
                                .map(MessageUtil::format)
                                .collect(Collectors.toList()),
                        new ItemStack(tpBowMaterial));

                // -------------------------------------------------------------------------------------------------- //

                Material arrowMaterial = Material.getMaterial(config.getString(path + "TpBow.Arrow.ItemStack", "ARROW"));
                if (arrowMaterial == null) {
                    arrowMaterial = Material.ARROW;
                }
                arrow = new TpArrow(MessageUtil.format(config.getString(path + "TpBow.Arrow.Name")),
                        config.getStringList(path + "TpBow.Arrow.Lore").stream()
                                .map(MessageUtil::format)
                                .collect(Collectors.toList()),
                        new ItemStack(arrowMaterial));

                // -------------------------------------------------------------------------------------------------- //

                Material serverSelectorMaterial = Material.getMaterial(config.getString(path + "ServerSelector.ItemStack", "COMPASS"));
                if (serverSelectorMaterial == null) {
                    serverSelectorMaterial = Material.COMPASS;
                }
                serverSelector = new ServerSelector(MessageUtil.format(config.getString(path + "ServerSelector.Name")),
                        config.getStringList(path + "ServerSelector.Lore").stream()
                                .map(MessageUtil::format)
                                .collect(Collectors.toList()),
                        new ItemStack(serverSelectorMaterial));

                // -------------------------------------------------------------------------------------------------- //

                Material playerHiderMaterial = Material.getMaterial(config.getString(path + "PlayerHider.ItemStack", "LIME_DYE"));
                if (playerHiderMaterial == null) {
                    playerHiderMaterial = Material.LIME_DYE;
                }
                playerHider = new PlayerHider(MessageUtil.format(config.getString(path + "PlayerHider.Name")),
                        config.getStringList(path + "PlayerHider.Lore").stream()
                                .map(MessageUtil::format)
                                .collect(Collectors.toList()),
                        new ItemStack(playerHiderMaterial));

                // -------------------------------------------------------------------------------------------------- //

                Material playerShowerMaterial = Material.getMaterial(config.getString(path + "PlayerShower.ItemStack", "RED_DYE"));
                if (playerShowerMaterial == null) {
                    playerShowerMaterial = Material.RED_DYE;
                }
                playerShower = new PlayerHider(MessageUtil.format(config.getString(path + "PlayerShower.Name")),
                        config.getStringList(path + "PlayerShower.Lore").stream()
                                .map(MessageUtil::format)
                                .collect(Collectors.toList()),
                        new ItemStack(playerShowerMaterial));

                // -------------------------------------------------------------------------------------------------- //
            } catch (Exception e) { HubPro.getInstance().getLogger().severe("Couldn't load Hub Items config values!" + e); }

            hubItemByName.put(tpBow.getItemName(), tpBow);
            hubItemByName.put(arrow.getItemName(), arrow);
            hubItemByName.put(serverSelector.getItemName(), serverSelector);
            hubItemByName.put(playerHider.getItemName(), playerHider);
            hubItemByName.put(playerShower.getItemName(), playerShower);
        }
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
