package com.jdeveloper.walpaperapp.Listeners;

import com.jdeveloper.walpaperapp.Models.SearchApiResponse;

public interface SearchResponseListener {
    void onFetch(SearchApiResponse response, String msg);
    void onError(String msg);
}
