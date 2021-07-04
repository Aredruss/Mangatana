package com.aredruss.mangatana.view.util.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.DialogChoiceBinding
import com.aredruss.mangatana.view.extensions.getString
import com.aredruss.mangatana.view.extensions.setIconText

class SaveDialog(
    private val currentStatus: Int,
    private val saveAction: (Int) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogChoiceBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChoiceBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity()).setView(binding.root)

        binding.apply {
            STATUS.values().forEach {
                statusRg.addView(
                    RadioButton(
                        context,
                        null,
                        R.attr.radioButtonStyle
                    ).apply {
                        setIconText(it.icon, binding.getString(it.text))
                    }
                )
            }

            statusRg.apply {
                check(
                    when (currentStatus) {
                        MediaDb.ONGOING_STATUS -> statusRg[0].id
                        MediaDb.BACKLOG_STATUS -> statusRg[1].id
                        MediaDb.FINISHED_STATUS -> statusRg[2].id
                        else -> 0
                    }
                )
                setOnCheckedChangeListener { _, checkedId ->
                    when (checkedId) {
                        statusRg.id -> saveAction(MediaDb.ONGOING_STATUS)
                        statusRg.id -> saveAction(MediaDb.BACKLOG_STATUS)
                        statusRg.id -> saveAction(MediaDb.FINISHED_STATUS)
                    }

                    postDelayed({ this@SaveDialog.dismiss() }, DELAY_DURATION)
                }
            }
        }
        return dialog.create()
    }

    companion object {
        private const val DELAY_DURATION = 250L
        const val SAVE_DIALOG_TAG = "save_dialog"
    }
}

enum class STATUS(val text: Int, val icon: Int) {
    ONGOING(R.string.status_ongoing, R.drawable.ic_progress),
    BACKLOG(R.string.status_backlog, R.drawable.ic_backlog),
    FINISHED(R.string.status_finished, R.drawable.ic_finished)
}
