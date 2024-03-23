package at.manu.hubpro.listeners;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.gui.GuiHelper;
import at.manu.hubpro.utils.proxyconnection.ConnectionHelper;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HubItemListener implements Listener {

    private final Map<UUID, GuiHelper> playerGuis = new HashMap<>();

    @EventHandler
    public void onArrowLand(ProjectileHitEvent e) {
        if (e.getEntity().getType() == EntityType.ARROW) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player p = (Player) e.getEntity().getShooter();
                ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
                if (itemInMainHand.isSimilar(HubItemInitializer.getTpBowItem())) {
                    Location location = e.getEntity().getLocation();

                    // Teleport player after a short delay to show particles
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HubPro.getInstance(), () -> {
                        p.teleport(location);
                        e.getEntity().remove();
                    }, 20L);

                    // Burst particle effect
                    p.spawnParticle(Particle.FLAME, location, 100, 0, 0, 0, 0.1);
                    p.spawnParticle(Particle.SMOKE_LARGE, location, 100, 0, 0, 0, 0.1);

                    // Circular particle effect
                    for (int degree = 0; degree < 360; degree++) {
                        double radians = Math.toRadians(degree);
                        double x = Math.cos(radians);
                        double z = Math.sin(radians);

                        Location particleLocation = location.clone().add(x, 0, z);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(HubPro.getInstance(), () -> {
                            p.spawnParticle(Particle.END_ROD, particleLocation, 1, 0, 0, 0, 0);
                        }, degree / 20L); // Time until particle spawns
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRightClickWithHubItem(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem() != null) {
            if (e.getItem().isSimilar(HubItemInitializer.getServerSelectorItem())) {
                Player p = e.getPlayer();
                Map<Integer, ItemStack> items = new HashMap<>();

                FileConfiguration config = ConfigManager.serverItemsConfig.get();
                ConfigurationSection serverItemsSection = config.getConfigurationSection("HubPro.ServerItems");

                if (serverItemsSection != null) {
                    for (String key : serverItemsSection.getKeys(false)) {
                        ConfigurationSection itemSection = serverItemsSection.getConfigurationSection(key);
                        if (itemSection != null) {
                            String itemName = MessageUtil.format(itemSection.getString("ItemName"));
                            Material itemMaterial = Material.getMaterial(itemSection.getString("ItemStack"));
                            int menuPlace = itemSection.getInt("menuplace");
                            if (itemMaterial != null && menuPlace >= 0) {
                                ItemStack itemStack = new ItemStack(itemMaterial);
                                ItemMeta meta = itemStack.getItemMeta();
                                if (meta != null) {
                                    meta.setDisplayName(itemName);
                                    List<String> itemLore = ConfigManager.serverItemsConfig.get().getStringList(key + ".Lore");
                                    meta.setLore(itemLore);
                                    itemStack.setItemMeta(meta);
                                }
                                items.put(menuPlace, itemStack);
                            }
                        }
                    }
                }

                GuiHelper gh = new GuiHelper(3, ChatColor.GREEN + "Server Selector", items);
                playerGuis.put(p.getUniqueId(), gh);
                p.openInventory(gh.getInventory());
            }
        }
    }

    @EventHandler
    public void onServerItemLeftClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();

        GuiHelper gh = playerGuis.get(player.getUniqueId());
        if (gh == null || !e.getInventory().equals(gh.getInventory())) {
            return;
        }

        if (e.getClick().isLeftClick()) {
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            FileConfiguration config = ConfigManager.serverItemsConfig.get();
            String basePath = "HubPro.ServerItems";

            for (String key : config.getConfigurationSection(basePath).getKeys(false)) {
                String path = basePath + "." + key;
                String configItemName = MessageUtil.format(config.getString(path + ".ItemName"));
                Material configItemStack = Material.matchMaterial(config.getString(path + ".ItemStack"));

                if (configItemStack == null) {
                    continue;
                }

                ItemStack comparisonStack = new ItemStack(configItemStack);
                ItemMeta meta = comparisonStack.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(configItemName);
                    comparisonStack.setItemMeta(meta);
                }

                if (clickedItem.isSimilar(comparisonStack)) {
                    String server = config.getString(path + ".Server");
                    if (server != null) {
                        new ConnectionHelper().movePlayerToOtherServer(player, server);
                        player.sendMessage(ChatColor.YELLOW + "Redirecting you to " + key + "!");
                        player.closeInventory();
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            playerGuis.remove(e.getPlayer().getUniqueId());
        }
    }
}
