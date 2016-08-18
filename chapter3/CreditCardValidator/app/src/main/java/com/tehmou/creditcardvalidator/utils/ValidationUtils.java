package com.tehmou.creditcardvalidator.utils;

import android.util.Log;

import rx.Observable;

/**
 * Created by ttuo on 24/02/16.
 */
public class ValidationUtils {
    private static final String TAG = ValidationUtils.class.getSimpleName();

    public static Observable<Boolean> and(
            Observable<Boolean> a, Observable<Boolean> b) {
        return Observable.combineLatest(a, b,
                (valueA, valueB) -> valueA && valueB);
    }

    public static boolean checkCardChecksum(String number) {
        Log.d(TAG, "checkCardChecksum(" + number + ")");
        final int[] digits = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            digits[i] = Integer.valueOf(number.substring(i, i + 1));
        }
        return checkCardChecksum(digits);
    }

    public static boolean checkCardChecksum(int[] digits) {
        int sum = 0;
        int length = digits.length;
        for (int i = 0; i < length; i++) {

            // Get digits in reverse order
            int digit = digits[length - i - 1];

            // Every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }

    public static boolean isValidCvc(CardType cardType, String cvc) {
        return cvc.length() == cardType.getCvcLength();
    }
}
