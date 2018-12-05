package com.github.mslenc.fursinvoices.validation;

import static com.github.mslenc.fursinvoices.validation.StringValidator.CharsAllowed.ANY;
import static com.github.mslenc.fursinvoices.validation.StringValidator.NullEmptyMode.EMPTY_TO_NULL;
import static com.github.mslenc.fursinvoices.validation.StringValidator.NullEmptyMode.NO_NULLS;
import static java.util.Objects.requireNonNull;

public class StringValidator {
    public enum CharsAllowed {
        ANY {
            boolean isValidChar(char c) {
                return true;
            }
        },
        ASCII_ALNUM {
            boolean isValidChar(char c) {
                return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z');
            }
        },
        HEX {
            boolean isValidChar(char c) {
                return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
            }
        },
        VAT_ID_CHARS {
            boolean isValidChar(char c) {
                // TODO - allow hyphens? remove lower-case characters?
                return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z');
            }
        },
        DIGITS_ONLY {
            boolean isValidChar(char c) {
                return c >= '0' && c <= '9';
            }
        };

        abstract boolean isValidChar(char c);
    }

    public enum NullEmptyMode {
        NO_NULLS,
        NULL_ALLOWED,
        EMPTY_TO_NULL
    }

    private final String propertyName;
    private final int minLength;
    private final int maxLength;
    private final CharsAllowed charsAllowed;
    private final NullEmptyMode nullEmptyMode;

    public StringValidator(String propertyName, int minLength, int maxLength, CharsAllowed charsAllowed, NullEmptyMode nullEmptyMode) {
        this.propertyName = requireNonNull(propertyName, "propertyName");
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.charsAllowed = requireNonNull(charsAllowed, "charsAllowed");
        this.nullEmptyMode = requireNonNull(nullEmptyMode, "nullEmptyMode");
    }

    public String validate(String value) {
        if (value == null) {
            if (nullEmptyMode == NO_NULLS) {
                throw new IllegalArgumentException("null " + propertyName);
            } else {
                return null;
            }
        }

        if (nullEmptyMode == EMPTY_TO_NULL && value.isEmpty())
            return null;

        int len = value.length();
        if (len < minLength || len > maxLength)
            throw new IllegalArgumentException("Length of " + propertyName + " must be between " + minLength + " and " + maxLength + ", but is " + len);

        CharsAllowed chars = charsAllowed;
        if (chars == ANY)
            return value;

        for (int i = 0; i < len; i++)
            if (!chars.isValidChar(value.charAt(i)))
                throw new IllegalArgumentException(propertyName + " contains an invalid character (allowed=" + chars + ")");

        return value;
    }
}
