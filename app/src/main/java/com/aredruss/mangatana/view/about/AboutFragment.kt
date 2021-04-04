package com.aredruss.mangatana.view.about

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.BuildConfig
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentAboutBinding
import com.aredruss.mangatana.view.util.BaseFragment

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    private val binding: FragmentAboutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setupFragment(
            titleRes = R.string.fr_about_app,
            showBackButton = true,
            showMenu = false
        )
        with(binding) {
            versionTv.text = BuildConfig.VERSION_NAME
        }
    }

    companion object {
        fun create() = AboutFragment()
    }
}
