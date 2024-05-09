package com.example.filmapp.responses;

import com.example.filmapp.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;
    public TVShowDetails getTvShowDetails(){
        return tvShowDetails;
    }
}
