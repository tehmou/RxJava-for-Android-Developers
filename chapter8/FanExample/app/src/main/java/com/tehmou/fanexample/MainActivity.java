package com.tehmou.fanexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jakewharton.rxbinding.view.RxView;

import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FanView fanView = (FanView) findViewById(R.id.fan_view);

        FanViewModel fanViewModel = new FanViewModel(RxView.clicks(fanView));
        fanViewModel
                .getOutput()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fanView::setOpenRatio);
    }
}
