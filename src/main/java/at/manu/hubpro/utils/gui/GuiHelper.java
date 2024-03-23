package at.manu.hubpro.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GuiHelper {

    private Inventory inventory;

    public GuiHelper(int rows, String title, Map<Integer, ItemStack> items) {
        createGUI(rows, title, items);
    }

    private void createGUI(int rows, String title, Map<Integer, ItemStack> items) {
        inventory = Bukkit.createInventory(null, rows * 9, title);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        // Set each item at its position
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

}
