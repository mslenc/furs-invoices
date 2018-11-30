package com.github.mslenc.furslib.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class InvoiceResponse(
    @JsonProperty("Header")
    val header: FursHeader,

    @JsonProperty("UniqueInvoiceID")
    val uniqueInvoiceId: UUID? = null,

    @JsonProperty("Error")
    val error: FursError? = null
)