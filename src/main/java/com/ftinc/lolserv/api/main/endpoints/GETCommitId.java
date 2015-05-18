package com.ftinc.lolserv.api.main.endpoints;

import com.ftinc.lolserv.App;
import com.ftinc.lolserv.api.API;
import com.ftinc.lolserv.api.APIInterface;
import com.ftinc.lolserv.api.model.Endpoint;
import com.ftinc.lolserv.data.model.LolCommit;
import com.ftinc.lolserv.util.Select;
import com.ftinc.lolserv.util.Utils;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.SQLException;

import static spark.Spark.halt;

/**
 *
 * GET /commits
 *
 * Created by r0adkll on 5/14/15.
 */
public class GETCommitId extends Endpoint {

    /**
     * Creator
     * @param api
     * @param path
     * @return
     */
    public static GETCommitId create(APIInterface api, String path){
        return new GETCommitId(api, path);
    }

    /**
     * Constructor
     */
    public GETCommitId(APIInterface api, String path) {
        super(api, GET, path);
    }

    @Override
    public Object handleRequest(Request request, Response response) {
        Provider<Connection> mDb = App.get()
                .component()
                .getConnectionFactory();

        /*
         * Query params
         *
         * ?
         *
         */

        String _id = request.params(":id");
        try(Connection connection = mDb.get()){

            if(StringUtils.isNumeric(_id)){
                int id = Utils.parseInt(_id, -1);

                LolCommit commit = (LolCommit) Select.with(LolCommit.class, connection)
                        .table("commits")
                        .where("id=?", id)
                        .fetchSingle();

                if(commit != null){
                    return commit;
                }else{
                    halt(404, API.failure("There was no lolcommit found for the id: " + _id, "No commit found for this id"));
                }

            }else{

                LolCommit commit = (LolCommit) Select.with(LolCommit.class, connection)
                        .table("commits")
                        .where("hash=?", String.format("\"%s\"", _id))
                        .fetchSingle();

                if(commit != null){
                    return commit;
                }else{
                    halt(404, API.failure("There was no lolcommit found for the id: " + _id, "No commit found for this id"));
                }

            }

        } catch (SQLException e) {
            API.haltSql(e);
        }

        return null;
    }


}
