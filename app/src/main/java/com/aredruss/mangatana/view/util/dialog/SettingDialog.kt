package com.aredruss.mangatana.view.util.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.aredruss.mangatana.databinding.DialogChoiceBinding

class SettingDialog(
    private val currentSetting: Int,
    private val saveAction: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogChoiceBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChoiceBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity()).setView(binding.root)
        binding.apply {
            statusRg.apply {

                postDelayed({ this@SettingDialog.dismiss() }, DELAY_DURATION)
            }
        }
        return dialog.create()
    }

    companion object {
        private const val DELAY_DURATION = 250L
    }
}