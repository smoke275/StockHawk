package com.udacity.stockhawk.sync;

import android.os.AsyncTask;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by Shash on 4/2/2017.
 */

public class CheckStockTask extends AsyncTask<String,Void,Boolean> {

    private Runnable onPresent;
    private Runnable onAbsent;

    CheckStockTask(Runnable onPresent, Runnable onAbsent){
        this.onPresent = onPresent;
        this.onAbsent = onAbsent;
    }

    CheckStockTask(Runnable onPresent){
        this(onPresent,null);
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        Stock stock = null;

        try {
             stock = YahooFinance.get(strings[0]);
        } catch (IOException e) {
        }

        if(stock!=null && stock.getName()!=null)
            return true;
        else
            return false;
    }

    @Override
    protected void onPostExecute(Boolean exists) {
        if(exists)
            onPresent.run();
        else
            onAbsent.run();

    }
}
