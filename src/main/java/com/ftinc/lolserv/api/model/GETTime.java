package com.ftinc.lolserv.api.model;

import com.ftinc.lolserv.BuildConfig;
import com.ftinc.lolserv.api.API;
import com.ftinc.lolserv.util.JSONTransformer;
import com.ftinc.lolserv.util.Utils;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;

import java.util.Map;

/**
 * Created by drew.heavner on 1/20/15.
 */
public class GETTime implements Route {

    public static void create(){
        get("/time", new GETTime(), new JSONTransformer());
    }

    @Override
    public Object handle(Request request, Response response) {
        response.type("application/json");
        Map<String, Object> map = API.success();
        map.put("timestamp", Utils.time());
        map.put("version", BuildConfig.VERSION);
        return map;
    }
}
