package com.ftinc.lolserv.data.plugin;

import com.ftinc.lolserv.data.model.LolCommit;

/**
 * Created by drew.heavner on 5/13/15.
 */
public interface Plugin {

    void onLolCommit(LolCommit commit);

}
