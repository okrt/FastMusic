package com.okirat.fastmusic.model;

/**
 * Created by Oguz Kirat on 9/4/2015.
 */
public class Post {
    private String id;
    private String date;
    private String link;
    private String author;
    private String featured_image;
    private String title;
    private String excerpt;
    private String content;

    public Post(String id, String date, String link, String author, String featured_image, String title, String excerpt, String content) {
        this.id = id;
        this.date = date;
        this.link = link;
        this.author = author;
        this.featured_image = featured_image;
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




}
