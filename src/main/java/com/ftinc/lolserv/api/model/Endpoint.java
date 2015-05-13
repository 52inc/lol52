package com.ftinc.lolserv.api.model;

import com.ftinc.lolserv.api.APIInterface;
import com.ftinc.lolserv.util.JSONTransformer;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Route;
import static spark.Spark.*;

/**
 * This is the endpoint model that all api endpoints will extend
 *
 * Created by r0adkll on 10/31/14.
 */
public abstract class Endpoint implements Route {

    /*******************************************************************************************************************
     *
     * Constants
     *
     */

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int DELETE = 2;
    public static final int PUT = 3;

    public static final JSONTransformer jsonTransformer = new JSONTransformer();


    /**
     * Constructor
     */
    public Endpoint(int method, String base, String path, ResponseTransformer transformer){
        String fullPath = base.concat(path);
        switch (method){
            case GET:
                if(transformer != null) {
                    get(fullPath, this, transformer);
                }else{
                    get(fullPath, this);
                }
                break;
            case POST:
                if(transformer != null) {
                    post(fullPath, this, transformer);
                }else{
                    post(fullPath, this);
                }
                break;
            case DELETE:
                if(transformer != null) {
                    delete(fullPath, this, transformer);
                }else{
                    delete(fullPath, this);
                }
                break;
            case PUT:
                if(transformer != null) {
                    put(fullPath, this, transformer);
                }else{
                    put(fullPath, this);
                }
                break;
        }
    }

    public Endpoint(int method, String base, String path){
        this(method, base, path, jsonTransformer);
    }

    /**
     * API Constructor
     *
     * @param api       the api interface
     * @param method    the endpoint method
     * @param path      the endpoint path
     */
    public Endpoint(APIInterface api, int method, String path){
        this(method, api.getBase(), path);
    }

    /**
     * This constructor needs to be overrided to use
     * the other constructor
     *
     * @param api
     * @param path
     */
    public Endpoint(APIInterface api, String path){}


    @Override
    public Object handle(Request request, Response response) {
        response.type("application/json");
        return handleRequest(request, response);
    }

    /**
     * Handle the incoming http request
     *
     * @param request       the http request
     * @param response      the http response
     * @return              the Map structure
     */
    public abstract Object handleRequest(Request request, Response response);
}
