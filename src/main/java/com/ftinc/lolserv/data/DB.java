package com.ftinc.lolserv.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The database helper class that provides connections to the underlying database that
 * powers this applications storage layer using JDBC
 *
 */
@Singleton
public class DB {
    private final Logger LOG = LoggerFactory.getLogger(DB.class);

    /************************************************************
     *
     * Constants
     *
     */

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String SQL_CONNECTION_URL = "jdbc:mysql://%s/%s?autoReconnect=true";

    /************************************************************
     *
     * Variables
     *
     */

    /* This is the pooled data source from which all connections are aquired */
    private ComboPooledDataSource mDataSource;

    @Inject
    Config mConfig;

    /**
     * Constructor
     */
    @Inject
    public DB(){}


    /************************************************************
     *
     * Helper Methods
     *
     */

    private String getSqlConnectionUrl(Config cfg){
        return String.format(SQL_CONNECTION_URL, cfg.database.address, cfg.database.name);
    }


    /**
     * Initialize the Database helper class
     */
    public void init(){

        // Attempt a force load of the driver
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        LOG.info("Initializing the Database...");

        mDataSource = new ComboPooledDataSource();
        try {
            mDataSource.setDriverClass(DRIVER_CLASS);
            mDataSource.setJdbcUrl(getSqlConnectionUrl(mConfig));
            mDataSource.setUser(mConfig.database.username);
            mDataSource.setPassword(mConfig.database.password);

            mDataSource.setAcquireIncrement(5);
            mDataSource.setInitialPoolSize(10);
            mDataSource.setMinPoolSize(10);
            mDataSource.setMaxPoolSize(50);
            mDataSource.setMaxStatements(10000);
            mDataSource.setMaxStatementsPerConnection(100);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        // This is to initially load connections into the pooled resource so the first request isn't
        // extra slow.
        try (Connection connection = getConnection()){
            LOG.info("Database Connection established!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shutdown the DB Helper
     */
    public void shutdown(){
        mDataSource.close();
    }

    /**
     * Get a new connection from the Pooled DataSource
     * for the defined sql server in use.
     *
     * @CAVEAT please explicitly call the .close() method on the Connection to
     *         return it to the pool immediately for another incoming connection.
     *         or call this in a try-with-resources block so it auto-closes
     *
     * @return                  a new connection to the database, or null.
     * @throws SQLException     an error occurs attempting to establish a new connection
     */
    public Connection getConnection() throws SQLException {
        if(mDataSource != null){
            return mDataSource.getConnection();
        }
        return null;
    }

    /**
     * Get a new connection from the Pooled DataSource
     * for the defined sql server in use.
     *
     * @CAVEAT please explicitly call the .close() method on the Connection to
     *         return it to the pool immediately for another incoming connection.
     *         or call this in a try-with-resources block so it auto-closes
     *
     * @return                  a new connection to the database, or null.
     * @throws SQLException     an error occurs attempting to establish a new connection
     */
    public Connection getSafeConnection(){
        if(mDataSource != null){
            try {
                return mDataSource.getConnection();
            } catch (SQLException e) {
                LOG.error("Error getting database connection", e);
            }
        }
        return null;
    }

}
