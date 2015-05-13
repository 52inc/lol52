package com.ftinc.lolserv.api.webhook.endpoints;

import com.ftinc.lolserv.api.API;
import com.ftinc.lolserv.api.APIInterface;
import com.ftinc.lolserv.api.model.Endpoint;
import com.ftinc.lolserv.data.model.LolCommit;
import com.ftinc.lolserv.util.RxBus;
import com.ftinc.lolserv.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

import static spark.Spark.halt;

/**
 * This is the webhook endpoint for catching requests sent from the uploldz lolcommit plugin
 *
 * Created by r0adkll on 5/12/15.
 */
public class POSTHook extends Endpoint {
    private static final Logger LOG = LoggerFactory.getLogger(POSTHook.class);
    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));

    public static POSTHook create(APIInterface api, String path){
        return new POSTHook(api, path);
    }

    /**
     * Constructor
     *
     * @param api
     * @param path
     */
    public POSTHook(APIInterface api, String path) {
        super(api, POST, path);
    }

    @Override
    public Object handleRequest(Request request, Response response) {
        request.attribute(org.eclipse.jetty.server.Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);

        // Pull request params
        try {
            Part file = request.raw().getPart("file");
            byte[] gifData = IOUtils.toByteArray(file.getInputStream());

            // parse the other parameters
            String message = request.queryParams("message");
            String repo = request.queryParams("repo");
            String authorName = request.queryParams("author_name");
            String authorEmail = request.queryParams("author_email");
            String commitHash = request.queryParams("sha");
            String optKey = request.queryParams("key");

            // Emit commit on the rxevent bus
            RxBus.get().send(new LolCommit.Builder()
                    .message(message)
                    .repo(repo)
                    .author(authorName, authorEmail)
                    .commit(commitHash)
                    .opt(optKey)
                    .image(gifData)
                    .build());

        } catch (IOException e) {
            halt(500, API.failure("Error getting file", "Error uploading file"));
        } catch (ServletException e) {
            halt(500, API.failure("Error getting file", "Error uploading file"));
        }

        return API.success();
    }
}
