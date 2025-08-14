package com.timmy.atenvoice.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.timmy.atenvoice.R
import com.timmy.atenvoice.databinding.FragmentAtenTtsBinding
import com.timmy.atenvoice.viewmodel.DataViewModel
import com.timmy.atenvoice.viewmodel.PageViewModel
import com.timmy.featureatenttslibs.repo.TTSStatus
import com.timmymike.componenttool.BaseFragment
import com.timmymike.viewtool.click
import com.timmymike.viewtool.resetLayoutTextSize
import com.timmymike.viewtool.setRippleBackgroundById
import java.io.File

class ATENTTSFragment : BaseFragment<FragmentAtenTtsBinding>() {

    private val pageViewModel: PageViewModel by activityViewModels()

    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initEvent()

        initObservable()
    }


    private fun initView() = binding.run {
        root.resetLayoutTextSize()
        ivPlay.setRippleBackgroundById(R.color.black)
        ivReplay.setRippleBackgroundById(R.color.black)
        ivStop.setRippleBackgroundById(R.color.black)

        initSpinnerView()
    }

    private fun initSpinnerView() = binding.run {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_options,
            android.R.layout.simple_spinner_item
        )

        // 指定下拉選項顯示的佈局
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 將適配器應用到 Spinner
        spVoiceSelect.adapter = adapter
    }

    private val mediaPlayer by lazy { MediaPlayer() }

    private fun playAudio(file: File) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(file.absolutePath)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.setOnPreparedListener {
            binding.tvStatus.text = "當前狀態：準備完成，開始播放內容"
        }

        mediaPlayer.setOnCompletionListener {
            binding.tvStatus.text = "當前狀態：播放內容完畢"
            // 可以在這裡處理播放完成的事件，例如: 重置或重新播放
        }
    }

    fun getNowEdtString() = binding.edtContent.text.toString().takeIf { it.isNotEmpty() }?.toString() ?: binding.edtContent.hint.toString()

    private fun initEvent() = binding.run {
        ivPlay.click { // call API 下載音頻，然後播放音頻
            showDialogLoading()
            dataViewModel.sendGetTTSResult(
                getNowEdtString(),
                getVoiceOwner(),
                getLanguage()
            )
        }

        ivReplay.click { // 播放已下載好的音頻
            (dataViewModel.getTTsRepoStatus().value?.first)?.run {
                playAudio(this)
            } ?: showToast(msgStr = "請先至少發送一次要說的內容。")
        }

        ivStop.click { // 停止播放音頻
            mediaPlayer.stop()
            val selectedOption = spVoiceSelect.selectedItem.toString()
            showToast(msgStr = "選擇的語音: $selectedOption")
        }

    }

    private fun getVoiceOwner(): String {
        // 獲取 Spinner 中選擇的項目
        val selectedOption = binding.spVoiceSelect.selectedItem.toString()

        // 根據選擇的語音回傳對應的字串
        return when {
            selectedOption.contains("男聲") -> "Aaron_taigi" // 如果包含 "男聲"，返回 "Aaron_taigi"
            selectedOption.contains("女聲") -> "Bella_host"  // 如果包含 "女聲"，返回 "Bella_host"
            else -> "" // 如果不符合任何條件，返回空字串
        }
    }

    private fun getLanguage(): String {
        // 獲取 Spinner 中選擇的項目
        val selectedOption = binding.spVoiceSelect.selectedItem.toString()

        // 根據選擇的語音回傳對應的字串
        return when {
            selectedOption.contains("中文") -> "TW"
            selectedOption.contains("台語") -> "TL"
            else -> "" // 如果不符合任何條件，返回空字串
        }
    }

    private fun initObservable() {
        dataViewModel.getTTsRepoStatus().observe(viewLifecycleOwner) { (file, status) ->
            if (file != null) {
                playAudio(file)
            }

            binding.tvStatus.text =
                when (status) {
                    TTSStatus.SendAPI -> "當前狀態：雲端AI音效合成中"
                    TTSStatus.GetURL -> "當前狀態：取得AI音效，檔案下載中"
                    TTSStatus.GetUrlFail -> "當前狀態：雲端AI音效合成失敗"
                    TTSStatus.DownloadDataFail -> "當前狀態：合成完畢後的檔案下載失敗，無法播放"
                    TTSStatus.DownloadComplete -> "當前狀態：下載完成，交給媒體播放器準備播放"
                }

            if (status == TTSStatus.DownloadDataFail || status == TTSStatus.DownloadComplete || status == TTSStatus.GetUrlFail) {
                hideDialogLoading()
            }

        }


    }
}