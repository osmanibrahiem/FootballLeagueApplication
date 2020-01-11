package io.osman.football.league.repository.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.osman.football.league.helper.ConstantUtils;
import io.osman.football.league.helper.NetworkUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FootballClient {

    private static FootballClient instance;
    private final Retrofit retrofit;
    private FootballAPI service;

    private FootballClient(Context context) {
        long cacheSize = (5 * 1024 * 1024);
        Cache myCache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.cache(myCache);

        httpClient.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader(ConstantUtils.API_KEY_PARAM, ConstantUtils.API_KEY);

            if (!NetworkUtils.isNetworkConnected(context)) {
                requestBuilder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7);
            } else {
                requestBuilder.addHeader("Cache-Control", "public, max-stale=" + 60);
            }

            return chain.proceed(requestBuilder.build());
        });

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .baseUrl(ConstantUtils.FOOTBALL_API_URL)
                .build();
    }

    public synchronized static FootballClient getInstance(Context context) {
        if (instance == null) {
            instance = new FootballClient(context);
        }
        return instance;
    }

    public FootballAPI getService() {
        if (service == null) {
            this.service = retrofit.create(FootballAPI.class);
        }
        return service;
    }
}
