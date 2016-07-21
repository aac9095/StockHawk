package com.sam_chordas.android.stockhawk.rest;

import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Ayush on 20-07-2016.
 */
public class SparkAdapter extends com.robinhood.spark.SparkAdapter {
    private float[] yData;

    public SparkAdapter(float[] yData) {
        this.yData = yData;
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }

    public void swapData(float []data){
        yData=data;
        notifyDataSetChanged();
    }

}
