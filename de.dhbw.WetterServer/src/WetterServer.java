import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Wetterdaten-Server
 *
 * Für die Vorlesung "Verteilte Systeme"
 * an der DHBW Karlsruhe
 */

public class WetterServer implements Runnable {
    private static final Logger logger = Logger.getLogger(WetterServer.class.getName());

    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[256];

    public WetterServer(){
        try {
            socket = new DatagramSocket(7134);
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Konnte Socket nicht erstellen", e);
        }
    }

    public static void main(String[] arguments){
        Thread serverThread = new Thread(new WetterServer());
        serverThread.start();
    }

    @Override public void run(){
    running = true;

    while(running){
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Konnte Paket nicht empfangen...", e);
        }

        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);
    }
    }

}
