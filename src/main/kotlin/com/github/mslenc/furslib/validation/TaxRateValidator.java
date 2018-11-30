package com.github.mslenc.furslib.validation;

public class TaxRateValidator extends DecimalValidator {
    public TaxRateValidator(String propertyName, NullZeroMode nullZeroMode) {
        super(propertyName, 7, 2, true, nullZeroMode);
    }
}
