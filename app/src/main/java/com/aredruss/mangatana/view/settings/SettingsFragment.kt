package com.aredruss.mangatana.view.settings

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentSettingsBinding
import com.aredruss.mangatana.view.extensions.setBarTitle
import com.aredruss.mangatana.view.util.BaseFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setBarTitle(R.string.fr_settings)
        with(binding) {
            settingsClearBtn.setOnClickListener {
                viewModel.clearDatabase()
            }
        }
    }

    companion object {
        fun create() = SettingsFragment()
    }
}
