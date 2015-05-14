package com.ftinc.lolserv.data.plugin;

import com.ftinc.lolserv.data.model.LolCommit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.IOException;

import static spark.SparkBase.externalStaticFileLocation;

/**
 * Created by r0adkll on 5/13/15.
 */
public class ImageStoragePlugin implements Plugin {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private String mBaseUrl;

    public ImageStoragePlugin(String baseUrl){
        mBaseUrl = baseUrl;
    }

    @Override
    public void onLolCommit(LolCommit commit) {

        File imageFile = new File(getImageDirectory(), commit.commitHash.concat(".gif"));

        try {
            FileUtils.writeByteArrayToFile(imageFile, commit.imageData);
            LOG.info("Image data written to: {}", imageFile.getAbsolutePath());

            // Now we need to set the image url on the commit
            commit.imageUrl = mBaseUrl.concat(imageFile.getName());
        } catch (IOException e) {
            LOG.error("Error writing lolcommit image to disk", e);
        }

        LOG.info("Image Storage Plugin processed lolcommit: {}", commit.commitHash);
    }

    public static File getPublicDirectory(){
        File workingDir = new File(".");
        File publicDir = new File(workingDir.getParentFile(), "public");
        return publicDir;
    }

    public static File getImageDirectory(){
        File imageDir = new File(getPublicDirectory(), "images");
        return imageDir;
    }

    public static void enableStaticFileLocation(){
        getImageDirectory().mkdirs();
        File publicDir = getPublicDirectory();

        // Set the static file location
        externalStaticFileLocation(publicDir.getAbsolutePath());
    }

}
