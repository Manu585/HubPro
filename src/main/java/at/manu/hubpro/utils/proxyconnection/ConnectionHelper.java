// --------------------------------------------------------------------------
// -						Class created by Manu585						-
// --------------------------------------------------------------------------

package at.manu.hubpro.utils.proxyconnection;

import at.manu.hubpro.HubPro;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionHelper {

    private static ConnectionHelper instance;

    public static synchronized ConnectionHelper getInstance() {
        if (instance == null) {
        instance = new ConnectionHelper();
        }
        return instance;
    }

    public void movePlayerToOtherServer(Player playerToMove, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        playerToMove.sendPluginMessage(HubPro.getInstance(), "BungeeCord", out.toByteArray());
    }
}
