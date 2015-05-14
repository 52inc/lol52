package com.ftinc.lolserv;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.ftinc.lolserv.api.ApiModule;
import com.ftinc.lolserv.data.Config;
import com.ftinc.lolserv.data.DataModule;
import dagger.Component;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by r0adkll on 5/12/15.
 */
public class App{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    /***********************************************************************************************
     *
     * Singleton
     *
     */

    private static App _instance;

    public static App init(Config cfg){
        if(_instance == null) _instance = new App(cfg);
        return _instance;
    }

    public static App get(){
        if(_instance == null) _instance = new App(loadDefaultConfig());
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
    public App(Config config){
        if(BuildConfig.ENVIRONMENT == BuildConfig.LOCAL) {
            PropertyConfigurator.configure("log4j.properties");
        }

        LOG.info("Configuration: {}", config);

        mComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(config))
                .apiModule(new ApiModule())
                .dataModule(new DataModule())
                .build();

        mComponent.service().run();
    }

    public App(){}

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
        App.init(parseConfig(args));
    }

    private static Config parseConfig(String[] args){
        if(args != null && args.length > 0) {
            String config = args[0];
            File configFile = new File(config);
            Yaml yaml = new Yaml();

            if (configFile.exists()) {

                try {
                    return yaml.loadAs(FileUtils.openInputStream(configFile), Config.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return loadDefaultConfig();
    }

    private static Config loadDefaultConfig(){
        String fullPath = "config/default_config.yml";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(fullPath);
        Yaml yaml = new Yaml();
        return yaml.loadAs(is, Config.class);
    }

}
