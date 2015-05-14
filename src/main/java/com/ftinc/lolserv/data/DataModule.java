package com.ftinc.lolserv.data;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
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

    @Provides @Singleton
    Gson provideGson(){
        return new Gson();
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient(){
        return new OkHttpClient();
    }

}
