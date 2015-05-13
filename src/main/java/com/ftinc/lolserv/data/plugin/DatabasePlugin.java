package com.ftinc.lolserv.data.plugin;

import com.ftinc.lolserv.data.model.LolCommit;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by drew.heavner on 5/13/15.
 */
public class DatabasePlugin implements Plugin {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
