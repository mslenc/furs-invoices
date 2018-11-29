package com.github.mslenc.furslib.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class InvoiceResponse(
    @JsonProperty("Header")
    val header: FursHeader,

    @JsonProperty("UniqueInvoiceID")
    val uniqueInvoiceId: String? = null,

    @JsonProperty("Error")
    val error: FursError? = null
)