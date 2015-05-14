package com.ftinc.lolserv.data.plugin;

import com.ftinc.lolserv.data.model.LolCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by drew.heavner on 5/13/15.
 */
public class DatabasePlugin implements Plugin {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private Provider<Connection> mConnectionFactory;

    /**
     * Constructor
     * @param dbProvider
     */
    public DatabasePlugin(Provider<Connection> dbProvider){
        mConnectionFactory = dbProvider;
    }

    /***********************************************************************************************
     *
     * Plugin Methods
     *
     */

    @Override
    public void onLolCommit(LolCommit commit) {

        try(Connection connection = mConnectionFactory.get()){

            commit.save(connection);

            LOG.info("Database Plugin processed lolcommit: {}", commit.commitHash);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
