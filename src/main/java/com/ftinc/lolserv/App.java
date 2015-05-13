package com.ftinc.lolserv;

import com.ftinc.lolserv.api.ApiModule;
import com.ftinc.lolserv.data.DataModule;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by r0adkll on 5/12/15.
 */
public class App{

    @Singleton
    @Component(modules = {
            AppModule.class,
            ApiModule.class,
            DataModule.class
    })
    public interface AppComponent{
        AppService service();
    }

    /**
     * Run the application
     * @param args
     */
    public static void main(String[] args){
        DaggerApp_AppComponent.builder()
                .appModule(new AppModule())
                .apiModule(new ApiModule())
                .dataModule(new DataModule())
                .build()
                .service()
                .run();
    }
}
