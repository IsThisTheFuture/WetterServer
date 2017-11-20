import java.net.DatagramPacket;
import java.sql.Connection;

public class DataProcessor implements Runnable {
    private  DatabaseAccess dbAccess = new DatabaseAccess();
    private Connection connection = dbAccess.getConnection();
    private DatagramPacket packet = null;

    public DataProcessor(DatagramPacket packet){
        super();
        this.packet = packet;
    }


    public void run() {
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);

    }
}
