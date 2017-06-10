package com.batman.booklistingapp;

/**
 * Created by HP on 6/7/2017.
 */

public class Book {
    private String mName , mAuthorName;

    public Book(String name, String authorName) {
        this.setName(name);
        this.setAuthorName(authorName);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String mAuthorName) {
        this.mAuthorName = mAuthorName;
    }
}
