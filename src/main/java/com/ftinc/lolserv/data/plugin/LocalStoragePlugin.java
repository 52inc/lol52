package com.ftinc.lolserv.data.plugin;

import com.ftinc.lolserv.data.Config;
import com.ftinc.lolserv.data.model.LolCommit;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static spark.SparkBase.externalStaticFileLocation;

/**
 * Created by r0adkll on 5/13/15.
 */
public class LocalStoragePlugin implements Plugin {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * The base url for the image to be referenced from
     */
    private Config mConfig;

    /**
     * Constructor
     */
    public LocalStoragePlugin(Config config){
        mConfig = config;
    }

    @Override
    public void onLolCommit(LolCommit commit) {

        File imageFile = new File(getImageDirectory(mConfig), commit.commitHash.concat(".gif"));

        try {
            FileUtils.writeByteArrayToFile(imageFile, commit.imageData);
            LOG.info("Image data written to: {}", imageFile.getAbsolutePath());

            // Now we need to set the image url on the commit
            String base = mConfig.server.baseUrl;
            String path = mConfig.storage.local.path;
            commit.imageUrl = base.concat(path).concat(imageFile.getName());
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

    public static File getImageDirectory(Config mConfig){
        File imageDir = new File(getPublicDirectory().getAbsolutePath().concat(mConfig.storage.local.path));
        if(!imageDir.exists()) imageDir.mkdirs();
        return imageDir;
    }

    public static void enableStaticFileLocation(){
        File publicDir = getPublicDirectory();
        publicDir.mkdirs();

        // Set the static file location
        externalStaticFileLocation(publicDir.getAbsolutePath());
    }

}
