package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinhood.spark.SparkView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.rest.SparkAdapter;
import com.sam_chordas.android.stockhawk.retrofit.Finance;
import com.sam_chordas.android.stockhawk.retrofit.FinanceAPI;
import com.sam_chordas.android.stockhawk.retrofit.Quote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private TextView minBid;
    private TextView maxBid;
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
        minBid = (TextView) findViewById(R.id.detail_min_textview);
        maxBid = (TextView) findViewById(R.id.detail_max_textview);
        bid = (TextView) findViewById(R.id.detail_bid_textview);
        bid_price = getIntent().getStringExtra(QuoteColumns.BIDPRICE);
        msymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-1);

        int toDay= calendar.get(Calendar.DATE);
        int toMonth = calendar.get(Calendar.MONTH);
        int toYear = calendar.get(Calendar.YEAR);

        String todayDate,prevWeek;

        if(toMonth<10)
            todayDate = String.valueOf(toYear) + "-0" + toMonth + "-" + toDay;
        else
            todayDate = String.valueOf(toYear) + "-" + toMonth + "-" + toDay;


        calendar.add(Calendar.DAY_OF_MONTH,-7);

        int fromDay= calendar.get(Calendar.DATE);
        int fromMonth = calendar.get(Calendar.MONTH);
        int fromYear = calendar.get(Calendar.YEAR);

        if(fromMonth<10)
            prevWeek = String.valueOf(fromYear) + "-0" + fromMonth + "-" + fromDay;
        else
            prevWeek = String.valueOf(fromYear) + "-" + fromMonth + "-" + fromDay;

        String symbolQuery = "select * from yahoo.finance.historicaldata where symbol = \'"
                + msymbol
                + "\' and startDate = \'" + prevWeek + "\' and endDate = \'" + todayDate + "\'";

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
        if(finance!=null){
            bid.setText(bid_price);
            symbol.setText(msymbol);
            ArrayList<Quote> quote = finance.getQuery1().getResults().getQuote();
            data = new float[quote.size()];
            float min = Float.MAX_VALUE;
            float max = Float.MIN_VALUE;
            for(int i=quote.size()-1,j=0;i>=0;i--,j++){
                data[j]=Float.parseFloat(quote.get(i).getAdj_Close());
                if(min>data[j])
                    min = data[j];
                if(max<data[j])
                    max=data[j];
            }
            minBid.setText(String.valueOf(min));
            maxBid.setText(String.valueOf(max));
            sparkAdapter.swapData(data);
        }
        else {
            Log.e("Response","null");
        }
    }
}
