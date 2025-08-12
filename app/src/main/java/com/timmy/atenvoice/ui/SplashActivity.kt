package com.timmy.atenvoice.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.timmy.atenvoice.databinding.ActivitySplashBinding
import com.timmymike.componenttool.BaseToolBarActivity
import com.timmymike.timetool.TimeUnits
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.run


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseToolBarActivity<ActivitySplashBinding>() {

    private val splashDelay = 3 * TimeUnits.oneSec // 固定至少等待三秒

    private val totalWaitCount = 1// 要打的 API 總數量 + 1(加上要等待的秒數)
    // 1.等待 splashDelay 秒數

    private var isCompleted = 0

//    private val dataViewModel: DataViewModel by viewModels()
//
//    private val robotViewModel: RobotAPIViewModel by viewModels()

//    private val businessInfoData by lazy { dataViewModel.getBusinessInfo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarVisible(false)

        initSDK()

        initView()

        initData()

        initObservable()
    }

    private fun initSDK() {
    }

    private fun initObservable() = binding.run {
    }

    private fun initView() = binding.run {

//        businessInfoData.takeIf { it.splashPage.twTitle != "" }?.splashPage?.let { // 不是預設內容，才要執行設定，以避免閃退。
//            root.setBackgroundColor(businessInfoData.colorInfo.pageBackgroundColor.parseColor())
//            tvTitle.setTextColor(businessInfoData.colorInfo.txtColorDark.parseColor())
//
//            ivLogo.insideDataByFid(it.shopIcon.pic, dataViewModel)
//            tvTitle.text = it.twTitle
//            root.resetLayoutText(businessInfoData)
//        } ?: run { root.resetLayoutTextSize() }

    }

    private fun initData() {
//        if (dataViewModel.nowNeedGetAPI() && dataViewModel.loginIsValid()) {
//            dataViewModel.splashInitData()   // 請求Splash頁的所有API
//        } else {
////            logWtf("當前無須撈取API、下載資料，因${if (dataViewModel.loginIsValid()) "非下載時段" else "未登入"}。")
//            isCompleted = totalWaitCount - 1 // 只要等秒數API。
//            checkAndProceed()
//        }
        // 啟動三秒延遲計時
        lifecycleScope.launch {
            delay(splashDelay)
            isCompleted++
            checkAndProceed()
        }
    }

    private fun checkAndProceed() { // 要等到所有的API都完成了以後才要處理下一步動作。
        if (isCompleted >= totalWaitCount) {
            judgeNeedLogin()
        }
    }


    private fun judgeNeedLogin() {
        startMainActivity()
    }

    private fun startMainActivity() {
        gotoActivity(RobotMainActivity::class.java, closeSelf = true)
    }


}