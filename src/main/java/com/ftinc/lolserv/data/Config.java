package com.ftinc.lolserv.data;

import java.util.List;

/**
 * Created by drew.heavner on 5/14/15.
 */
public class Config {

    public Server server;
    public Database database;
    public Storage storage;
    public Slack slack;
    public List<String> plugins;

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
            // TODO: Find what is actually required of S3
            public String meta;
            public String meta2;
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
