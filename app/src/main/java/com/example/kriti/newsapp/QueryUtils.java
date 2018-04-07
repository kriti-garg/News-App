/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.kriti.newsapp;

import android.net.Uri;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.kriti.newsapp.NewsAppActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving book data from Google API.
 */
final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    private QueryUtils() {
    }

    /**
     * Query the dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData() {

        // Create URL object
        URL url = createUrl();

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s

        // Return the list of {@link News}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl() {
        URL url = null;
        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .encodedAuthority("content.guardianapis.com")
                    .appendPath("search")
                    .appendQueryParameter("order-by", "newest")
                    .appendQueryParameter("show-references", "author")
                    .appendQueryParameter("show-tags", "contributor")
                    .appendQueryParameter("q", "apple")
                    .appendQueryParameter("api-key", "test");
            String stringUrl = builder.build().toString();
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making HTTP request:", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the newsJSON string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject JsonResponse = baseJsonResponse.getJSONObject("response");


            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news.
            JSONArray newsArray = JsonResponse.getJSONArray("results");


            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of books
                JSONObject currentNews = newsArray.getJSONObject(i);

                String newsHeadline;
                try {
                    newsHeadline = currentNews.getString("webTitle");
                } catch (JSONException e) {
                    newsHeadline = "News Headline Unknown";
                }

                String newsAuthors;
                try {
                    JSONArray authorsArray = currentNews.getJSONArray("tags");
                    if (authorsArray.length() == 0) {
                        newsAuthors = "Author Unknown";
                    } else {
                        newsAuthors = formatAuthors(authorsArray);
                    }
                } catch (JSONException e) {
                    newsAuthors = "Author Unknown";
                }

                String newsDate;
                try {
                    newsDate = currentNews.getString("webPublicationDate");
                    newsDate = formatDate(newsDate);
                } catch (JSONException e) {
                    newsDate = "News Date Unknown";
                }

                String newsGenre;
                try {
                    newsGenre = currentNews.getString("sectionName");
                } catch (JSONException e) {
                    newsGenre = "News Genre Unknown";
                }

                String newsURL;
                try {
                    newsURL = currentNews.getString("webUrl");
                } catch (JSONException e) {
                    newsURL = "News Link Unknown";
                }

                // Create a new {@link News} object with the headlines, authors, date, genre, url, newsnumber,
                // and url from the JSON response.
                News news = new News(newsHeadline, newsAuthors, newsDate, newsGenre, newsURL, i + 1);

                // Add the new {@link News} to the list of books.
                newsList.add(news);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return newsList;
    }

    private static String formatDate(String rawDate) {
        try {
            String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }

    private static String formatAuthors(JSONArray authorsList) throws JSONException {
        StringBuilder newsAuthor = null;
        for (int i = 0; i < authorsList.length(); i++) {
            JSONObject obj = authorsList.getJSONObject(i);
            if (i == 0) {
                newsAuthor = new StringBuilder(obj.getString("webTitle"));
            } else {
                newsAuthor.append(obj.getString("webTitle"));
            }
            if (i != authorsList.length() - 1)
                newsAuthor.append(",");
        }
        return newsAuthor.toString();
    }

}
