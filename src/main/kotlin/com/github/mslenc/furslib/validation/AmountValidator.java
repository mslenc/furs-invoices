package com.github.mslenc.furslib.validation;

public class AmountValidator extends DecimalValidator {
    public AmountValidator(String propertyName, NullZeroMode nullZeroMode) {
        super(propertyName, 16, 2, true, nullZeroMode);
    }
}
