package com.ftinc.lolserv.data.model;

/**
 * Created by r0adkll on 5/13/15.
 */
public class LolCommit {

    public String message;
    public String repo;
    public String authorName;
    public String authorEmail;
    public String commitHash;
    public String optionalKey;
    public String imageUrl;

    @Override
    public String toString() {
        return String.format("Webhook [image: %s][repo: %s][message: %s][author: %s %s][commit: %s][opt: %s]",
                imageUrl, repo, message, authorName, authorEmail, commitHash, optionalKey);
    }

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

        public Builder image(String url){
            lc.imageUrl = url;
            return this;
        }

        public LolCommit build(){
            return lc;
        }

    }

}
