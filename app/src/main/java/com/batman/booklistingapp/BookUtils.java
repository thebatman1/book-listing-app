package com.batman.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by HP on 6/9/2017.
 */

public class BookUtils {


    private static final String LOG_TAG = BookUtils.class.getSimpleName();
    private static final int RESPONSE_OK = 200;
    private static final int RESPONSE_MOVED_PERMANENTLY = 301;
    private static final int RESPONSE_BAD_REQUEST = 400;
    private static final int RESPONSE_FORBIDDEN = 403;
    private static final int RESPONSE_NOT_FOUND = 404;
    private static final int RESPONSE_TIMEOUT = 408;
    private static final int RESPONSE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_BAD_GATEWAY = 502;
    private static final int RESPONSE_SERVICE_UNAVAILABLE = 503;


    public static ArrayList<Book> getBooks(String requestURL) {
        URL url = createUrl(requestURL);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e) {
            Log.e(LOG_TAG , e.getMessage());
        }

        return getBookListFromJson(jsonResponse);
    }

    //Method to create a URL from the string
    private static URL createUrl( String string ) {
        URL url = null;
        try {
            url = new URL(string);
        }catch (MalformedURLException e) {
            Log.e(LOG_TAG , e.getMessage());
        }
        return url;
    }

    //Method which requests and returns the Books Json Response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == RESPONSE_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG , "ERROR CODE: " + urlConnection.getResponseCode());
            }
        }catch(IOException e) {
            Log.e(LOG_TAG , e.getMessage());
        }finally {
            if (urlConnection!=null) {
                urlConnection.disconnect();
            }
            if (inputStream!=null) {
                try {
                    inputStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    //Read from the InputStream and return the JSON Response
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream , Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Parse JSON and return the information
    private static ArrayList<Book> getBookListFromJson(String jsonRespose) {
        ArrayList<Book> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(jsonRespose)) {
            return null;
        }

        try {

            JSONObject jsonObject = new JSONObject(jsonRespose);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i=0 ; i<jsonArray.length() ; ++i ) {
                JSONObject object = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = object.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                JSONArray array = volumeInfo.getJSONArray("authors");

                StringBuilder authors = new StringBuilder();

                for (int j=0 ; j<array.length() ; ++j) {
                    if (j!=array.length()-1) {
                        authors.append(array.getString(j)).append(", ");
                    }else {
                        authors.append(array.getString(j));
                    }
                }

                arrayList.add(new Book(title , authors.toString()));
            }
                return arrayList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
