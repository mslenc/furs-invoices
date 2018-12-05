package com.github.mslenc.fursinvoices.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static com.github.mslenc.fursinvoices.validation.DecimalValidator.NullZeroMode.NO_NULLS;
import static com.github.mslenc.fursinvoices.validation.DecimalValidator.NullZeroMode.ZERO_TO_NULL;
import static java.math.BigDecimal.ZERO;

public class DecimalValidator {
    public enum NullZeroMode {
        NO_NULLS,
        NULL_ALLOWED,
        ZERO_TO_NULL
    }

    private final String propertyName;
    private final int scale;
    private final BigDecimal minimum;
    private final BigDecimal maximum;
    private final long scaleMultiplier;
    private final long minUnscaled;
    private final long maxUnscaled;
    private final BigDecimal normalizedZero;
    private final NullZeroMode nullZeroMode;

    public DecimalValidator(String propertyName, int totalDigits, int decimalDigits, boolean allowNegative, NullZeroMode nullZeroMode) {
        if (decimalDigits >= totalDigits)
            throw new IllegalArgumentException("decimalDigits " + decimalDigits + " >= totalDigits " + totalDigits);

        if (totalDigits < 1 || totalDigits > 18)
            throw new UnsupportedOperationException("totalDigits " + totalDigits + " not in range 1..18");

        if (decimalDigits < 0)
            throw new IllegalArgumentException("decimalDigits " + decimalDigits + " < 0");

        this.propertyName = Objects.requireNonNull(propertyName, "propertyName");
        this.nullZeroMode = Objects.requireNonNull(nullZeroMode, "nullZeroMode");
        this.scale = decimalDigits;
        this.maximum = BigDecimal.valueOf(powerOfTen(totalDigits) - 1, scale);
        this.scaleMultiplier = powerOfTen(decimalDigits);
        this.maxUnscaled = maximum.unscaledValue().longValueExact();

        if (allowNegative) {
            this.minimum = this.maximum.negate();
            this.minUnscaled = -this.maxUnscaled;
        } else {
            this.minimum = ZERO;
            this.minUnscaled = 0;
        }

        this.normalizedZero = validateAndNormalize(ZERO);
    }

    public BigDecimal validateAndNormalize(BigDecimal value) {
        if (value == null) {
            if (nullZeroMode == NO_NULLS) {
                throw new IllegalArgumentException("null " + propertyName);
            } else {
                return null;
            }
        }

        if (nullZeroMode == ZERO_TO_NULL && value.compareTo(ZERO) == 0)
            return null;

        BigDecimal normalized;
        try {
            normalized = value.setScale(scale, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(propertyName + " (" + value + ") has too many decimals");
        }

        if (normalized.compareTo(minimum) < 0 || normalized.compareTo(maximum) > 0)
            throw new IllegalArgumentException(propertyName + " (" + value + ") is out of range " + minimum + " .. " + maximum);

        return normalized;
    }

    public BigDecimal validateAndConvert(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("NaN " + propertyName);

        if (nullZeroMode == ZERO_TO_NULL && value == 0.0)
            return null;

        long unscaled = Math.round(value * scaleMultiplier);

        if (unscaled < minUnscaled || unscaled > maxUnscaled)
            throw new IllegalArgumentException(propertyName + " (" + value + ") is out of range " + minimum + " .. " + maximum);

        return BigDecimal.valueOf(unscaled, scale);
    }

    public BigDecimal validateAndConvert(Double value) {
        if (value == null) {
            if (nullZeroMode == NO_NULLS) {
                throw new IllegalArgumentException("null " + propertyName);
            } else {
                return null;
            }
        }

        return validateAndConvert(value.doubleValue());
    }

    public BigDecimal getNormalizedZero() {
        return normalizedZero;
    }

    static long powerOfTen(int exponent) {
        long result = 1;
        while (exponent-->0)
            result *= 10;
        return result;
    }
}
