package at.manu.hubpro.utils.permission;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static at.manu.hubpro.utils.memoryutil.MemoryUtil.permissionsMap;

public class PermissionUtils {
    public PermissionUtils add(String permission, boolean hasPermission) {
        permissionsMap.put(permission, hasPermission);
        return this;
    }

    public boolean check() {
        return permissionsMap.containsValue(true);
    }

    public Map<String, Boolean> getPermissionsMap() {
        return permissionsMap;
    }

    public static PermissionUtils breakPermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.build.break", p.hasPermission("hubpro.build.break"))
                .add("hubpro.build.*", p.hasPermission("hubpro.build.*"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
    }

    public static PermissionUtils placePermission(Player p) {
        return new PermissionUtils()
                .add("hubpro.build.place", p.hasPermission("hubpro.build.place"))
                .add("hubpro.build.*", p.hasPermission("hubpro.build.*"))
                .add("hubpro.*", p.hasPermission("hubpro.*"));
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
}
