package at.manu.hubpro.listeners;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.hubitem.HubItem;
import at.manu.hubpro.hubitem.initializer.HubItemInitializer;
import at.manu.hubpro.utils.gui.GuiHelper;
import at.manu.hubpro.utils.proxyconnection.ConnectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
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

import java.util.HashMap;
import java.util.Map;

public class HubItemListener implements Listener {

    private GuiHelper gh;

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
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null) {
                if (e.getItem().isSimilar(HubItemInitializer.getServerSelectorItem())) {
                    Player p = e.getPlayer();
                    Map<Integer, ItemStack> items = new HashMap<>();
                    items.put(13, HubItemInitializer.getServeritemItem());
                    gh = new GuiHelper(3, ChatColor.GREEN + "Server Selector", items);
                    p.openInventory(gh.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onServerItemLeftClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory() == gh.getInventory()) {
                if (e.getClick().isLeftClick()) {
                    p.sendMessage("Left clicked!");
                    assert e.getCurrentItem() != null;
                    if (e.getCurrentItem().isSimilar(HubItemInitializer.getServeritemItem())) {
                        new ConnectionHelper().movePlayerToOtherServer(p, "lobby");
                        p.sendMessage(ChatColor.YELLOW + "Its working!");
                        p.closeInventory();
                    }
                }
            }
        }
    }
}
