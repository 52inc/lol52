package com.ftinc.lolserv;

import com.ftinc.lolserv.data.Config;
import com.ftinc.lolserv.data.plugin.DatabasePlugin;
import com.ftinc.lolserv.data.plugin.ImageStoragePlugin;
import com.ftinc.lolserv.data.plugin.Plugin;
import com.ftinc.lolserv.data.plugin.SlackPlugin;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by drew.heavner on 5/13/15.
 */
@Module
public class AppModule {

    private Config mConfig;

    /**
     * Constructor
     * @param cfg
     */
    public AppModule(Config cfg){
        mConfig = cfg;
    }

    @Provides @Singleton
    Config provideConfiguration(){
        return mConfig;
    }

    @Provides @Singleton
    @Named("port")
    int provideApiPort(){
        return mConfig.server.port;
    }

    @Provides @Singleton
    @Named("image_base_url")
    String provideImageBaseUrl(){
        return mConfig.server.baseUrl.concat("/images");
    }

    @Provides @Singleton
    List<Plugin> providePlugins(OkHttpClient client,
                                Gson gson,
                                Provider<Connection> dbProvider,
                                @Named("image_base_url") String baseUrl){

        return Arrays.asList(new Plugin[]{
                new ImageStoragePlugin(baseUrl),
                new SlackPlugin(client, gson),
                new DatabasePlugin(dbProvider)
        });
    }

}
