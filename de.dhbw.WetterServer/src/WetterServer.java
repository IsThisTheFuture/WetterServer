import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wetterdaten-Server
 *
 * Für die Vorlesung "Verteilte Systeme"
 * an der DHBW Karlsruhe
 */

public class WetterServer {
    private static final Logger logger = Logger.getLogger(WetterServer.class.getName());

    private static DatagramSocket socket = null;
    private static final int portNumber = 7134;
    private static ExecutorService threadPool = null;


    public static void main(String[] arguments){
        threadPool = Executors.newFixedThreadPool(10);

        try {
            socket = new DatagramSocket(portNumber);
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Konnte Socket nicht erstellen", e);
        }

        /* Add ShutdownHook to close Socket and ThreadPool when process is terminated */
        Runtime.getRuntime().addShutdownHook(new ShutDownHook());

        logger.info("Listening for Input on Port " + portNumber + "...");
        while(true){
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                /* Empfangene Pakete werden dem ThreadPool übergeben */
                socket.receive(packet);
                DataProcessor task = new DataProcessor(packet);
                threadPool.execute(task);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Konnte Paket nicht empfangen...", e);
            }
        }
    }


    private static class ShutDownHook extends Thread{
        @Override
        public void run(){
            logger.info("Server shutting down...");
            threadPool.shutdown();
            socket.close();

            logger.info("Server shutdown complete");
        }
    }
}