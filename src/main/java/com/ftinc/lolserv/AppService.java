package com.ftinc.lolserv;

import com.ftinc.lolserv.api.API;
import com.ftinc.lolserv.data.Nexus;
import com.ftinc.lolserv.data.DB;
import com.github.lalyos.jfiglet.FigletFont;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by drew.heavner on 5/13/15.
 */
public class AppService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Inject
    Nexus mNexus;

    @Inject
    API mApi;

    @Inject
    DB mDb;

    /**
     * Constructor
     */
    @Inject
    public AppService(){}

    /**
     * Run this app service
     */
    public void run(){

        // Print banner
        try {
            String banner = FigletFont.convertOneLine("lol52");
            LOG.info("\n" + banner);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the Database Helper
        mDb.init();

        // Initialize the API
        mApi.init();

        // Initialize the Nexus
        mNexus.init();

    }

}
