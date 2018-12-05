package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MobilePremiseType {
    MOBILE_OBJECT("A"),
    FIXED_OBJECT("B"),
    INDIVIDUAL_DEVICE("C");

    private final String encoded;

    MobilePremiseType(String encoded) {
        this.encoded = encoded;
    }

    @JsonValue
    public String getEncodedForm() {
        return encoded;
    }

    @JsonCreator
    public static MobilePremiseType fromEncodedForm(String encoded) {
        if (encoded == null)
            return null;

        switch (encoded) {
            case "A": return MOBILE_OBJECT;
            case "C": return FIXED_OBJECT;
            case "B": return INDIVIDUAL_DEVICE;
        }

        throw new IllegalArgumentException("Unrecognized encoded form \"" + encoded + "\" of MobilePremiseType");
    }
}
