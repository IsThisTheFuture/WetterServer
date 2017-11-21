import java.net.DatagramPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataProcessor implements Runnable {
    private final static Logger logger=Logger.getLogger(DataProcessor.class.getName());

    private DatabaseAccess dbAccess = new DatabaseAccess();
    private Connection conn = dbAccess.getConnection();
    private DatagramPacket packet = null;

    public DataProcessor(DatagramPacket packet){
        super();
        this.packet = packet;
    }


    public void run() {
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);

        insertIntoDatabase(received);
    }

    private void insertIntoDatabase(String received) {
        /* Splittet bei einem oder mehreren Trennzeichen (Spaces) */
        String[] splitted = received.split("\\s+");
        String station = splitted[0];
        int unixTimestamp = Integer.parseInt(splitted[1]);
        String property = splitted[2];
        String proprtyUnit = splitted[3];
        double value = Double.parseDouble(splitted[4]);

        try {
            PreparedStatement pst =
                    conn.prepareStatement("INSERT INTO messungen (`station`,`unix_timestamp`,`property`,`property_unit`,`value`) VALUES (?,?,?,?,?)");

            pst.setString(1, station);
            pst.setInt(2, unixTimestamp);
            pst.setString(3, property);
            pst.setString(4, proprtyUnit);
            pst.setDouble(5, value);
            pst.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Messung in die Datenbank",e);
        }
    }
}