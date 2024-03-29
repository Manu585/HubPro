package at.manu.hubpro.utils.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@Getter
public class GuiHelper {
    private Inventory inventory;

    public GuiHelper(UUID uuid, int rows, String title, Map<Integer, ItemStack> items) {
        createGUI(uuid, rows, title, items);
    }

    private void createGUI(UUID uuid, int rows, String title, Map<Integer, ItemStack> items) {
        inventory = Bukkit.createInventory(Bukkit.getPlayer(uuid), rows * 9, title);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        // Set each item at its position
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
    }
}
