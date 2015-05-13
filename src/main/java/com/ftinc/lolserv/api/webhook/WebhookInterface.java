package com.ftinc.lolserv.api.webhook;

import com.ftinc.lolserv.api.APIInterface;
import com.ftinc.lolserv.api.webhook.endpoints.POSTHook;

/**
 * Created by r0adkll on 5/12/15.
 */
public class WebhookInterface extends APIInterface {

    /**
     * Constructor
     */
    public WebhookInterface() {
        super();
    }

    @Override
    public void setupFilters() {

    }

    @Override
    public void setupEndpoints() {
        POSTHook.create(this, "/webhook");
    }

    @Override
    public String getBase() {
        return "/lolcommits";
    }
}
