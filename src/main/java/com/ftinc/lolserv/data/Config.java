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

    public static class Server{
        public int port;
        public String name;
    }

    public static class Database{
        public String name;
        public String address;
        public String username;
        public String password;
    }

    public static class Storage{

        public Local local;
        public S3 s3;
        public FTP ftp;
        public SFTP sftp;
        public Imgur imgur;

        public static class Local{
            public String path;
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
    }


}
