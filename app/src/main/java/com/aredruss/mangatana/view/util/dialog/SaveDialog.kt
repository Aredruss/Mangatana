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
import com.aredruss.mangatana.databinding.DialogSaveBinding

class SaveDialog(
    private val currentStatus: Int,
    private val saveAction: (Int) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogSaveBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSaveBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity()).setView(binding.root)
        binding.apply {
            statusRg.apply {
                check(
                    when (currentStatus) {
                        MediaDb.ONGOING_STATUS -> statusRg[0].id
                        MediaDb.BACKLOG_STATUS -> statusRg[1].id
                        MediaDb.FINISHED_STATUS -> statusRg[2].id
                        else -> 0
                    }
                )
                setOnCheckedChangeListener { group, checkedId ->
                    when (group.findViewById<RadioButton>(checkedId).id) {
                        R.id.progress_rb -> saveAction(MediaDb.ONGOING_STATUS)
                        R.id.backlog_rb -> saveAction(MediaDb.BACKLOG_STATUS)
                        R.id.finished_rb -> saveAction(MediaDb.FINISHED_STATUS)
                    }

                    postDelayed({ this@SaveDialog.dismiss() }, DELAY_DURATION)
                }
            }
        }
        return dialog.create()
    }

    companion object {
        private const val DELAY_DURATION = 250L
    }
}
