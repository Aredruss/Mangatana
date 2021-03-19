package com.aredruss.mangatana.view.about

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.forEach
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.BuildConfig
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentAboutBinding
import com.aredruss.mangatana.view.util.BaseFragment

class AboutFragment : BaseFragment(R.layout.fragment_about) {
    companion object {
        fun create() = AboutFragment()
    }

    private val binding: FragmentAboutBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            aboutVersionTv.text = BuildConfig.VERSION_NAME
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        activity?.invalidateOptionsMenu()
        menu.findItem(R.id.action_about).setVisible(false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.forEach {
            it.isVisible = false
        }
    }
}
