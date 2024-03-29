package at.manu.hubpro.methods;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.proxyconnection.ConnectionHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.hidePlayers;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralMethods {

    private static GeneralMethods instance;

    public static synchronized GeneralMethods getInstance() {
        if (instance == null) {
            instance = new GeneralMethods();
        }
        return instance;
    }

    /**
     * Generates a map from item positions to item stacks based on a configuration section.
     *
     * @param config The configuration from which the items are loaded.
     * @param sectionPath The path to the section in the configuration that contains the items.
     * @return A map from Integer (inventory slot) to ItemStacks.
     */
    public Map<Integer, ItemStack> generateItemStacksFromConfig(FileConfiguration config, String sectionPath) {
        Map<Integer, ItemStack> items = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection(sectionPath);

        if (section == null) {
            return items;
        }

        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection != null) {
                Material material = Material.getMaterial(Objects.requireNonNull(itemSection.getString("ItemStack")));
                String name = MessageUtil.format(itemSection.getString("ItemName"));
                List<String> lore = itemSection.getStringList("Lore").stream().map(MessageUtil::format).collect(Collectors.toList());
                int slot = itemSection.getInt("menuplace", -1);

                if (material != null && slot >= 0) {
                    ItemStack item = new ItemStack(material);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName(name);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                    }
                    items.put(slot, item);
                }
            } else {
                System.out.println("Section is null. Check path: " + sectionPath);
            }
        }
        return items;
    }



    /**
     * Checks if a clicked ItemStack matches a server item defined in the configuration and performs an action if a match is found.
     *
     * @param player The player performing the action.
     * @param clickedItem The ItemStack that was clicked on.
     * @param config The FileConfiguration from which the server items are read.
     * @param sectionPath The path within the configuration where the server items are defined.
     * @return true if an action was performed, otherwise false.

     * This method iterates through all entries under the specified configuration path. For each entry, it attempts to match
     * the item defined in the configuration with the clicked item. If the material matches, it also checks for a match on the display name and lore.
     * If these match, the defined action is performed (e.g., redirecting the player to another server). This method can be used to implement a clean and reusable logic
     * for handling custom items in menus or similar GUIs, streamlining the interaction process for players on a Minecraft server.
     */
    public boolean checkAndPerformServerItemAction(Player player, ItemStack clickedItem, FileConfiguration config, String sectionPath) {
        ConfigurationSection section = config.getConfigurationSection(sectionPath);
        if (section == null) {
            return false;
        }

        for (String key : section.getKeys(false)) {
            String itemName = MessageUtil.format(config.getString(sectionPath + "." + key + ".ItemName"));
            List<String> lore = config.getStringList(sectionPath + "." + key + ".Lore").stream().map(MessageUtil::format).collect(Collectors.toList());
            Material material = Material.matchMaterial(Objects.requireNonNull(config.getString(sectionPath + "." + key + ".ItemStack")));
            String serverName = config.getString(sectionPath + "." + key + ".Server");

            if (material != null && clickedItem.getType() == material) {
                ItemStack comparisonStack = new ItemStack(material);
                ItemMeta meta = comparisonStack.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(itemName);
                    meta.setLore(lore);
                    comparisonStack.setItemMeta(meta);
                }

                if (clickedItem.isSimilar(comparisonStack)) {
                    if (serverName != null) {
                        new ConnectionHelper().movePlayerToOtherServer(player, serverName);
                        player.sendMessage(ChatColor.YELLOW + "Redirecting you to " + key + "!");
                        player.closeInventory();
                        return true;
                    }
                }
            }
        }
        return false;
    }



    /**
     * Sends a Bukkit title with values out of a config to the player
     *
     * @param player The player who will get sent the title.
     */
    public void sendTitle(Player player) {
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
                    subTitle = subTitle.replace("%player%", player.getDisplayName());
                }
                player.sendTitle(MessageUtil.format(mainTitle), MessageUtil.format(subTitle), blendIn, stay, blendOut);
            }
        } catch (Exception e) {
            HubPro.getInstance().getLogger().severe("An error occurred while sending title: " + e.getMessage());
        }
    }



    /**
     * Hides other players from the player.
     *
     * @param player The player who will be affected by hiding others
     * @param hide If the other players should be hidden or seen
     */
    public void togglePlayerVisibility(Player player, boolean hide) {
        String cooldownKey = hide ? "hide_players" : "show_players";
        String notificationCooldownKey = cooldownKey + "_notification";
        String messageKey = hide ? "HubPro.HubItems.PlayerHider.HideMessage" : "HubPro.HubItems.PlayerShower.ShowMessage";
        ItemStack newItem = hide ? HubItemInitializer.getPlayerShowerItem() : HubItemInitializer.getPlayerHiderItem();

        if (HubPro.getCooldownManager().isOnCooldown(player, cooldownKey)) {
            if (!HubPro.getCooldownManager().isOnCooldown(player, notificationCooldownKey)) {
                String spamPreventMessage = ConfigManager.languageConfig.get().getString("HubPro.HubItems.PlayerHider.SpamMessage");
				assert spamPreventMessage != null;
				if (spamPreventMessage.contains("%seconds%")) {
                    spamPreventMessage = spamPreventMessage.replace("%seconds%", String.valueOf(HubPro.getCooldownManager().getRemainingCooldown(player, cooldownKey)));
                }
                player.sendMessage(MessageUtil.format(spamPreventMessage));
                HubPro.getCooldownManager().setCooldown(player, notificationCooldownKey, ConfigManager.languageConfig.get().getInt("HubPro.HubItems.PlayerHider.spamCooldown")); // Prevent spamming the message
            }
            return;
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (hide && !target.hasPermission("hubpro.hider.bypass")) {
                player.hidePlayer(HubPro.getInstance(), target);
            } else {
                player.showPlayer(HubPro.getInstance(), target);
            }
        }

        String feedbackMessage = ConfigManager.languageConfig.get().getString(messageKey);
        if (feedbackMessage != null) {
            player.sendMessage(MessageUtil.format(feedbackMessage));
        }

        Bukkit.getScheduler().runTaskLater(HubPro.getInstance(), () -> player.getInventory().setItem(8, newItem), 1L);

        if (hide) {
            hidePlayers.add(player);
        } else {
            hidePlayers.remove(player);
        }
        HubPro.getCooldownManager().setCooldown(player, cooldownKey, 5);
    }



    /**
     * Gives the player the starter items upon joining the server.
     *
     * @param player The player to give the items to.
     */
    public void insertHubItems(Player player) {
        if (player != null) {
            player.getInventory().clear();

            player.getInventory().setItem(0, HubItemInitializer.getTpBowItem());
            player.getInventory().setItem(4, HubItemInitializer.getServerSelectorItem());
            player.getInventory().setItem(8, HubItemInitializer.getPlayerHiderItem());
            player.getInventory().setItem(22, HubItemInitializer.getArrowItem());
        } else {
            HubPro.getInstance().getLogger().severe("An error occurred while giving player starter items");
        }
    }



    /**
     * Plays an animation to the client and also teleporting the player
     * upon shooting with a bow.
     *
     * @param player The player to teleport and client to play the particles.
     * @param entity The arrow.
     */
    public void TpBowArrowLandAnimation(Player player, Entity entity) {
        Location location = entity.getLocation();
        double x_ = location.getX();
        double y_ = location.getY();
        double z_ = location.getZ();
        entity.remove();

        Bukkit.getScheduler().scheduleSyncDelayedTask(HubPro.getInstance(), () -> {
                player.teleport(new Location(player.getWorld(), x_, y_, z_, player.getLocation().getYaw(), player.getLocation().getPitch()));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 2);
            }, 20L);

        player.spawnParticle(Particle.FLAME, location, 100, 0, 0, 0, 0.1);
        player.spawnParticle(Particle.SMOKE_LARGE, location, 100, 0, 0, 0, 0.1);

        for (int degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double x = Math.cos(radians);
            double z = Math.sin(radians);

            Location particleLocation = location.clone().add(x, 0, z);

            Bukkit.getScheduler().scheduleSyncDelayedTask(HubPro.getInstance(), () -> player.spawnParticle(Particle.END_ROD, particleLocation, 1, 0, 0, 0, 0), degree / 20L);
        }
    }
}
