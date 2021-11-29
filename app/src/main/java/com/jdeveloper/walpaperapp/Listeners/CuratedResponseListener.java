package com.jdeveloper.walpaperapp.Listeners;

import com.jdeveloper.walpaperapp.Models.CuratedApiResponse;

public interface CuratedResponseListener {
    void onFetch(CuratedApiResponse response, String msg);
    void onError(String msg);

}
