package com.tehmou.creditcardvalidator.utils;

import java.util.regex.Pattern;

/**
 * Created by ttuo on 10/11/15.
 */
public enum CardType {
    UNKNOWN(-1),
    VISA(3),
    MASTER_CARD(3),
    AMERICA_EXPRESS(4);

    private final int cvcLength;

    CardType(int cvcLength) {
        this.cvcLength = cvcLength;
    }

    public int getCvcLength() {
        return cvcLength;
    }

    public static CardType fromNumber(String number) {
        if (regVisa
                .matcher(number).matches()) {
            return VISA;
        } else if (regMasterCard
                .matcher(number).matches()) {
            return MASTER_CARD;
        } else if (regAmericanExpress
                .matcher(number).matches()) {
            return AMERICA_EXPRESS;
        }
        return UNKNOWN;
    }

    private static Pattern regVisa =
            Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
    private static Pattern regMasterCard =
            Pattern.compile("^5[1-5][0-9]{14}$");
    private static Pattern regAmericanExpress =
            Pattern.compile("^3[47][0-9]{13}$");
}
