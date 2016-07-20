package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinhood.spark.SparkView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.SparkAdapter;
import com.sam_chordas.android.stockhawk.retrofit.Finance;
import com.sam_chordas.android.stockhawk.retrofit.FinanceAPI;
import com.sam_chordas.android.stockhawk.retrofit.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ayush on 20-07-2016.
 */
public class DetailActivity extends AppCompatActivity implements Callback<Finance> {
    private String bid_price ;
    private TextView symbol;
    private TextView stockName;
    private TextView currency;
    private TextView bid;
    private float []data;
    private SparkView sparkView;
    private SparkAdapter sparkAdapter;
    String msymbol;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        symbol= (TextView) findViewById(R.id.detail_symbol_textview);
        stockName = (TextView) findViewById(R.id.detail_name_textview);
        currency = (TextView) findViewById(R.id.detail_currency_textview);
        bid = (TextView) findViewById(R.id.detail_bid_textview);
        bid_price = getIntent().getStringExtra("bid_price");
        msymbol = getIntent().getStringExtra("symbol");
        String q = "select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22" ;
        String p ="%22%20and%20startDate%20%3D%20%222016-07-11%22%20and%20endDate%20%3D%20%222016-07-19%22";
        String symbolQuery = q + msymbol + p;
        Log.e("Symbol Query",symbolQuery);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FinanceAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FinanceAPI financeAPI = retrofit.create(FinanceAPI.class);
        Call<Finance> financeCall = financeAPI.getSymbol(symbolQuery);
        financeCall.enqueue(this);
        sparkView = (SparkView) findViewById(R.id.sparkview);
        data = new float[0];
        sparkAdapter = new SparkAdapter(data);
        sparkView.setAdapter(sparkAdapter);
    }

    @Override
    public void onResponse(Call<Finance> call, Response<Finance> response) {
        int code = response.code();
        if (code == 200) {
            Finance finance = response.body();
            getStockDetails(finance);
        } else {
            Toast.makeText(this, "Did not work: " + String.valueOf(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<Finance> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();
        Log.e("Throwable ",t.toString());
    }

    void getStockDetails(Finance finance){
        currency.setText("USD");
        bid.setText(bid_price);
        symbol.setText(msymbol);
        List<Quote> quote = finance.getQuery().getResults().getQuote();
        data = new float[quote.size()];
        for(int i=0;i<quote.size();i++){
            data[i]=Float.parseFloat(quote.get(i).getAdjClose());
        }
        sparkAdapter.swapData(data);
    }
}
