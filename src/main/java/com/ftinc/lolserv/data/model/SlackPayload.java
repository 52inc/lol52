package com.ftinc.lolserv.data.model;

import com.ftinc.lolserv.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by drew.heavner on 5/13/15.
 */
public class SlackPayload {

    /***********************************************************************************************
     *
     * Static methods
     *
     */

    public static SlackPayload create(LolCommit commit){
        SlackPayload payload = new SlackPayload();

        // 1) Create fields
        String repoVal = (!Utils.isEmpty(commit.repo) && !commit.repo.equalsIgnoreCase("/")) ? commit.repo : commit.optionalKey;
        Field repoField = new Field("Repository", repoVal, true);
        Field hashField = new Field("Commit", commit.commitHash, true);

        // 2) Create Attachment
        Attachment attachment = new Attachment.Builder()
                .pretext(String.format("New lolcommit from %s", commit.authorName))
                .text(commit.message)
                .title(commit.commitHash.concat(".gif"))
                .color("#52AADD")
                .image(commit.imageUrl)
                .fields(repoField, hashField)
                .build();

        // Add the attachment to the payload
        payload.attachments.add(attachment);

        // return payload
        return payload;
    }

    /***********************************************************************************************
     *
     * Variables
     *
     */

    public List<Attachment> attachments;

    /**
     * Default Constructor
     */
    public SlackPayload(){
        attachments = new ArrayList<>();
    }

    /***********************************************************************************************
     *
     * Inner Models
     *
     */

    /**
     * Represents a Slack Attachment Object
     */
    public static class Attachment{

        public String pretext;
        public String text;
        public String title;
        public String color;
        public String image_url;
        public List<Field> fields;

        public Attachment(){
            fields = new ArrayList<>();
        }

        public static class Builder{

            private Attachment attachment;

            public Builder(){
                attachment = new Attachment();
            }

            public Builder pretext(String txt){
                attachment.pretext = txt;
                return this;
            }

            public Builder text(String txt){
                attachment.text = txt;
                return this;
            }

            public Builder title(String title){
                attachment.title = title;
                return this;
            }

            public Builder color(String color){
                attachment.color = color;
                return this;
            }

            public Builder image(String url){
                attachment.image_url = url;
                return this;
            }

            public Builder field(Field field){
                attachment.fields.add(field);
                return this;
            }

            public Builder fields(Field... fields){
                attachment.fields.addAll(Arrays.asList(fields));
                return this;
            }

            public Builder fields(Collection<Field> fields){
                attachment.fields.addAll(fields);
                return this;
            }

            public Attachment build(){
                return attachment;
            }

        }

    }

    /**
     * Represents a Slack Field Object
     */
    public static class Field{

        public String title;
        public String value;

        @SerializedName("short")
        public boolean isShort;

        public Field(String title, String value, boolean isShort){
            this.title = title;
            this.value = value;
            this.isShort = isShort;
        }

    }

}
