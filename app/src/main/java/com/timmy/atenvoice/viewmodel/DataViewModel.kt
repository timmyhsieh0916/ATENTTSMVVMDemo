package com.timmy.atenvoice.viewmodel

import androidx.lifecycle.ViewModel
import com.timmy.featureatenttslibs.repo.TTSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 處理資料的ViewModel，主要用於登入、取資料等工作
 * */

@HiltViewModel
class DataViewModel @Inject constructor(
    ttsRepo:TTSRepository
) : ViewModel() {

    fun getData() {

        // DataStoreRepository 非同步方案
    }


}
