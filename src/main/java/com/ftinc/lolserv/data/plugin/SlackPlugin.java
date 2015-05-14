package com.ftinc.lolserv.data.plugin;

import com.ftinc.lolserv.data.Config;
import com.ftinc.lolserv.data.model.LolCommit;
import com.ftinc.lolserv.data.model.SlackPayload;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by drew.heavner on 5/13/15.
 */
public class SlackPlugin implements Plugin {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final MediaType JSON = MediaType.parse("application/json");

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private Config mConfig;
    private Gson mGson;
    private OkHttpClient mClient;

    /**
     * Constructor
     *
     * @param client
     */
    public SlackPlugin(Config config,
                       OkHttpClient client,
                       Gson gson){
        mConfig = config;
        mClient = client;
        mGson = gson;
    }

    /***********************************************************************************************
     *
     * Plugin Methods
     *
     */

    @Override
    public void onLolCommit(LolCommit commit) {

        // Generate request payload
        SlackPayload payload = SlackPayload.create(commit);

        // Gson encode payload to string
        String body = mGson.toJson(payload);

        // Prepare request
        Request request = new Request.Builder()
                .url(mConfig.slack.url)
                .post(RequestBody.create(JSON, body))
                .build();

        // Execute request asynchronously
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LOG.error("Error posting to Slack", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LOG.info("Slack API response: \n{}", response.toString());
            }
        });

    }

}
