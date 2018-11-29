package com.github.mslenc.furslib.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigInteger

data class JwsHeader(
    @JsonProperty("alg")
    val alg: String = "RS256",

    @JsonProperty("subject_name")
    val subjectName: String,

    @JsonProperty("issuer_name")
    val issuerName: String,

    @JsonProperty("serial")
    val serial: BigInteger
)