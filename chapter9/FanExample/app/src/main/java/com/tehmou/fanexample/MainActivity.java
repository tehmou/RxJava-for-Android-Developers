package com.tehmou.fanexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Arrays;

import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FanView fanView = (FanView) findViewById(R.id.fan_view);
        View veilView = findViewById(R.id.veil);

        FanViewModel fanViewModel = new FanViewModel(
                RxView.clicks(fanView),
                Arrays.asList(
                        new FanItem("John Smith"),
                        new FanItem("Call"),
                        new FanItem("SMS"),
                        new FanItem("Send email")
                ));
        fanViewModel
                .getFanItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fanView::setFanItems);
        fanViewModel
                .getOpenRatio()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fanView::setOpenRatio);
        fanViewModel
                .getOpenRatio()
                .map(ratio -> (int) (64f * ratio))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(veilView.getBackground()::setAlpha);
    }
}
