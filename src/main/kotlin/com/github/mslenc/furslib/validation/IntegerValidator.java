package com.github.mslenc.furslib.validation;

public class IntegerValidator {
    private final String propertyName;
    private final int minimum;
    private final int maximum;

    public IntegerValidator(String propertyName, int minimum, int maximum) {
        this.propertyName = propertyName;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Integer validate(int value) {
        if (value < minimum || value > maximum)
            throw new IllegalArgumentException(propertyName + " (" + value + ") out of range " + minimum + " .. " + maximum);

        return value;
    }
}
