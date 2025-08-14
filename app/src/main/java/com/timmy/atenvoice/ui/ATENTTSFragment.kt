package com.timmy.atenvoice.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.timmy.atenvoice.R
import com.timmy.atenvoice.databinding.FragmentAtenTtsBinding
import com.timmy.atenvoice.viewmodel.DataViewModel
import com.timmy.atenvoice.viewmodel.PageViewModel
import com.timmymike.componenttool.BaseFragment
import com.timmymike.viewtool.click
import com.timmymike.viewtool.resetLayoutTextSize
import com.timmymike.viewtool.setRippleBackgroundById

class ATENTTSFragment : BaseFragment<FragmentAtenTtsBinding>() {

    private val pageViewModel: PageViewModel by activityViewModels()

    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initEvent()
    }

    private fun initView() = binding.run {
        root.resetLayoutTextSize()
        ivPlay.setRippleBackgroundById(R.color.black)
        ivReplay.setRippleBackgroundById(R.color.black)
        ivStop.setRippleBackgroundById(R.color.black)
//        }
    }

    private fun initEvent() = binding.run {
        ivPlay.click { // call API 下載音頻，然後播放音頻
            dataViewModel.synthesize(
                edtContent.text.toString().takeIf { it.isNotEmpty() }?.toString() ?: edtContent.hint.toString(),
                "Bella_host", "TL"
            )
        }

        ivReplay.click { // 播放已下載好的音頻


        }

        ivStop.click { // 停止播放音頻


        }

    }

}