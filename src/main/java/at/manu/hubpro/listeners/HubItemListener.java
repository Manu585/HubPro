// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.listeners;

import at.manu.hubpro.configuration.Config;
import at.manu.hubpro.item.hubitem.HubItem;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.memoryutil.MemoryUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.findMenuConfigByTitle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HubItemListener implements Listener {

    @Getter
    private static final HubItemListener instance = new HubItemListener();


    // TP-BOW LISTENER
    @EventHandler
    public void onArrowLand(ProjectileHitEvent e) {
        if (e.getEntity().getType() == EntityType.ARROW) {
            if (e.getEntity().getShooter() instanceof Player p) {
				ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
                if (itemInMainHand.isSimilar(HubItemInitializer.getTpBowItem())) {
                    GeneralMethods.getInstance().TpBowArrowLandAnimation(p, e.getEntity());  // PLAYS ANIMATION AND REMOVES ARROW
                }
            }
        }
    }


    @EventHandler
    public void onRightClickWithHubItem(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            if (e.getItem().isSimilar(HubItemInitializer.getServerSelectorItem())) {
                HubItem server_selector = HubItemInitializer.getServerSelector();
                String action = server_selector.getAction();
                String menu = server_selector.getMenu();
                if (action != null && action.equalsIgnoreCase("OPEN_MENU")) {
                    Config config = MemoryUtil.menusConfigs.get(menu);
                    if (config != null) {
                        GeneralMethods.getInstance().openGUIFromConfig(config, e.getPlayer());
                    }
                }
            } else if (e.getItem().isSimilar(HubItemInitializer.getPlayerHiderItem())) {
                GeneralMethods.getInstance().togglePlayerVisibility(e.getPlayer(), true);  // Hide players
            } else if (e.getItem().isSimilar(HubItemInitializer.getPlayerShowerItem())) {
                GeneralMethods.getInstance().togglePlayerVisibility(e.getPlayer(), false); // Show players
            }
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

		ItemStack clickedItem = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();

        if (clickedItem == null || !clickedItem.hasItemMeta() || inventory == null) return;

        String inventoryTitle = event.getView().getTitle();
        Config menuConfig = findMenuConfigByTitle(inventoryTitle);

        if (menuConfig == null) return;

        event.setCancelled(true);

        boolean actionPerformed = GeneralMethods.getInstance().checkAndPerformServerItemAction(player, clickedItem, menuConfig.get(), "items");
        if (actionPerformed) {
            player.closeInventory();
        }
    }
}
