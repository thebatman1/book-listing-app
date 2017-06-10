package com.batman.booklistingapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookListingActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Book>>{

    private EditText mSearchQuery;
    private Button mSearchButton;
    private ListView mBookList;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private BooksAdapter adapter;
    private static String QUERY_URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";
    private static String QUERY_URL_MAXRES = "&maxResults=20";
    private static String QUERY_URL;
    private ConnectivityManager cm;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);

        mSearchQuery = (EditText) findViewById(R.id.search_query);
        mSearchButton = (Button) findViewById(R.id.search_button);
        mBookList = (ListView) findViewById(R.id.book_list);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        emptyTextView = (TextView) findViewById(R.id.error_text);
        mBookList.setEmptyView(emptyTextView);
        emptyTextView.setText(R.string.welcome_message);

        //Initializing with an empty arraylist
        adapter = new BooksAdapter(this , new ArrayList<Book>());
        mBookList.setAdapter(adapter);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mSearchQuery.getText())) {
                    Toast.makeText(BookListingActivity.this, "Please Enter a search term!", Toast.LENGTH_SHORT).show();
                }
                else {
                    QUERY_URL = QUERY_URL_BASE + mSearchQuery.getText().toString() + QUERY_URL_MAXRES;
                }

                View focused = BookListingActivity.this.getCurrentFocus();

                if(focused!=null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(focused.getWindowToken() , 0);
                }

                cm = (ConnectivityManager) BookListingActivity.this.getSystemService(CONNECTIVITY_SERVICE);

                NetworkInfo isActive = cm.getActiveNetworkInfo();

                if(!(isActive!=null
                        &&  isActive.isConnectedOrConnecting())) {
                    emptyTextView.setText(R.string.error_message_network);
                }else {
                    if (getLoaderManager().getLoader(0) == null) {
                        getLoaderManager().initLoader(0, null, BookListingActivity.this);
                    }else {
                        adapter.clear();
                        getLoaderManager().restartLoader(0 , null , BookListingActivity.this);
                    }
                    emptyTextView.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BooksLoader(this , QUERY_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        progressBar.setVisibility(View.GONE);
        emptyTextView.setText(R.string.error_message_no_data);

        adapter.clear();

        if (books!=null && !books.isEmpty()) {
            adapter.addAll(books);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        adapter.clear();
    }
}
