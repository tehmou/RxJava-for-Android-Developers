package com.tehmou.fanexample;

import android.util.Log;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by ttuo on 28/07/16.
 */
public class FanViewModel {
    private static final String TAG = FanViewModel.class.getSimpleName();

    private final BehaviorSubject<List<FanItem>> fanItems;
    private final BehaviorSubject<Boolean> isOpen = BehaviorSubject.create(false);
    private final BehaviorSubject<Float> openRatio = BehaviorSubject.create();

    public FanViewModel(Observable<Void> clickObservable,
                        List<FanItem> fanItems) {
        this.fanItems = BehaviorSubject.create(fanItems);
        clickObservable
                .doOnNext(click -> Log.d(TAG, "click"))
                .subscribe(click -> isOpen.onNext(!isOpen.getValue()));
         AnimateToOperator.animate(
                 isOpen.map(value -> value ? 1f : 0f), 200)
                 .doOnNext(value -> Log.d(TAG, "value: " + value))
                 .subscribe(openRatio::onNext);
    }

    public Observable<Float> getOpenRatio() {
        return openRatio;
    }

    public Observable<List<FanItem>> getFanItems() {
        return fanItems;
    }
}
