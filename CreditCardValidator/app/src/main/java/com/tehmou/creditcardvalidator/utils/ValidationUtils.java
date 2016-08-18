package com.tehmou.creditcardvalidator.utils;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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


    public static Subscription setupTextView(TextView textView, Observable<Boolean> showError) {
        return showError
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        value -> textView.setTextColor(
                                value ? Color.RED : Color.BLACK));

    }

    public static Subscription setupCardType(TextView textView, Observable<CardType> cardType) {
        return cardType
                .map(Enum::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textView::setText);
    }

    public static Subscription setupSubmitButton(View submitButton,
                                                  Observable<Boolean> isValidNumber,
                                                  Observable<Boolean> isValidCvc,
                                                  Observable<Boolean> isKnownCardType) {
        return Observable.combineLatest(
                isValidNumber,
                isValidCvc,
                isKnownCardType,
                (isValidNumberValue, isValidCvcValue, isKnownCardTypeValue) ->
                        isValidNumberValue && isValidCvcValue && isKnownCardTypeValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(submitButton::setEnabled);
    }

    public static Subscription setupErrorDisplay(TextView textView,
                                                  Observable<Boolean> isKnownCardType,
                                                  Observable<Boolean> isValidCheckSum,
                                                  Observable<Boolean> isValidCvc) {
        return Observable.combineLatest(
                Arrays.asList(
                        isKnownCardType.map(value -> value ? "" : "Unknown card type"),
                        isValidCheckSum.map(value -> value ? "" : "Invalid checksum"),
                        isValidCvc.map(value -> value ? "" : "Invalid CVC code")),
                (errorStrings) -> {
                    StringBuilder builder = new StringBuilder();
                    for (Object errorString : errorStrings) {
                        if (!"".equals(errorString)) {
                            builder.append(errorString);
                            builder.append("\n");
                        }
                    }
                    return builder.toString();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textView::setText);
    }
}
