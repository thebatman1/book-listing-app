package com.batman.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by HP on 6/10/2017.
 */

public class BooksLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private String mUrl;
    public BooksLoader(Context context , String url) {
        super(context);

        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        return mUrl==null?null:BookUtils.getBooks(mUrl);
    }
}
