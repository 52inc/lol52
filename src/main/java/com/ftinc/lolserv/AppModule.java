package com.ftinc.lolserv;

import com.ftinc.lolserv.data.plugin.Plugin;
import com.ftinc.lolserv.data.plugin.SlackPlugin;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

/**
 * Created by drew.heavner on 5/13/15.
 */
@Module
public class AppModule {

    @Provides @Singleton
    @Named("port")
    int provideApiPort(){
        return 8321;
    }

    @Provides @Singleton
    List<Plugin> providePlugins(OkHttpClient client,
                                Gson gson){

        return Arrays.asList(new Plugin[]{
            new SlackPlugin(client, gson)
        });
    }

}
