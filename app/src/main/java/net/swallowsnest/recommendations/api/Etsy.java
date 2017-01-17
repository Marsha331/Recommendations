package net.swallowsnest.recommendations.api;

import net.swallowsnest.recommendations.model.ActiveListings;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by marshas on 1/14/17.
 */

public class Etsy {

    private static final String API_KEY = "xfas5ixxl18oc67luyqmlqq4";

    private static RequestInterceptor getInterceptor(){
            return new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addEncodedQueryParam("api_key", API_KEY);
                }
            };
    }

    private static Api getApi(){
        return new RestAdapter.Builder()
                .setEndpoint("https://openapi.etsy.com/v2")
                .setRequestInterceptor(getInterceptor())
                .build()
                .create(Api.class);
    }

    public static void getActiveListings(Callback<ActiveListings> callback) {
        getApi().activeListings("Images, Shop", callback);
    }
}
