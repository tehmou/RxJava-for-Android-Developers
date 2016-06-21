package com.tehmou.creditcardvalidator.utils;

import rx.Observable;

/**
 * Created by ttuo on 24/02/16.
 */
public class ValidationUtils {
    public static Observable<Boolean> and(
            Observable<Boolean> a, Observable<Boolean> b) {
        return Observable.combineLatest(a, b,
                (valueA, valueB) -> valueA && valueB);
    }
}
