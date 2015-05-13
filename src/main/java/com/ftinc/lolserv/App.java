package com.ftinc.lolserv;

import com.ftinc.lolserv.api.API;
import com.ftinc.lolserv.data.model.LolCommit;
import com.ftinc.lolserv.util.DBHelper;
import com.ftinc.lolserv.util.RxBus;
import com.github.lalyos.jfiglet.FigletFont;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;

import java.io.IOException;

/**
 * Created by r0adkll on 5/12/15.
 */
public class App implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    /**
     * Run the application
     * @param args
     */
    public static void main(String[] args){
        new App().run();
    }


    @Override
    public void run() {

        PropertyConfigurator.configure("log4j.properties");

        // Print banner
        try {
            String banner = FigletFont.convertOneLine("lol52");
            LOG.info("\n" + banner);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the Database Helper
        DBHelper.get().init();

        // Initialize the API
        API.init();

        RxBus.get().toObserverable()
                .subscribe(o -> {
                    if(o instanceof LolCommit){
                        LolCommit commit = (LolCommit) o;

                        LOG.info(commit.toString());
                    }
                });

    }
}
