package com.timmy.atenvoice.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Stack
import javax.inject.Inject

/**
 * 頁面流程的ViewModel，主要用於切頁、換頁、儲存頁面LiveData資料等工作。
 * */
@HiltViewModel
class PageViewModel @Inject constructor(
) : ViewModel() {

    // 頁面切換邏輯
    private val _switchFragmentLiveData = MutableLiveData<Pair<Fragment, Boolean>>()
    val switchFragmentLiveData: LiveData<Pair<Fragment, Boolean>> = _switchFragmentLiveData

    private val _removeFragmentLiveData = MutableLiveData<Fragment>()
    val removeFragmentLiveData: LiveData<Fragment> = _removeFragmentLiveData

    private val _removeAllFragmentLiveData = MutableLiveData<Int>()
    val removeAllFragmentLiveData: LiveData<Int> = _removeAllFragmentLiveData

    // Only For 主餐單頁面
//    private val _switchClassLiveData = MutableLiveData<Int>()
//    val switchClassLiveData: LiveData<Int> = _switchClassLiveData
//
//    fun setClassSelectPosition(defaultSelect: Int) {
//        _switchClassLiveData.postValue(defaultSelect)
//    }

    // 上一頁的儲存庫
    private val fragmentStack = Stack<Fragment>()

    fun viewModelSwitchFragment(switchFragment: Fragment, needReplace: Boolean = false) { // 點餐環節的時候，needReplace要為false，讓其可以回到menu主頁時可以有記憶效果。
        fragmentStack.push(switchFragment)
        _switchFragmentLiveData.postValue(Pair(switchFragment, needReplace))
    }

    // 用於返回起始頁
//    fun returnToWelcomePage() {  // 回到歡迎頁，要刪除所有Fragment的stack。
//        clearStack()
//        _removeAllFragmentLiveData.postValue(1) // 先清空Fragment
//        WelcomePageFragment().let {
//            fragmentStack.push(it)
//            _switchFragmentLiveData.postValue(Pair(it, true))
//        }
//    }


    // 返回上一頁
    fun navigateBack() {
        if (fragmentStack.size > 1) {
            fragmentStack.pop() // 移除當前頁面
            _switchFragmentLiveData.postValue(Pair(fragmentStack.peek(), false)) // 返回上一頁
        }
    }

    // 結束此頁面
    fun finish(fragment: Fragment) {
        if (fragmentStack.size > 1) {
            fragmentStack.pop() // 移除當前頁面
            _removeFragmentLiveData.postValue(fragment) //  要於fragmentManager中移除此頁面
            _switchFragmentLiveData.postValue(Pair(fragmentStack.peek(), false)) // 返回上一頁
        }
    }

}
