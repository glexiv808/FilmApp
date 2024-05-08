package com.example.filmapp.responses;

import com.example.filmapp.models.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowsResponse {

        @SerializedName("page")
        private int page;

        @SerializedName("pages")
        private int totalPages;

        @SerializedName("tv_shows")
        private List<TVShow> tvshows ;
        public int getPage() {
            return page;
        }
        public int getTotalPages() {
            return totalPages;
        }
        public List<TVShow> getTvshows() {
            return tvshows;
        }
}
