package com.ftinc.lolserv.api;

import com.ftinc.lolserv.api.main.MainInterface;
import com.ftinc.lolserv.api.webhook.WebhookInterface;
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
public class ApiModule {

    @Provides @Singleton
    List<APIInterface> provideApiInterfaces(){
        return Arrays.asList(new APIInterface[]{
                new WebhookInterface(),
                new MainInterface()
        });
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
