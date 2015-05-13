package com.ftinc.lolserv.api.model;

/**
 * This class will describe an endpoint
 *
 * Created by r0adkll on 10/31/14.
 */
public class APIDefinition {

    public String domain;
    public int version;

    /**
     * Constructor
     * @param domain        the domain of the api
     * @param version       the version number
     */
    public APIDefinition(String domain, int version){
        this.domain = domain;
        this.version = version;
    }

    /**
     * Get the base definition of this api
     *
     * @return      the base definition '/domain/V#'
     */
    public String getBase(){
        return String.format("/%s/V%d", domain, version);
    }

}
