// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.utils.chatutil;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.utils.permission.PermissionUtils;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    private static final Pattern hexPattern = Pattern.compile("(&#)([a-fA-F0-9]{6})");

    public static String format(String msg) {
        Matcher hexMatcher = hexPattern.matcher(msg);
        while (hexMatcher.find()) {
            String color = hexMatcher.group(2);
            String hexColor = ChatColor.of("#" + color).toString();
            msg = msg.replace(hexMatcher.group(0), hexColor);
        }
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg;
    }

    public static String getPrefix() {
        return ConfigManager.languageConfig.get().getString(MessageUtil.format("HubPro.Chat.Prefix"));
    }

    // --SERVER SIDE MESSAGES--
    public static @NotNull String serverStartMessage() {
        return format("&2Starting " + getPrefix() + " v0.0.7!");
    }

    public static @NotNull String serverStopMessage() {
        return format("&2Stopping " + getPrefix() + " v0.0.7!");
    }


    // --CLIENT SIDE MESSAGES--
    public static String noPermissionMessage(PermissionUtils pc) {
        StringBuilder missingPermissions = new StringBuilder();

        for (Map.Entry<String, Boolean> entry : pc.getPermissionsMap().entrySet()) {
            if (!entry.getValue()) {
                missingPermissions.append(entry.getKey()).append(" or ");
            }
        }

        if (!missingPermissions.isEmpty()) {
            missingPermissions.setLength(missingPermissions.length() - 4);
        }

        return getPrefix() + format(" &cInsufficient permissions! Missing permissions: " + missingPermissions);
    }
}
