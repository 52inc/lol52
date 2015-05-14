package com.ftinc.lolserv.data.plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ftinc.lolserv.data.Config;
import com.ftinc.lolserv.data.model.LolCommit;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by drew.heavner on 5/14/15.
 */
public class S3StoragePlugin implements Plugin{
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private Config mConfig;

    public S3StoragePlugin(Config cfg){
        mConfig = cfg;
    }

    @Override
    public void onLolCommit(LolCommit commit) {

        Config.Storage.S3 s3 = mConfig.storage.s3;

        // Build AWS S3 client
        AmazonS3Client client = new AmazonS3Client(new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return s3.access;
            }

            @Override
            public String getAWSSecretKey() {
                return s3.secret;
            }
        });

        File imageFile = new File("temp/" + commit.commitHash.concat(".gif"));

        try {
            FileUtils.writeByteArrayToFile(imageFile, commit.imageData);
            PutObjectRequest request = new PutObjectRequest(s3.bucket, imageFile.getName(), imageFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult result = client.putObject(request);

            // Get the resource URL
            commit.imageUrl = client.getResourceUrl(s3.bucket, imageFile.getName());
            imageFile.delete();

            LOG.info("Image uploaded to S3: {}, {}, {}, url: {}", result.getVersionId(), result.getETag(), result.getContentMd5(), commit.imageUrl);
        } catch (IOException e) {
            LOG.error("Error writing lolcommit image to disk", e);
        }



    }
}
