// --------------------------------------------------------------------------
// -						Class created by PK     						-
// --------------------------------------------------------------------------

package at.manu.hubpro.configuration;

import java.util.HashMap;

public class ConfigType {

    private static final HashMap<String, ConfigType> ALL_TYPES = new HashMap<>();

    public static final ConfigType DEFAULT = new ConfigType("Default");
    public static final ConfigType LANGUAGE = new ConfigType("Language");
    public static final ConfigType ITEMS = new ConfigType("Items");
    public static final ConfigType MENU = new ConfigType("Menus");

    private final String string;

    public ConfigType(final String string) {
        this.string = string;
        ALL_TYPES.put(string, this);
    }

    @Override
    public String toString() {
        return this.string;
    }
}
