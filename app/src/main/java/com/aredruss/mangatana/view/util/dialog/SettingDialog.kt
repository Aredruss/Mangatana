package com.aredruss.mangatana.view.util.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.DialogChoiceBinding
import com.aredruss.mangatana.view.extensions.setIconText

class SettingDialog<T : Choices>(
    private val currentSetting: Int,
    private val choices: Array<T>,
    private val action: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogChoiceBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChoiceBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(requireActivity()).setView(binding.root)
        binding.apply {
            choices.forEach {
                statusRg.addView(
                    RadioButton(
                        context,
                        null,
                        R.attr.radioButtonStyle
                    ).apply {
                        setIconText(it.icon, it.text)
                    }
                )
            }

            statusRg.apply {
                val preselected = statusRg.getChildAt(currentSetting)
                if (preselected != null) check((preselected as RadioButton).id)

                setOnCheckedChangeListener { _, checkedId ->
                    postDelayed(
                        {
                            when (checkedId) {
                                statusRg[0].id -> action(0)
                                else -> action(1)
                            }
                            dismiss()
                        },
                        DELAY_DURATION
                    )
                }
            }
        }
        return dialog.create()
    }

    companion object {
        private const val DELAY_DURATION = 250L
    }
}

interface Choices {
    val text: Int
    val icon: Int
}

enum class THEME(override val text: Int, override val icon: Int) : Choices {
    DARK(R.string.settings_color_chocolate, R.drawable.ic_night),
    LIGHT(R.string.settings_color_vanilla, R.drawable.ic_day),
}
