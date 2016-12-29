package com.tehmou.creditcardvalidator.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

/**
 * Created by ttuo on 27/01/15.
 */
public class FocusWatcherObservable {
    static public Observable<Boolean> create(@NonNull EditText editText) {
        return Observable.create(new OnSubscribe(editText));
    }

    static class OnSubscribe implements View.OnFocusChangeListener, Observable.OnSubscribe<Boolean> {
        final private Subject<Boolean, Boolean> subject = BehaviorSubject.create();

        private OnSubscribe(@NonNull EditText editText) {
            editText.setOnFocusChangeListener(this);
        }

        @Override
        public void call(Subscriber<? super Boolean> subscriber) {
            subject.subscribe(subscriber);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            subject.onNext(hasFocus);
        }
    }
}
