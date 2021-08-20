package authentication_one2one;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class UDPBuilder {
    public static DatagramChannel openChannel() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        return datagramChannel;
    }

    public static DatagramChannel bindChannel(SocketAddress local) throws IOException {
        return openChannel().bind(local);
    }
}
