package com.github.mslenc.furslib.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class FursResponse(
    @JsonProperty("EchoResponse")
    val echoResponse: String? = null,

    @JsonProperty("InvoiceResponse")
    val invoiceResponse: InvoiceResponse? = null
)