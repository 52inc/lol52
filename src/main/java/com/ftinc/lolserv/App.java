package com.ftinc.lolserv;

import com.ftinc.lolserv.api.ApiModule;
import com.ftinc.lolserv.data.DataModule;

/**
 * Created by r0adkll on 5/12/15.
 */
public class App{

    /***********************************************************************************************
     *
     * Singleton
     *
     */

    private static App _instance;
    public static App get(){
        if(_instance == null) _instance = new App();
        return _instance;
    }

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private AppComponent mComponent;

    /**
     * Constructor
     */
    public App(){
        mComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .apiModule(new ApiModule())
                .dataModule(new DataModule())
                .build();

        mComponent.service().run();
    }

    /***********************************************************************************************
     *
     * Methods
     *
     */

    public AppComponent component(){
        return mComponent;
    }




    /***********************************************************************************************
     *
     * Main
     *
     */

    /**
     * Run the application
     */
    public static void main(String[] args){
        App.get();
    }

}
