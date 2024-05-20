// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.utils.gui;

import at.manu.hubpro.utils.chatutil.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

@Getter
public class GuiHelper {
    private final Inventory inventory;

    public GuiHelper(int rows, String title, Map<Integer, ItemStack> items, boolean fillWithGlassPane, Material fillMaterial, String fillItemName) {
        inventory = Bukkit.createInventory(null, rows * 9, title);

        ItemStack fillItemStack = new ItemStack(fillMaterial != null ? fillMaterial : Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillItemMeta = fillItemStack.getItemMeta();

		assert fillItemMeta != null;
        fillItemMeta.setDisplayName(MessageUtil.format(fillItemName));
        fillItemStack.setItemMeta(fillItemMeta);
        if (fillWithGlassPane) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (!items.containsKey(i)) {
					inventory.setItem(i, fillItemStack);
                }
            }
        }
        items.forEach(inventory::setItem);
    }
}
