import java.net.DatagramPacket;

public class DataProcessor implements Runnable {
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
