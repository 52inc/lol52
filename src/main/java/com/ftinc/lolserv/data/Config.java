package com.ftinc.lolserv.data;

import com.ftinc.lolserv.data.plugin.*;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Provider;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by drew.heavner on 5/14/15.
 */
public class Config {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /***********************************************************************************************
     *
     * Variables
     *
     */

    public Server server;
    public Database database;
    public Storage storage;
    public Slack slack;
    public List<String> plugins;

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */


    public List<Plugin> generatePlugins(OkHttpClient client,
                                        Gson gson,
                                        Provider<Connection> dbProvider){
        List<Plugin> plugs = new ArrayList<>();
        for(String plg: plugins){
            Plugin plug = createPlugin(plg, client, gson, dbProvider);
            if(plug != null) plugs.add(plug);
        }
        return plugs;
    }

    private Plugin createPlugin(String id,
                                OkHttpClient client,
                                Gson gson,
                                Provider<Connection> dbProvider){
        switch (id){
            case "storage-local":
                LOG.info("Creating Local Storage Plugin...");
                return new LocalStoragePlugin(this);
            case "storage-s3":
                LOG.info("Creating S3 Storage Plugin...");
                return new S3StoragePlugin(this);
            case "database":
                LOG.info("Creating Database Plugin...");
                return new DatabasePlugin(dbProvider);
            case "slack":
                LOG.info("Creating Slack Plugin");
                return new SlackPlugin(this, client, gson);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "\nConfig {\n" +
                    "\t%s,\n" +
                    "\t%s,\n" +
                    "\t%s,\n" +
                    "\t%s,\n" +
                    "\t%s,\n}",
                server, database, storage, slack, plugins);
    }

    /***********************************************************************************************
     *
     * Configuration Classes
     *
     */

    public static class Server{
        public int port;
        public String name;
        public String baseUrl;

        @Override
        public String toString() {
            return String.format("Server [%s:%d - %s]", name, port, baseUrl);
        }
    }

    public static class Database{
        public String name;
        public String address;
        public String username;
        public String password;

        @Override
        public String toString() {
            return String.format("Database [%s, %s - %s, %s]", name, address, username, password);
        }
    }

    public static class Storage{

        public Local local;
        public S3 s3;
        public FTP ftp;
        public SFTP sftp;
        public Imgur imgur;

        @Override
        public String toString() {
            return String.format("Storage [%s, %s, %s, %s, %s]", local, s3, ftp, sftp, imgur);
        }

        public static class Local{
            public String path;

            @Override
            public String toString() {
                return String.format("Local [%s]", path);
            }
        }

        public static class S3{
            public String access;
            public String secret;
            public String bucket;
        }

        public static class FTP {
            public String host;
            public int port;
            public String user;
            public String pass;
        }

        public static class SFTP {
            public String host;
            public int port;
            public String user;
            public String pass;
        }

        public static class Imgur {
            public String apitoken;
        }

    }

    public static class Slack {
        public String url;

        @Override
        public String toString() {
            return String.format("Slack [%s]", url);
        }
    }


}
