package com.ftinc.lolserv.data;

import dagger.Module;
import dagger.Provides;

import java.sql.Connection;

/**
 * Created by drew.heavner on 5/13/15.
 */
@Module
public class DataModule {

    @Provides
    Connection provideDBConnectionProvider(DB db){
        return db.getSafeConnection();
    }

}
