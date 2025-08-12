package com.timmy.featureatenttslibs.repo

import com.timmy.featureatenttslibs.repo.api.TtsService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class TTSRepository@Inject constructor(
    @Named("TtsApi") retrofit: Retrofit
) {
    private val ttsService by lazy {
        retrofit.create(TtsService::class.java)
    }



}