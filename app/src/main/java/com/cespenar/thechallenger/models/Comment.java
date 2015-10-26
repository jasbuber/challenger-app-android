package com.cespenar.thechallenger.models;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasbuber on 24/10/2015.
 */
public class Comment implements Serializable {

    private Long id;

    private User author;

    private Date creationTimestamp;

    private String message;

    private long relevantObjectId;

    public Comment(){}

    public Comment(long id, User author, String message, long relevantObjectId, Date creationTimestamp){
        this(author, message, relevantObjectId, creationTimestamp);
        this.id = id;
    }

    public Comment(User author, String message, long relevantObjectId, Date creationTimestamp){
        this.author = author;
        this.message = message;
        this.relevantObjectId = relevantObjectId;
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author='" + author + '\'' +
                ", objectId='" + relevantObjectId + '\'' +
                ",time=" + creationTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return false;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }



    public String getMessage() {
        return message;
    }

    public long getRelevantObjectId() {
        return relevantObjectId;
    }

    /**
     * TO_DO Remove as soon as possible after Gson casting was figured out
     * @param map
     * @return
     */
    public static List<Comment> castLinkedTreeMapToChallengeList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<Comment> comments = new ArrayList<>();

        for(LinkedTreeMap<String, Object> comment : map){

            comments.add(castLinkedTreeMapToComment(comment));
        }

        return comments;
    }

    public static Comment castLinkedTreeMapToComment(LinkedTreeMap<String, Object> comment) {

        String message = String.valueOf(comment.get("message"));
        LinkedTreeMap author = (LinkedTreeMap) comment.get("author");
        long id = (long)((double) comment.get("id"));
        long challengeId = (long)((double) comment.get("relevantObjectId"));
        Date creationTimestamp = new Date();

        try {
            creationTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse((String) comment.get("creationTimestamp"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Comment(id, User.castLinkedTreeMapToUser(author), message, challengeId, creationTimestamp);
    }
}
