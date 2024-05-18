// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.methods;

import at.manu.hubpro.HubPro;
import at.manu.hubpro.configuration.Config;
import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import at.manu.hubpro.utils.chatutil.MessageUtil;
import at.manu.hubpro.utils.gui.GuiHelper;
import at.manu.hubpro.utils.proxyconnection.ConnectionHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.getConfigByName;
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

    public boolean checkAndPerformServerItemAction(Player player, ItemStack clickedItem, FileConfiguration config, String sectionPath) {
        ConfigurationSection section = config.getConfigurationSection(sectionPath);
        if (section == null) return false;

        for (String key : section.getKeys(false)) {

            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            String itemName   = MessageUtil.format(itemSection.getString("ItemName"));
            List<String> lore = itemSection.getStringList("ItemLore").stream().map(MessageUtil::format).collect(Collectors.toList());
            Material material = Material.matchMaterial(Objects.requireNonNull(itemSection.getString("ItemStack")));
            String action     = itemSection.getString("Action");

            if (matchMaterialAndItem(player, clickedItem, material, itemName, lore)) {
				assert action != null;
				if (action.equalsIgnoreCase("CONNECT")) {
                    handleConnectAction(player, itemSection.getString("Server"));
                    return true;
                } else if (action.equalsIgnoreCase("OPENGUI")) {
                    handleOpenGUIAction(player, itemSection.getString("GUI"));
                    return true;
                }

            }
        }
        return false;
    }

    private void handleConnectAction(Player player, String serverName) {
        if(serverName != null) {
            ConnectionHelper.getInstance().movePlayerToOtherServer(player, serverName);
            player.sendMessage(ChatColor.YELLOW + "Redirecting you to " + serverName + "!");
            player.closeInventory();
        }
    }

    private void handleOpenGUIAction(Player player, String guiName) {
        if (guiName != null) {
            Config gui = getConfigByName(guiName);
            player.closeInventory();
            Bukkit.getScheduler().runTask(HubPro.getInstance(), ()-> openGUIFromConfig(gui, player));
        }
    }

    private boolean matchMaterialAndItem(Player p, ItemStack clickedItem, Material material, String itemName, List<String> lore) {
        return material != null && clickedItem.getType() == material && isItemSimilar(p, clickedItem, material, itemName, lore);
    }

    private boolean isItemSimilar(Player p, ItemStack clickedItem, Material material, String itemName, List<String> lore ){
        ItemStack comparisonStack = new ItemStack(material);
        ItemMeta meta = comparisonStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(PlaceholderAPI.setPlaceholders(p, itemName));
            meta.setLore       (PlaceholderAPI.setPlaceholders(p, lore));
            comparisonStack.setItemMeta(meta);
        }
        return clickedItem.isSimilar(comparisonStack);
    }

    /**
     * Sends a Bukkit title with values out of a config to the player
     *
     * @param player The player who will get sent the title.
     */
    public void sendTitle(Player player) {
        try {
            boolean enabled = ConfigManager.languageConfig.get().getBoolean("HubPro.Title.Enabled");

            String mainTitle= ConfigManager.languageConfig.get().getString("HubPro.Title.MainTitle");
            String subTitle = ConfigManager.languageConfig.get().getString("HubPro.Title.SubTitle");

            if (mainTitle == null || mainTitle.isEmpty() || subTitle == null) {
                HubPro.getInstance().getLogger().warning("MainTitle or SubTitle is not configured correctly.");
                return;
            }

            int blendIn     = ConfigManager.languageConfig.get().getInt("HubPro.Title.BlendIn", 10);
            int stay        = ConfigManager.languageConfig.get().getInt("HubPro.Title.Stay", 70);
            int blendOut    = ConfigManager.languageConfig.get().getInt("HubPro.Title.BlendOut", 20);

            if (enabled) {
                if (subTitle.contains("%player%")) { subTitle = subTitle.replace("%player%", player.getDisplayName()); }
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
     * @param hide   If the other players should be hidden or seen
     */
    public void togglePlayerVisibility(Player player, boolean hide) {
        String cooldownKey = hide ? "hide_players" : "show_players";
        String notificationCooldownKey = cooldownKey + "_notification";
        String messageKey = hide ? "HubPro.Items.PlayerHider.HideMessage" : "HubPro.Items.PlayerShower.ShowMessage";
        ItemStack newItem = hide ? HubItemInitializer.getPlayerShowerItem() : HubItemInitializer.getPlayerHiderItem();

        if (HubPro.getCooldownManager().isOnCooldown(player, cooldownKey)) {
            if (!HubPro.getCooldownManager().isOnCooldown(player, notificationCooldownKey)) {
                String spamPreventMessage = ConfigManager.languageConfig.get().getString("HubPro.Items.PlayerHider.SpamMessage");
                assert spamPreventMessage != null;
                if (spamPreventMessage.contains("%seconds%")) {
                    spamPreventMessage = spamPreventMessage.replace("%seconds%", String.valueOf(HubPro.getCooldownManager().getRemainingCooldown(player, cooldownKey)));
                }
                player.sendMessage(MessageUtil.format(spamPreventMessage));
                HubPro.getCooldownManager().setCooldown(player, notificationCooldownKey, ConfigManager.defaultConfig.get().getInt("HubPro.Items.PlayerHider.SpamCooldown"));
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
        HubPro.getCooldownManager().setCooldown(player, cooldownKey, ConfigManager.defaultConfig.get().getInt("HubPro.Items.PlayerHider.Cooldown"));
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
    public void TpBowArrowLandAnimation(@NotNull Player player, @NotNull Entity entity) {
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

    public void openGUIFromConfig(@NotNull Config config, Player player) {
        FileConfiguration fileConfig = config.get();

        String guiTitle       = fileConfig.getString("Title");
        int guiSize           = fileConfig.getInt("Size");
        boolean guiFill       = fileConfig.getBoolean("Fill_rest_with_items");
        Material fillMaterial = Material.matchMaterial(fileConfig.getString("rest_items", "GRAY_STAINED_GLASS_PANE"));

        Map<Integer, ItemStack> items = new HashMap<>();
        ConfigurationSection itemsSection = fileConfig.getConfigurationSection("items");

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    List<String> lore = itemSection.getStringList("ItemLore").stream()
                            .map(MessageUtil::format)
                            .collect(Collectors.toList());

                    Material material = Material.matchMaterial(Objects.requireNonNull(itemSection.getString("ItemStack")));
                    String itemName   = itemSection.getString("ItemName");
                    int itemPlace     = itemSection.getInt("MenuPlace");

                    if (material != null) {
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, MessageUtil.format(itemName)));
                            meta.setLore       (PlaceholderAPI.setPlaceholders(player, lore));
                            item.setItemMeta(meta);
                        }
                        items.put(itemPlace, item);
                    }
                }
            }
        }
        GuiHelper gui = new GuiHelper(guiSize, MessageUtil.format(guiTitle), items, guiFill, fillMaterial);
        player.openInventory(gui.getInventory());
    }

    public void setPlayerSpawn(Player player) {
        Config config = ConfigManager.getDefaultConfig();
        config.get().addDefault("HubPro.Spawn.World" ,player.getWorld().getName());
        config.get().addDefault("HubPro.Spawn.X"     ,player.getLocation().getX());
        config.get().addDefault("HubPro.Spawn.Y"     ,player.getLocation().getY());
        config.get().addDefault("HubPro.Spawn.Z"     ,player.getLocation().getZ());
        config.get().addDefault("HubPro.Spawn.Yaw"   ,player.getLocation().getYaw());
        config.get().addDefault("HubPro.Spawn.Pitch" ,player.getLocation().getPitch());

        config.save();
    }

    public Location getSpawnLocation() {
        Config config = ConfigManager.getDefaultConfig();

        String world  = config.get().getString("HubPro.Spawn.World");
        double x      = config.get().getDouble("HubPro.Spawn.X");
        double y      = config.get().getDouble("HubPro.Spawn.Y");
        double z      = config.get().getDouble("HubPro.Spawn.Z");
        float yaw     = (float) config.get().getDouble("HubPro.Spawn.Yaw");
        float pitch   = (float) config.get().getDouble("HubPro.Spawn.Pitch");

		assert world != null;
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}
