package com.aredruss.mangatana.view.util

import androidx.fragment.app.Fragment
import com.aredruss.mangatana.App
import com.aredruss.mangatana.view.MainViewModel
import com.aredruss.mangatana.view.extensions.backButtonVisible
import com.aredruss.mangatana.view.extensions.menuItemsVisible
import com.aredruss.mangatana.view.extensions.setBarTitle
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val modo = App.INSTANCE.modo
    protected val viewModel: MainViewModel by sharedViewModel()

    fun setupFragment(titleRes: Int, showBackButton: Boolean, showMenu: Boolean) {
        activity?.setBarTitle(titleRes)
        activity?.menuItemsVisible(showMenu)
        activity?.backButtonVisible(showBackButton)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
    }
}
