package com.aredruss.mangatana.view.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.aredruss.mangatana.R

object DialogHelper {
    fun <T : Any> buildConfirmDialog(
        context: Context,
        title: Int,
        message: Int,
        argument: T,
        action: (T) -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(title))
            .setMessage(context.getString(message))
            .setPositiveButton(R.string.dialog_ok) { _, _ ->
                action(argument)
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }
}
