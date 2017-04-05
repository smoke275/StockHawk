package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.base.Splitter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shash on 4/4/2017.
 */

public class StockChartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_LOADER = 0;
    private static final String STOCK_SYMBOL = "symbol";
    @BindView(R.id.chart)
    LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);
        Log.d("onCreate", "called");
        Bundle bundle = null;
        if(getIntent() != null && getIntent().getExtras()!=null)
            bundle = getIntent().getExtras();
        if(bundle != null)
            getSupportLoaderManager().initLoader(STOCK_LOADER, bundle, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Quote.makeUriForStock(args.getString(STOCK_SYMBOL)),
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() != 0) {
            data.moveToFirst();
            Iterable<String> dayWiseToken = Splitter.on('\n').split(data.getString(Contract.Quote.POSITION_HISTORY));
            List<Entry> entries = new ArrayList<Entry>();
            int counter = 0;
            for(String dayWise : dayWiseToken){
                String[] dayVsQuote = dayWise.split(",");
                if(dayVsQuote.length==2)
                    entries.add(new Entry(counter,Float.parseFloat(dayVsQuote[1])));
                counter +=5;

            }
            LineDataSet dataSet = new LineDataSet(entries,"Price");
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
