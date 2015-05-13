package com.ftinc.lolserv.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by r0adkll on 10/31/14.
 */
public class JSONTransformer implements ResponseTransformer {

    public static final Gson GSON = new Gson();

    @Override
    public String render(Object model) throws Exception {
        return GSON.toJson(model);
    }
}
