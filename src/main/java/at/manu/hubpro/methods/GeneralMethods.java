package at.manu.hubpro.methods;

import at.manu.hubpro.configuration.ConfigManager;
import at.manu.hubpro.item.initializer.HubItemInitializer;
import org.bukkit.entity.Player;

public class GeneralMethods {

    public void sendTitle(Player p) {
        // GET CONFIG VALUES
        boolean enabled = ConfigManager.languageConfig.get().getBoolean("HubPro.Title.Enabled");

        String mainTitle = ConfigManager.languageConfig.get().getString("HubPro.Title.MainTitle");
        String subTitle = ConfigManager.languageConfig.get().getString("HubPro.Title.SubTitle");

        int blendIn = ConfigManager.languageConfig.get().getInt("HubPro.Title.BlendIn");
        int stay = ConfigManager.languageConfig.get().getInt("HubPro.Title.Stay");
        int blendOut = ConfigManager.languageConfig.get().getInt("HubPro.Title.BlendOut");

        // SEND TITLE
        assert subTitle != null;
        if (subTitle.contains("%player%")) {
            subTitle = subTitle.replace("%player%", p.getDisplayName());
        }

        p.sendTitle(mainTitle, subTitle, blendIn, stay, blendOut);
    }

    public void insertHubItems(Player p) {
        p.getInventory().clear();

        p.getInventory().setItem(0, HubItemInitializer.getTpBowItem());
        p.getInventory().setItem(4, HubItemInitializer.getServerSelectorItem());
    }
}
