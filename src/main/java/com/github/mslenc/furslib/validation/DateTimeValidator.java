package com.github.mslenc.furslib.validation;

import com.github.mslenc.furslib.FursEnv;

import java.time.Instant;
import java.time.LocalDateTime;

public class DateTimeValidator {
    private final String propertyName;

    public DateTimeValidator(String propertyName) {
        this.propertyName = propertyName;
    }

    public LocalDateTime validate(LocalDateTime dateTime) {
        if (dateTime == null)
            throw new IllegalArgumentException("null " + propertyName);

        if (dateTime.getNano() != 0) {
            return dateTime.minusNanos(dateTime.getNano());
        } else {
            return dateTime;
        }
    }

    public LocalDateTime validateAndConvert(Instant instant) {
        if (instant == null)
            throw new IllegalArgumentException("null " + propertyName);

        return validate(LocalDateTime.ofInstant(instant, FursEnv.EUROPE_LJUBLJANA));
    }
}
