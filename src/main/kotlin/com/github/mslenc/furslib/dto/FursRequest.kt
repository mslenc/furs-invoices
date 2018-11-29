package com.github.mslenc.furslib.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class FursRequest(
    @JsonProperty("EchoRequest")
    val echoRequest: String? = null,

    @JsonProperty("InvoiceRequest")
    val invoiceRequest: InvoiceRequest? = null
)