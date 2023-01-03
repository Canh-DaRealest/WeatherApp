package com.canhmai.theweatherapi.api.clientservice;

import com.canhmai.theweatherapi.api.apiservice.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientService {

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static ClientService instance;

    private Retrofit retrofit = null;

    private ClientService() {
        //for singleton

    }

    public static  ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public ApiService getRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd  HH-mm-ss")
                    .create();

            OkHttpClient client = new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

        }
        return retrofit.create(ApiService.class);

    }
}
