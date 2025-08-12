package com.timmy.base.data

import com.google.gson.annotations.SerializedName

data class SampleDataFromAPI(
    @SerializedName("__extras")
    val extras: Extras = Extras(),
    @SerializedName("fields")
    val fields: List<Field> = listOf(),
    @SerializedName("include_total")
    val includeTotal: Boolean = false,
    @SerializedName("limit")
    val limit: String = "",
    @SerializedName("records")
    val records: List<Record> = listOf(),
    @SerializedName("resource_format")
    val resourceFormat: String = "",
    @SerializedName("resource_id")
    val resourceId: String = "",
    @SerializedName("total")
    val total: String = ""
)

data class Extras(
    @SerializedName("api_key")
    val apiKey: String = ""
)

data class Field(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("info")
    val info: Info = Info(),
    @SerializedName("type")
    val type: String = ""
)

data class Record(
    @SerializedName("site")
    val site: String = "",
    @SerializedName("county")
    val county: String = "",
    @SerializedName("pm25")
    val pm25: String = "",
    @SerializedName("datacreationdate")
    val datacreationdate: String = "",
    @SerializedName("itemunit")
    val itemunit: String = ""

)

data class Info(
    @SerializedName("label")
    val label: String = ""
)
