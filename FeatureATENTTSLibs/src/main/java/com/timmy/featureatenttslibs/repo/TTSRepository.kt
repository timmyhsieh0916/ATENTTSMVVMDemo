package com.timmy.featureatenttslibs.repo

import android.content.Context
import com.timmy.featureatenttslibs.repo.api.TtsService
import com.timmy.featureatenttslibs.repo.model.TTSResponseBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class TTSRepository@Inject constructor(
    private val context: Context,
    @Named("TtsApi") private val retrofit: Retrofit,
    @Named("TtsApi") private val okHttpClient: OkHttpClient
) {
    private val ttsService by lazy {
        retrofit.create(TtsService::class.java)
    }

    fun synthesizeSpeech(text: String, voiceName: String, langType: String): Call<TTSResponseBody> {
        val ssml = createSSML(text, voiceName, langType)
        val request = SynthesisRequest(ssml)
        return ttsService.synthesizeSpeech(request)
    }

    private fun createSSML(text: String, voiceName: String, langType: String): String {
        return "<speak xmlns='http://www.w3.org/2001/10/synthesis' version='1.5' xml:lang='zh-TW'>" +
                "<voice name='$voiceName'>" +
                "<lang lang_type='$langType'>$text</lang>" +
                "</voice></speak>"
    }


}


data class SynthesisRequest(
    val ssml: String
)

enum class VoiceType {
    ManTW,
    ManTL,
    WomenTW,
    WomenTL
}