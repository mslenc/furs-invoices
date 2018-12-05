package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NumberingStructure {
    CENTRALLY("C"),
    PER_REGISTER("B");

    private final String encoded;

    NumberingStructure(String encoded) {
        this.encoded = encoded;
    }

    @JsonValue
    public String getEncodedForm() {
        return encoded;
    }

    @JsonCreator
    public static NumberingStructure fromEncodedForm(String encoded) {
        if (encoded == null)
            return null;

        switch (encoded) {
            case "C": return CENTRALLY;
            case "B": return PER_REGISTER;
        }

        throw new IllegalArgumentException("Unrecognized encoded form \"" + encoded + "\" of NumberingStructure");
    }
}
