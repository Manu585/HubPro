package at.manu.hubpro.listeners;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.gui.GuiHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HubItemListener implements Listener {

    @Getter
    private static final HubItemListener instance = new HubItemListener();
    private final String basePath = "HubPro.ServerItems";
    private GuiHelper gh;


	// TP-BOW LISTENER
    @EventHandler
    public void onArrowLand(ProjectileHitEvent e) {
        if (e.getEntity().getType() == EntityType.ARROW) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player p = (Player) e.getEntity().getShooter();
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
                Map<Integer, ItemStack> items = GeneralMethods.getInstance().generateItemStacksFromConfig(ConfigManager.serverItemsConfig.get(), basePath);
                gh = new GuiHelper(e.getPlayer().getUniqueId(), 3, MessageUtil.format(ConfigManager.languageConfig.get().getString("HubPro.HubItems.ServerSelector.Name")), items);
                e.getPlayer().openInventory(gh.getInventory());
            } else if (e.getItem().isSimilar(HubItemInitializer.getPlayerHiderItem())) {
                GeneralMethods.getInstance().togglePlayerVisibility(e.getPlayer(), true);  // Hide players
            }
            else if (e.getItem().isSimilar(HubItemInitializer.getPlayerShowerItem())) {
                GeneralMethods.getInstance().togglePlayerVisibility(e.getPlayer(), false); // Show players
            }
        }
    }

    @EventHandler
    public void onServerItemLeftClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if (gh == null || !e.getInventory().equals(gh.getInventory())) return;

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        boolean actionTaken = GeneralMethods.getInstance().checkAndPerformServerItemAction(player, clickedItem, ConfigManager.serverItemsConfig.get(), basePath);
        if (actionTaken) {
            e.setCancelled(true);
        }
    }
}
