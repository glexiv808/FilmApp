package com.example.filmapp.listeners;

import com.example.filmapp.models.TVShow;

public interface WatchlistListener {

    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWatchlist(TVShow tvShow, int position);
}
