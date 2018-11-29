package com.github.mslenc.furslib.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class FursError(
    @JsonProperty("ErrorCode")
    val errorCode: String? = null,

    @JsonProperty("ErrorMessage")
    val errorMessage: String? = null
) {
    @JsonIgnore
    fun toException(defaultMessage: String): FursException {
        throw FursException(errorCode ?: "????", errorMessage ?: defaultMessage)
    }
}
