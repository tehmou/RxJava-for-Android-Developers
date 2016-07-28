package com.tehmou.fanexample;

import android.util.Log;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by ttuo on 28/07/16.
 */
public class FanViewModel {
    private static final String TAG = FanViewModel.class.getSimpleName();

    final BehaviorSubject<Boolean> openRatio = BehaviorSubject.create(false);
    final BehaviorSubject<Float> output = BehaviorSubject.create();

    public FanViewModel(Observable<Void> clickObservable) {
        clickObservable
                .doOnNext(click -> Log.d(TAG, "click"))
                .subscribe(click -> openRatio.onNext(!openRatio.getValue()));
         AnimateToOperator.animate(
                 openRatio.map(value -> value ? 1f : 0f), 200)
                 .doOnNext(value -> Log.d(TAG, "value: " + value))
                 .subscribe(output::onNext);
    }

    public Observable<Float> getOutput() {
        return output;
    }
}
