package com.ftinc.lolserv.api;

import com.ftinc.lolserv.api.model.GETTime;
import com.ftinc.lolserv.data.plugin.LocalStoragePlugin;
import com.ftinc.lolserv.util.Pair;
import com.ftinc.lolserv.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by r0adkll on 5/12/15.
 */
@Singleton
public class API {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /********************************************************
     *
     * Constants
     *
     */

    /**
     * The API interfaces
     */
    @Inject
    List<APIInterface> mInterfaces;

    @Inject @Named("port")
    int mPort;

    /********************************************************
     *
     * Variables
     *
     */

    @Inject
    public API(){}

    /********************************************************
     *
     * Helper Methods
     *
     */

    /**
     * Initialize the API
     */
    public void init(){

        // Set the port
        port(mPort);

        // Setup static files
        LocalStoragePlugin.enableStaticFileLocation();

        // Setup the Filters
        mInterfaces.stream().forEach(APIInterface::setupFilters);

        // Setup the Endpoints
        mInterfaces.stream().forEach(APIInterface::setupEndpoints);

        // Setup the Time endpoint
        GETTime.create();

        exception(JSONException.class, (exception, request, response) -> {
            response.type("application/json");
            response.body(failure("Invalid JSON formed", "Internal server error, please try again"));
        });

    }

    /********************************************************
     *
     * Static Helper Methods
     *
     */

    public static Map<String, Object> success(){
        return new HashMap<>();
    }

    /**
     * Create a filaure object
     *
     * @param technical     the technical explanation
     * @param readable      the readable explanation
     * @return
     */
    public static String failure(String technical, String readable){
        JSONObject json = new JSONObject();
        json.put("technical", technical);
        json.put("readable", readable);
        return json.toString();
    }

    public static void checkParams(int statusCode, Pair<String, String> message, String... params){
        for(String param: params){
            if(Utils.isEmpty(param)){
                halt(statusCode, API.failure(message.first, message.second));
                break;
            }
        }
    }

    public static void checkParams(String... params){
        checkParams(400, Pair.create("Invalid parameters sent", "Invalid Request"), params);
    }

    public static void checkObjects(Object... params){
        for(Object obj: params){
            if(obj == null){
                haltParams();
                break;
            }
        }
    }

    public static void haltSql(SQLException e){
        halt(500, failure("There was a SQL Exception: " + e.getLocalizedMessage(), "Internal Server Error"));
    }

    public static void haltParams(){
        halt(400, API.failure("Invalid parameters sent", "Invalid Request"));
    }

}
