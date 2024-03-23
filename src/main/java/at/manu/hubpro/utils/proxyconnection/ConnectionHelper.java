package at.manu.hubpro.utils.proxyconnection;

import at.manu.hubpro.HubPro;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class ConnectionHelper {

    public void movePlayerToOtherServer(Player playerToMove, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        playerToMove.sendPluginMessage(HubPro.getInstance(), "BungeeCord", out.toByteArray());
    }

}
