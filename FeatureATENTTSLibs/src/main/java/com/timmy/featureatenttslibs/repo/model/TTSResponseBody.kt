package com.timmy.featureatenttslibs.repo.model

import com.google.gson.annotations.SerializedName

data class TTSResponseBody(
    @SerializedName("srt_path")
    var srtPath: String? = "",
    @SerializedName("synthesis_id")
    var synthesisId: String? = "",
    @SerializedName("synthesis_path")
    var synthesisPath: String? = "",
    @SerializedName("error_code")
    var errorCode: String? = "",
)


