package com.ftinc.lolserv.api;

import com.ftinc.lolserv.api.model.APIDefinition;

public abstract class APIInterface {

    /*******************************************************************************************************************
     *
     * Variables
     *
     */

    private APIDefinition mDefinition;


    /**
     * Constructor
     * @param definition        the api definition
     */
    public APIInterface(APIDefinition definition){
        mDefinition = definition;
    }

    public APIInterface(){}

    /**
     * Get the base definition of this api
     * @return      the base string
     */
    public String getBase(){
        if(mDefinition != null) return mDefinition.getBase();
        return "";
    }

    /**
     * Get the full path for an api that is concatenated
     * with the base string
     *
     * @param path      the endpoint path
     * @return          the full path
     */
    public String getPath(String path){
        return getBase().concat(path);
    }

    /**
     * Setup the filters
     */
    public abstract void setupFilters();

    /**
     * Setup the api's endpoints
     */
    public abstract void setupEndpoints();


}