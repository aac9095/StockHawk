package com.sam_chordas.android.stockhawk.retrofit;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Ayush on 20-07-2016.
 */
public interface FinanceAPI {
    String ENDPOINT = "https://query.yahooapis.com";

    @GET("/v1/public/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")

    Call<Finance> getSymbol (@Query("q") String query);
}
