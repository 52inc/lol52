package com.ftinc.lolserv.api.main;

import com.ftinc.lolserv.api.APIInterface;
import com.ftinc.lolserv.api.model.APIDefinition;

/**
 * Created by r0adkll on 5/13/15.
 */
public class MainInterface extends APIInterface{

    public MainInterface() {
        super(new APIDefinition("API", 1));
    }

    @Override
    public void setupFilters() {

    }

    @Override
    public void setupEndpoints() {

        // GETCommits.create(this, "/commits");
        // GETCommits.create(this, "/commits/:start/:end");
        // GETUsers.create(this, "/users");


    }


}
