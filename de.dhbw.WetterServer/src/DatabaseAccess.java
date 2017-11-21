import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseAccess {
    private static final Logger logger = Logger.getLogger(WetterServer.class.getName());

    private static ComboPooledDataSource connectionPool = null;
    private static int maxPoolSize = 30;
    private static int minPoolSize = 10;

    public static Connection connection = null;

    private static final String url = "jdbc:mysql://localhost/wetterdaten";
    private static final String user = "wetterAdmin";
    private static final String password = "test";

    public DatabaseAccess() {
        if (connectionPool == null)
            createPool();
    }

    public static Connection getConnection() {
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not get Connection", e);
        }
        return connection;
    }

    public static void createPool(){
        try {
            connectionPool = new ComboPooledDataSource();
            connectionPool.setDriverClass("com.mysql.jdbc.Driver");
            connectionPool.setJdbcUrl(url);
            connectionPool.setUser(user);
            connectionPool.setPassword(password);

            connectionPool.setMaxPoolSize(maxPoolSize);
            connectionPool.setMinPoolSize(minPoolSize);
            logger.info("Created Connection Pool");
        } catch (PropertyVetoException e) {
            logger.log(Level.SEVERE, "Could not Connect to DB", e);
        }
    }

    public static void closeConnectionPool() {
        connectionPool.close();
    }
}
