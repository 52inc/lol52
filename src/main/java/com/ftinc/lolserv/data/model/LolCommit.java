package com.ftinc.lolserv.data.model;

import com.ftinc.lolserv.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;

/**
 * Created by r0adkll on 5/13/15.
 */
public class LolCommit implements ModelMap<LolCommit> {
    private static final Logger LOG = LoggerFactory.getLogger(LolCommit.class);

    /***********************************************************************************************
     *
     * Static methods
     *
     */



    /**
     * Get LolCommit from a parsed database map
     *
     * @param map
     * @return
     */
    public static LolCommit createFromMap(Map<String, Object> map) {
        LolCommit commit = new LolCommit();
        commit.id           = (Long) map.get("id");
        commit.message      = (String) map.get("message");
        commit.repo         = (String) map.get("repo");
        commit.authorName   = (String) map.get("author_name");
        commit.authorEmail  = (String) map.get("author_email");
        commit.commitHash   = (String) map.get("hash");
        commit.optionalKey  = (String) map.get("optional_key");
        commit.imageUrl     = (String) map.get("image_url");
        commit.timestamp    = (long) map.get("timestamp");
        return commit;
    }

    /***********************************************************************************************
     *
     * Variables
     *
     */

    public Long id;
    public String message;
    public String repo;
    public String authorName;
    public String authorEmail;
    public String commitHash;
    public String optionalKey;
    public String imageUrl;
    public transient byte[] imageData;
    public long timestamp;

    /***********************************************************************************************
     *
     * Methods
     *
     */

    /**
     * Save this object into the database, or update it if it already exists
     *
     * @param connection        the database connection
     * @throws SQLException     there was an error interfacing with the database
     */
    public void save(Connection connection) throws SQLException {

        if(id == null){

            String insert = "INSERT INTO commits (message,repo,author_name,author_email,hash,optional_key,image_url,timestamp) " +
                    "VALUES (?,?,?,?,?,?,?,?)";

            // Prepare the statement
            try(PreparedStatement stmt = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){

                // Set values
                stmt.setString(1, message);
                stmt.setString(2, repo);
                stmt.setString(3, authorName);
                stmt.setString(4, authorEmail);
                stmt.setString(5, commitHash);
                stmt.setString(6, optionalKey);
                stmt.setString(7, imageUrl);
                stmt.setLong(8, Utils.time());

                int result = stmt.executeUpdate();
                if(result > 0){

                    // Get generated keys
                    try(ResultSet keys = stmt.getGeneratedKeys()){
                        if(keys.next()) {
                            id = keys.getLong(1);
                            LOG.info("Lolcommit[{}] was saved!", id);
                        }
                    }

                }

            }

        }else{

            String update = "UPDATE commits SET message=?,repo=?,author_name=?,author_email=?,hash=?,optional_key=?,image_url=?,timestamp=? WHERE id=?";
            try(PreparedStatement stmt = connection.prepareStatement(update)){

                // Set values
                stmt.setString(1, message);
                stmt.setString(2, repo);
                stmt.setString(3, authorName);
                stmt.setString(4, authorEmail);
                stmt.setString(5, commitHash);
                stmt.setString(6, optionalKey);
                stmt.setString(7, imageUrl);
                stmt.setLong(8, Utils.time());
                stmt.setLong(9, id);

                int result = stmt.executeUpdate();
                if(result > 0){
                    LOG.info("Lolcommit[{}] was updated!", id);
                }

            }

        }

    }


    /**
     * Convert object into string representation
     */
    @Override
    public String toString() {
        return String.format("Webhook [image: %s][repo: %s][message: %s][author: %s %s][commit: %s][opt: %s]",
                imageData.length, repo, message, authorName, authorEmail, commitHash, optionalKey);
    }

    /**
     * Get LolCommit from a parsed database map
     *
     * @param map
     * @return
     */
    @Override
    public LolCommit fromMap(Map<String, Object> map) {
        id           = (Long) map.get("id");
        message      = (String) map.get("message");
        repo         = (String) map.get("repo");
        authorName   = (String) map.get("author_name");
        authorEmail  = (String) map.get("author_email");
        commitHash   = (String) map.get("hash");
        optionalKey  = (String) map.get("optional_key");
        imageUrl     = (String) map.get("image_url");
        timestamp    = (int) map.get("timestamp");
        return this;
    }

    /***********************************************************************************************
     *
     * Builder
     *
     */

    /**
     * Builder class
     */
    public static class Builder{

        private LolCommit lc;

        public Builder(){
            lc = new LolCommit();
        }

        public Builder message(String msg){
            lc.message = msg;
            return this;
        }

        public Builder repo(String repo){
            lc.repo = repo;
            return this;
        }

        public Builder author(String name, String email){
            lc.authorName = name;
            lc.authorEmail = email;
            return this;
        }

        public Builder commit(String hash){
            lc.commitHash = hash;
            return this;
        }

        public Builder opt(String optKey){
            lc.optionalKey = optKey;
            return this;
        }

        public Builder image(byte[] data){
            lc.imageData = data;
            return this;
        }

        public LolCommit build(){
            return lc;
        }

    }

}
