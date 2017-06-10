package com.batman.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 6/7/2017.
 */

public class BooksAdapter extends ArrayAdapter<Book> {

    public BooksAdapter(Context context , ArrayList<Book> bookList) {
        super(context , 0 , bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item , parent , false);

            holder = new ViewHolder();
            holder.bookName = (TextView) convertView.findViewById(R.id.book_title);
            holder.authorName = (TextView) convertView.findViewById(R.id.book_author);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);
        holder.bookName.setText(book.getName());
        holder.authorName.setText(book.getAuthorName());

        return convertView;
    }

    private static class ViewHolder {
        private TextView bookName , authorName;
    }
}
