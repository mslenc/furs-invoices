package com.github.mslenc.furslib.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.FursEnv;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Message header.
 */
public class FursHeader {
    private UUID messageId;
    private LocalDateTime dateTime;

    public FursHeader(UUID messageId, LocalDateTime dateTime) {
        setMessageId(messageId);
        setDateTime(dateTime);
    }

    /**
     * Creates a header with a new message ID (random UUID) and the current time.
     */
    public FursHeader() {
        this(UUID.randomUUID(), LocalDateTime.now(FursEnv.Companion.getEUROPE_LJUBLJANA()));
    }

    /**
     * @see #setMessageId(UUID)
     */
    @JsonProperty("MessageID")
    public UUID getMessageId() {
        return messageId;
    }

    /**
     * Sets the identifier of the message.
     * FURS says:
     * <blockquote>
     *     Unique identifier of the message.
     *     Every message shall have the unique identification mark. The same is
     *     obligatory also at sending of the message, which is resent due to an
     *     error.
     * </blockquote>
     *
     * @param messageId the new value (must not be null)
     * @return this, for fluent interface
     */
    @JsonProperty("MessageID")
    public FursHeader setMessageId(UUID messageId) {
        if (messageId == null)
            throw new IllegalArgumentException("Null messageId");

        this.messageId = messageId;
        return this;
    }

    /**
     * @see #setDateTime(LocalDateTime)
     */
    @JsonProperty("DateTime")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date/time of sending, which should probably be in Europe/Ljubljana time zone.
     * FURS says:
     * <blockquote>
     *      Date and time of sending the message
     * </blockquote>
     * @param dateTime the new value (must not be null)
     * @return this, for fluent interface
     */
    @JsonProperty("DateTime")
    public FursHeader setDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            throw new IllegalArgumentException("Null dateTime");

        this.dateTime = dateTime;
        return this;
    }
}
