package at.manu.hubpro.methods;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneralMethods {

    public void sendTitle(Player p) {
        try {
            boolean enabled = ConfigManager.languageConfig.get().getBoolean("HubPro.Title.Enabled");

            String mainTitle = ConfigManager.languageConfig.get().getString("HubPro.Title.MainTitle");
            String subTitle = ConfigManager.languageConfig.get().getString("HubPro.Title.SubTitle");

            if (mainTitle == null || mainTitle.isEmpty() || subTitle == null) {
                HubPro.getInstance().getLogger().warning("MainTitle or SubTitle is not configured correctly.");
                return;
            }

            int blendIn = ConfigManager.languageConfig.get().getInt("HubPro.Title.BlendIn", 10);
            int stay = ConfigManager.languageConfig.get().getInt("HubPro.Title.Stay", 70);
            int blendOut = ConfigManager.languageConfig.get().getInt("HubPro.Title.BlendOut", 20);

            if (enabled) {
                if (subTitle.contains("%player%")) {
                    subTitle = subTitle.replace("%player%", p.getDisplayName());
                }
                p.sendTitle(mainTitle, subTitle, blendIn, stay, blendOut);
            }
        } catch (Exception e) {
            HubPro.getInstance().getLogger().severe("An error occurred while sending title: " + e.getMessage());
        }
    }

    public void insertHubItems(Player p) {
        if (p != null) {
            p.getInventory().clear();

            p.getInventory().setItem(0, HubItemInitializer.getTpBowItem());
            p.getInventory().setItem(4, HubItemInitializer.getServerSelectorItem());
            p.getInventory().setItem(8, HubItemInitializer.getPlayerHiderItem());
            p.getInventory().setItem(20, HubItemInitializer.getArrowItem());
        } else {
            HubPro.getInstance().getLogger().severe("An error occurred while giving player starter items");
        }
    }

    public static void TpBowArrowLandAnimation(Player p, Entity e) {
        Location location = e.getLocation();
        e.remove();

        Bukkit.getScheduler().scheduleSyncDelayedTask(HubPro.getInstance(), () -> {
                p.teleport(location);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 2);
            }, 20L);

        p.spawnParticle(Particle.FLAME, location, 100, 0, 0, 0, 0.1);
        p.spawnParticle(Particle.SMOKE_LARGE, location, 100, 0, 0, 0, 0.1);

        for (int degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double x = Math.cos(radians);
            double z = Math.sin(radians);

            Location particleLocation = location.clone().add(x, 0, z);

            Bukkit.getScheduler().scheduleSyncDelayedTask(HubPro.getInstance(), () -> {
                    p.spawnParticle(Particle.END_ROD, particleLocation, 1, 0, 0, 0, 0);
                    }, degree / 20L);
        }
    }

    public static void getServerItemsFromConfig(Map<Integer, ItemStack> items) {
        FileConfiguration config = ConfigManager.serverItemsConfig.get();
        ConfigurationSection serverItemsSection = config.getConfigurationSection("HubPro.ServerItems");

        if (serverItemsSection != null) {
            for (String key : serverItemsSection.getKeys(false)) {
                ConfigurationSection itemSection = serverItemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String itemName = MessageUtil.format(itemSection.getString("ItemName"));
                    Material itemMaterial = Material.getMaterial(Objects.requireNonNull(itemSection.getString("ItemStack")));
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
    }
}
