package at.manu.hubpro.utils.chatutil;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.utils.permission.PermissionUtils;
import net.md_5.bungee.api.ChatColor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String format(String msg) {
        Matcher match = pattern.matcher(msg);
        while (match.find()) {
            String color = msg.substring(match.start(), match.end());
            msg = msg.replace(color, ChatColor.of(color) + "");
            match = pattern.matcher(msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getPrefix() {
        return ConfigManager.languageConfig.get().getString("HubPro.Chat.Prefix");
    }

    // --SERVER SIDE MESSAGES--
    public static String serverStartMessage() {
        return format("&2Starting " + getPrefix() + " v1.0.0 !");
    }

    public static String serverStopMessage() {
        return format("&2Stopping " + getPrefix() + " v1.0.0 !");
    }


    // --CLIENT SIDE MESSAGES--
    public static String noPermissionMessage(PermissionUtils pc) {
        StringBuilder missingPermissions = new StringBuilder();

        for (Map.Entry<String, Boolean> entry : pc.getPermissionsMap().entrySet()) {
            if (!entry.getValue()) {
                missingPermissions.append(entry.getKey()).append(" or ");
            }
        }

        if (missingPermissions.length() > 0) {
            missingPermissions.setLength(missingPermissions.length() - 4);
        }

        return getPrefix() + format(" &cInsufficient permissions! Missing permissions: " + missingPermissions);
    }
}
