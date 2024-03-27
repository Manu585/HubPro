package at.manu.hubpro.listeners;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.methods.GeneralMethods;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.gui.GuiHelper;
import at.manu.hubpro.utils.proxyconnection.ConnectionHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
                    GeneralMethods.TpBowArrowLandAnimation(p, e.getEntity());  // PLAYS ANIMATION AND REMOVES ARROW
                }
            }
        }
    }

    // SERVER SELECTOR LISTENER
    @EventHandler
    public void onRightClickWithHubItem(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            if (e.getItem().isSimilar(HubItemInitializer.getServerSelectorItem())) {
                Map<Integer, ItemStack> items = new HashMap<>();
                FileConfiguration config = ConfigManager.serverItemsConfig.get();
                ConfigurationSection serverItemsSection = config.getConfigurationSection(basePath);
                if (serverItemsSection != null) {
                    for (String key : serverItemsSection.getKeys(false)) {
                        ConfigurationSection itemSection = serverItemsSection.getConfigurationSection(key);
                        if (itemSection != null) {
                            Material itemMaterial = Material.getMaterial(Objects.requireNonNull(itemSection.getString("ItemStack")));
                            String itemName       = MessageUtil.format(itemSection.getString("ItemName"));
                            List<String> lore     = itemSection.getStringList("Lore"); lore.replaceAll(MessageUtil::format);
                            int menuPlace         = itemSection.getInt("menuplace");

                            if (itemMaterial != null && menuPlace >= 0) {
                                ItemStack itemStack = new ItemStack(itemMaterial);
                                ItemMeta meta = itemStack.getItemMeta();
                                if (meta != null) {
                                    meta.setDisplayName(itemName);
                                    meta.setLore(lore);
                                    itemStack.setItemMeta(meta);
                                }
                                items.put(menuPlace, itemStack);
                            }
                        }
                    }
                }
                gh = new GuiHelper(e.getPlayer().getUniqueId(), 3, ChatColor.GREEN + "Server Selector", items);
                e.getPlayer().openInventory(gh.getInventory());
            } else if (e.getItem().isSimilar(HubItemInitializer.getPlayerHiderItem())) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    e.getPlayer().hidePlayer(HubPro.getInstance(), online);
                }
                HubPro.hidePlayers.add(e.getPlayer());
                e.getItem().setType(HubItemInitializer.getPlayerShowerItem().getType());
            } else if (e.getItem().isSimilar(HubItemInitializer.getPlayerShowerItem())) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    e.getPlayer().showPlayer(HubPro.getInstance(), online);
                }
                HubPro.hidePlayers.remove(e.getPlayer());
                e.getItem().setType(HubItemInitializer.getPlayerHiderItem().getType());
            }
        }
    }

    @EventHandler
    public void onServerItemLeftClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (gh == null || !e.getInventory().equals(gh.getInventory())) return;

        if (e.getClick().isLeftClick()) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
            FileConfiguration config = ConfigManager.serverItemsConfig.get();

            for (String key : Objects.requireNonNull(config.getConfigurationSection(basePath)).getKeys(false)) {
                String configItemName    = MessageUtil.format(config.getString(basePath + "." + key + ".ItemName"));
                Material configItemStack = Material.matchMaterial(Objects.requireNonNull(config.getString(basePath + "." + key + ".ItemStack")));
                List<String> lore        = config.getStringList(basePath + "." + key + ".Lore");
                lore.replaceAll(MessageUtil::format);

				assert configItemStack != null;
				ItemStack comparisonStack = new ItemStack(configItemStack);
                ItemMeta meta = comparisonStack.getItemMeta();

                if (meta != null) { meta.setDisplayName(configItemName); meta.setLore(lore); comparisonStack.setItemMeta(meta); } // Necessary for comparison checks

                if (e.getCurrentItem().isSimilar(comparisonStack)) {
                    String server = config.getString(basePath + "." + key + ".Server");
                    if (server != null) {
                        Player player = (Player) e.getWhoClicked();
                        new ConnectionHelper().movePlayerToOtherServer(player, server);
                        player.sendMessage(ChatColor.YELLOW + "Redirecting you to " + key + "!");
                        player.closeInventory();
                        return;
                    }
                }
            }
        }
    }

}
