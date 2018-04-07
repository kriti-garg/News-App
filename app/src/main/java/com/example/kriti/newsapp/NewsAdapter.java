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

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link News} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of news.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the book at the given position in the list of news
        News currentNews = getItem(position);

        // Find the TextView with News Number
        TextView newsnumberView = (TextView) listItemView.findViewById(R.id.newsnumber);

        // Display the news number of the current news in that TextView
        newsnumberView.setText(currentNews.getNewsNumber().toString());

        // Set the proper background color on the news number circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable numberCircle = (GradientDrawable) newsnumberView.getBackground();

        // Get the appropriate background color based on the current news number
        int newsColor = getColor(currentNews.getNewsNumber());

        // Set the color on the news number circle
        numberCircle.setColor(newsColor);

        // Find the TextView with News Headline
        TextView newsHeadline = (TextView) listItemView.findViewById(R.id.newsHeadline);

        // Display the headline of the current news in that TextView
        newsHeadline.setText(currentNews.getHeadline());

        // Find the TextView with author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);

        // Display the author of the current news in that TextView
        authorView.setText(currentNews.getAuthor());

        // Find the TextView with date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        // Display the date of the current news in that TextView
        dateView.setText(currentNews.getDate());

        // Find the TextView with genre
        TextView genreView = (TextView) listItemView.findViewById(R.id.genre);

        // Display the genre of the current news in that TextView
        genreView.setText(currentNews.getGenre());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the color for the newsnumber circle based on the news Number.
     *
     * @param newsnumber of the book
     */
    private int getColor(Integer newsnumber) {
        int newsnumberColorResourceId;
        switch (newsnumber) {
            case 0:
            case 1:
                newsnumberColorResourceId = R.color.color1;
                break;
            case 2:
                newsnumberColorResourceId = R.color.color2;
                break;
            case 3:
                newsnumberColorResourceId = R.color.color3;
                break;
            case 4:
                newsnumberColorResourceId = R.color.color4;
                break;
            case 5:
                newsnumberColorResourceId = R.color.color5;
                break;
            case 6:
                newsnumberColorResourceId = R.color.color6;
                break;
            case 7:
                newsnumberColorResourceId = R.color.color7;
                break;
            case 8:
                newsnumberColorResourceId = R.color.color8;
                break;
            case 9:
                newsnumberColorResourceId = R.color.color9;
                break;
            default:
                newsnumberColorResourceId = R.color.color10;
                break;
        }

        return ContextCompat.getColor(getContext(), newsnumberColorResourceId);
    }
}

