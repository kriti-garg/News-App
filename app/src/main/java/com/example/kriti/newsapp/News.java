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

/**
 * An {@link News} object contains information related to a single book.
 */
class News {

    private final String mHeadline;
    private final String mAuthor;
    private final String mDate;
    private final String mGenre;
    private final String mUrl;
    private final Integer mNewsNumber;


    /**
     * Constructs a new {@link News} object.
     *
     * @param headline is the headline of the news
     * @param author is the author of the news
     * @param date is the date of the news
     * @param genre is the genre of the news
     * @param url is the website URL of the news.
     * @param newsnumber is the number of the news
     *
     */
    public News(String headline, String author, String date, String genre, String url, Integer newsnumber) {
        mHeadline = headline;
        mAuthor = author;
        mDate = date;
        mGenre = genre;
        mUrl = url;
        mNewsNumber = newsnumber;
    }

    /**
     * Returns the headline of the news.
     */
    public String getHeadline() {
        return mHeadline;
    }

    /**
     * Returns the author's name of the news.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the Date of the news.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the Genre of the news.
     */
    public String getGenre() {
        return mGenre;
    }

    /**
     * Returns the website URL of the news.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Returns the News Number of the book.
     */
    public Integer getNewsNumber() {
        return mNewsNumber;
    }
}
