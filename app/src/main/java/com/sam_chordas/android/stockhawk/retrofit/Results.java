package com.sam_chordas.android.stockhawk.retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayush on 20-07-2016.
 */
public class Results {
    private List<Quote> quote = new ArrayList<Quote>();

    public List<Quote> getQuote() {
        return quote;
    }

    public void setQuote(List<Quote> quote) {
        this.quote = quote;
    }
}
