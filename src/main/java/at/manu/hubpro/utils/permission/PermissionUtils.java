// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.utils.permission;

import org.bukkit.entity.Player;

import java.util.Map;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.permissionsMap;

public class PermissionUtils {
	public PermissionUtils add(String permission, boolean hasPermission) {
        permissionsMap.put(permission, hasPermission);
        return this;
    }

    public boolean check() {
        return !permissionsMap.containsValue(true);
    }

    public Map<String, Boolean> getPermissionsMap() {
        return permissionsMap;
    }

    public static PermissionUtils dropPermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.drop", p.hasPermission("hubpro.drop"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }

    public static PermissionUtils pickupPermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.pickup", p.hasPermission("hubpro.pickup"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }

    public static PermissionUtils inventoryClickPermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.invclick", p.hasPermission("hubpro.invclick"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }

    public static PermissionUtils entityHurtPermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.damage", p.hasPermission("hubpro.damage"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }

    public static PermissionUtils blockPlacePermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.blockplace", p.hasPermission("hubpro.blockplace"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }

    public static PermissionUtils blockBreakPermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.blockbreak", p.hasPermission("hubpro.blockbreak"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }
}
