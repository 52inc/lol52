package com.ftinc.lolserv;

import com.ftinc.lolserv.api.ApiComponent;
import com.ftinc.lolserv.api.ApiModule;
import com.ftinc.lolserv.api.main.endpoints.GETCommits;
import com.ftinc.lolserv.data.DataModule;
import dagger.Component;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.sql.Connection;

@Singleton
@Component(modules = {
        AppModule.class,
        ApiModule.class,
        DataModule.class
})
public interface AppComponent{
    AppService service();

    Provider<Connection> getConnectionFactory();

}