package com.aredruss.mangatana.view.settings

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentSettingsBinding
import com.aredruss.mangatana.view.extensions.context
import com.aredruss.mangatana.view.media.list.MediaListViewModel
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.DialogHelper
import com.github.terrakok.modo.back
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewModel: MediaListViewModel by sharedViewModel()
    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun setupViews() = with(binding) {
        backBtn.setOnClickListener {
            modo.back()
        }
        clearBtn.setOnClickListener {
            DialogHelper.buildConfirmDialog(
                context = this.context(),
                title = R.string.dialog_clear,
                message = R.string.dialog_clear_message,
                argument = Unit,
                action = this@SettingsFragment::clearDatabase
            )
        }
    }

    @Suppress("UnusedPrivateMember")
    private fun clearDatabase(unit: Unit) {
        viewModel.clearDatabase()
    }

    companion object {
        fun create() = SettingsFragment()
    }
}
