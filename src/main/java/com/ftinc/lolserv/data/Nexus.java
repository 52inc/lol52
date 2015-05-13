package com.ftinc.lolserv.data;

import com.ftinc.lolserv.data.model.LolCommit;
import com.ftinc.lolserv.data.plugin.Plugin;
import com.ftinc.lolserv.util.RxBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * This is the central communication hub for all the lolcommit
 * webhook bus events be be handled with plugins
 *
 * Created by drew.heavner on 5/13/15.
 */
@Singleton
public class Nexus implements Observer<Object>{
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /***********************************************************************************************
     *
     * Variables
     *
     */

    @Inject
    List<Plugin> mPlugins;

    /**
     * Injectable Constructor
     */
    @Inject
    public Nexus(){}

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    /**
     * Initialize the Nexus
     */
    public void init(){

        // Subscribe to the event bus
        RxBus.get().toObserverable().subscribe(this);

    }

    /**
     * Apply a lolcommit to every plugin
     *
     * @param commit        the commit to notify the plugins with
     */
    private void applyPlugins(LolCommit commit){
        mPlugins.stream().forEach(plugin -> plugin.onLolCommit(commit));
    }

    /***********************************************************************************************
     *
     * Bus observer
     *
     */

    @Override
    public void onNext(Object o) {
        if(o instanceof LolCommit){
            LolCommit commit = (LolCommit) o;
            applyPlugins(commit);
        }
    }

    @Override
    public void onError(Throwable e) {
        LOG.error("Something went wrong in the RxBus", e);
    }

    @Override
    public void onCompleted() {
        LOG.info("EvenBus has dried up!");
    }
}
