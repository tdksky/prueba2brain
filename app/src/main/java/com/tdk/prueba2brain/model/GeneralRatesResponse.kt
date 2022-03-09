package com.tdk.prueba2brain.model

import com.google.gson.annotations.SerializedName

data class GeneralRatesResponse(
    @SerializedName("success")val success: Boolean,
    @SerializedName("timestamp")val timestamp: Long,
    @SerializedName("base")val base: String,
    @SerializedName("date")val date: String,
    @SerializedName("rates") val rates: Map<String, Double>
)

