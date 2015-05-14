package com.ftinc.lolserv.api.main.endpoints;

import com.ftinc.lolserv.App;
import com.ftinc.lolserv.api.API;
import com.ftinc.lolserv.api.APIInterface;
import com.ftinc.lolserv.api.model.Endpoint;
import com.ftinc.lolserv.data.model.LolCommit;
import com.ftinc.lolserv.util.Select;
import com.ftinc.lolserv.util.Utils;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * GET /commits
 *
 * Created by r0adkll on 5/14/15.
 */
public class GETCommits extends Endpoint {

    @Inject
    Provider<Connection> mDb;

    /**
     * Creator
     * @param api
     * @param path
     * @return
     */
    public static GETCommits create(APIInterface api, String path){
        return new GETCommits(api, path);
    }

    /**
     * Constructor
     */
    public GETCommits(APIInterface api, String path) {
        super(api, GET, path);
        App.get().component().inject(this);
    }

    @Override
    public Object handleRequest(Request request, Response response) {

        /*
         * Query params
         *
         * ?
         *
         */

        String _start = request.params(":start");
        String _end = request.params(":end");
        Long start = Utils.parseLong(_start, null);
        Long end = Utils.parseLong(_end, null);

        try(Connection connection = mDb.get()){

            // Build Query
            Select.Builder<LolCommit> builder =
                    Select.with(LolCommit.class, connection)
                        .table("commits");

            if(start != null && end != null){
                builder.where("timestamp>?", start)
                        .and("timestamp<?", end);
            }

            builder.orderBy("timestamp ASC");

            // Convert to json
            return builder.fetch();

        } catch (SQLException e) {
            API.haltSql(e);
        }

        return null;
    }


}