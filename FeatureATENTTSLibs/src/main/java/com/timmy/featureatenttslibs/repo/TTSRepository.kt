package com.timmy.featureatenttslibs.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.timmy.featureatenttslibs.repo.api.TtsService
import com.timmy.featureatenttslibs.repo.model.TTSResponseBody
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class TTSRepository @Inject constructor(
    private val context: Context,
    @Named("TtsApi") private val retrofit: Retrofit,
    @Named("TtsApi") private val okHttpClient: OkHttpClient
) {
    private val _TTSStatus = MutableLiveData<Pair<File?, TTSStatus>>()

    val ttsStatus: LiveData<Pair<File?, TTSStatus>> = _TTSStatus

    private val ttsService by lazy {
        retrofit.create(TtsService::class.java)
    }

    private fun synthesizeSpeech(text: String, voiceName: String, langType: String): retrofit2.Call<TTSResponseBody> {
        _TTSStatus.postValue(Pair(null, TTSStatus.SendAPI))
        val ssml = createSSML(text, voiceName, langType)
        val request = SynthesisRequest(ssml)
        return ttsService.synthesizeSpeech(request)
    }

    fun synthesize(text: String, voiceName: String, langType: String) {
        val call = synthesizeSpeech(text, voiceName, langType)
        call.enqueue(object : retrofit2.Callback<TTSResponseBody> {
            override fun onResponse(call: retrofit2.Call<TTSResponseBody>, response: retrofit2.Response<TTSResponseBody>) {
                _TTSStatus.postValue(Pair(null, TTSStatus.GetURL))
                downloadAudioFile(response.body()?.synthesisPath ?: return)
                // 處理回應
            }

            override fun onFailure(call: retrofit2.Call<TTSResponseBody>, t: Throwable) {
                _TTSStatus.postValue(Pair(null, TTSStatus.GetUrlFail))
                // 處理失敗
            }
        })
    }

    fun String.getFileName(): String = "${this.split("/").last()}.wav" // 加上 .wav 副檔名


    // 用於下載音頻檔案
    fun downloadAudioFile(url: String) {
        val file = File(context.getExternalFilesDir(null), url.getFileName())

//        // 如果檔案已存在，就直接返回
//        if (file.exists()) {
//            onComplete(file)
//            return
//        }

        // 使用 OkHttp 下載wav音頻
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                _TTSStatus.postValue(Pair(null, TTSStatus.DownloadDataFail))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body?.let { responseBody ->
                        FileOutputStream(file).use { output ->
                            output.write(responseBody.bytes())
                        }
                        _TTSStatus.postValue(Pair(file, TTSStatus.DownloadComplete))
                    } ?: _TTSStatus.postValue(Pair(null, TTSStatus.DownloadDataFail))
                }
            }
        })
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

enum class TTSStatus {
    SendAPI,
    GetURL,
    DownloadComplete,
    GetUrlFail,
    DownloadDataFail
}

enum class VoiceType {
    ManTW,
    ManTL,
    WomenTW,
    WomenTL
}