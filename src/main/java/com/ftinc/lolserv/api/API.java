package com.ftinc.lolserv.api;

import com.ftinc.lolserv.api.main.MainInterface;
import com.ftinc.lolserv.api.model.GETTime;
import com.ftinc.lolserv.api.webhook.WebhookInterface;
import com.ftinc.lolserv.util.Pair;
import com.ftinc.lolserv.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by r0adkll on 5/12/15.
 */
public class API {

    /********************************************************
     *
     * Constants
     *
     */

    public static final int PORT = 8321;

    /**
     * The API interfaces
     */
    public static final List<APIInterface> INTERFACES = new ArrayList<APIInterface>(){
        {
            add(new WebhookInterface());
            add(new MainInterface());
        }
    };

    /********************************************************
     *
     * Variables
     *
     */



    /********************************************************
     *
     * Helper Methods
     *
     */

    /**
     * Initialize the API
     */
    public static void init(){

        // Set the port
        port(PORT);

        // Setup the Filters
        INTERFACES.stream().forEach(APIInterface::setupFilters);

        // Setup the Endpoints
        INTERFACES.stream().forEach(APIInterface::setupEndpoints);

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
