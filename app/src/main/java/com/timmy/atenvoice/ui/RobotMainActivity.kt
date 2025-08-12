package com.timmy.atenvoice.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.timmy.atenvoice.R
import com.timmy.atenvoice.databinding.ActivityRobotMainBinding
import com.timmy.atenvoice.viewmodel.DataViewModel
import com.timmy.atenvoice.viewmodel.PageViewModel
import com.timmymike.componenttool.BaseToolBarActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RobotMainActivity : BaseToolBarActivity<ActivityRobotMainBinding>() {
    private val dataViewModel: DataViewModel by viewModels()
    private val pageViewModel: PageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarVisible(false)

        initEvent()

        initRobotSDK()

        initView()

        initObservable()

        pageViewModel.viewModelSwitchFragment(ATENTTSFragment())
    }

    private fun initView() = binding.run {


    }

    private fun initObservable() {

        pageViewModel.switchFragmentLiveData.observe(this@RobotMainActivity) {
            switchFragment(it.first, it.second)
        }

        pageViewModel.removeFragmentLiveData.observe(this@RobotMainActivity) {
            removeFragment(it)
        }

        pageViewModel.removeAllFragmentLiveData.observe(this@RobotMainActivity) {
            if (it == 1) {
                clearFragmentStack()
            }
        }

    }

    private fun initEvent() = binding.run {
    }


    private fun initRobotSDK() {
//        dataViewModel.getData()
    }

    @SuppressLint("CommitTransaction")
    fun switchFragment(fragment: Fragment, needReplace: Boolean = false) {
//        logWtf("即將switchTo ${fragment},此時的needReplace是=>${needReplace}")
        val transaction = supportFragmentManager.beginTransaction()
        if (needReplace) {
            transaction.replace(R.id.container_content, fragment, fragment.javaClass.name).commit()
        } else {
            // 隱藏當前 Fragment
            supportFragmentManager.findFragmentById(R.id.container_content)?.let {
                if (judgeIsNeedHideFragment(it)) transaction.hide(it) else transaction.remove(it)
            }

            supportFragmentManager.findFragmentByTag(fragment.javaClass.name)?.let {
                // 顯示已存在的 Fragment
                transaction.show(it)
            } ?: run {
                // 新增 Fragment
                transaction.add(R.id.container_content, fragment, fragment.javaClass.name)
            }
            transaction.commitAllowingStateLoss()
        }

    }

    /** 是否是只要Hide，不完全移除，要保留在 supportFragmentManager 中，所使用的邏輯。*/
    private fun judgeIsNeedHideFragment(fragment: Fragment): Boolean = listOf(
        ATENTTSFragment::class
    ).any { fragment::class == it }

    private fun removeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        // 移除傳入的 Fragment
        supportFragmentManager.findFragmentById(R.id.container_content)?.let {
            transaction.remove(fragment)
        }

        transaction.commit()

    }

    @SuppressLint("CommitTransaction")
    fun clearFragmentStack() {
        supportFragmentManager.run {
            fragments.forEach {
                beginTransaction().remove(it)
            }
            beginTransaction().commit()
        }
    }

}