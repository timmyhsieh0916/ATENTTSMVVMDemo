package com.timmy.atenvoice.viewmodel

import androidx.lifecycle.ViewModel
import com.timmy.featureatenttslibs.repo.TTSRepository
import com.timmy.featureatenttslibs.repo.model.TTSResponseBody
import com.timmymike.logtool.loge
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

/**
 * 處理資料的ViewModel，主要用於登入、取資料等工作
 * */

@HiltViewModel
class DataViewModel @Inject constructor(
    val ttsRepo: TTSRepository
) : ViewModel() {

    fun synthesize(text: String, voiceName: String, langType: String) {
        val call = ttsRepo.synthesizeSpeech(text, voiceName, langType)
        call.enqueue(object : retrofit2.Callback<TTSResponseBody> {
            override fun onResponse(call: Call<TTSResponseBody>, response: retrofit2.Response<TTSResponseBody>) {
                loge("成功回傳的TTS網址是=>${response.body()?.synthesisPath}")

                // 處理回應
            }

            override fun onFailure(call: Call<TTSResponseBody>, t: Throwable) {
                // 處理失敗
            }
        })
    }

}
