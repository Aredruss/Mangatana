package com.aredruss.mangatana.view.settings

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentSettingsBinding
import com.aredruss.mangatana.view.media.list.MediaListViewModel
import com.aredruss.mangatana.view.util.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewModel: MediaListViewModel by sharedViewModel()
    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setupFragment(
            titleRes = R.string.fr_settings,
            showBackButton = true,
            showMenu = false
        )
        with(binding) {
            clearBtn.setOnClickListener {
                viewModel.clearDatabase()
            }
        }
    }

    companion object {
        fun create() = SettingsFragment()
    }
}
