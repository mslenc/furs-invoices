package com.github.mslenc.furslib.validation;

import static java.lang.Integer.parseInt;

public class TaxNumberValidator {
    private final String propertyName;
    private final boolean allowNull;

    public TaxNumberValidator(String propertyName, boolean allowNull) {
        this.propertyName = propertyName;
        this.allowNull = allowNull;
    }

    public Integer validate(Integer taxNumber) {
        if (taxNumber == null)
            return allowNull ? null : fail(null);

        if (taxNumber >= 1000_0000 && taxNumber <= 9999_9999)
            return taxNumber;

        return fail(taxNumber);
    }

    public Integer validate(int taxNumber) {
        if (taxNumber >= 1000_0000 && taxNumber <= 9999_9999)
            return taxNumber;

        if (taxNumber == 0 && allowNull)
            return null;

        return fail(taxNumber);
    }

    public Integer validateAndConvert(String taxNumber) {
        String originalTaxNumber = taxNumber;

        if (taxNumber == null || taxNumber.isEmpty())
            return allowNull ? null : fail(originalTaxNumber);

        if (taxNumber.length() == 10) {
            if (taxNumber.regionMatches(true, 0, "si", 0, 2)) {
                taxNumber = taxNumber.substring(2);
            } else {
                return fail(taxNumber);
            }
        }

        if (taxNumber.length() == 8) {
            if (allDigits(taxNumber)) {
                return validate(parseInt(taxNumber));
            } else {
                return fail(originalTaxNumber);
            }
        }

        return fail(originalTaxNumber);
    }

    Integer fail(Object value) {
        throw new IllegalArgumentException(propertyName + " must be a number between 10000000 and 99999999; instead, it is " + value);
    }

    static boolean allDigits(String taxNumber) {
        for (int i = 0, n = taxNumber.length(); i < n; i++) {
            char c = taxNumber.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
