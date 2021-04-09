package com.aredruss.mangatana.view.util

import androidx.fragment.app.Fragment
import com.aredruss.mangatana.App
import com.aredruss.mangatana.view.extensions.backButtonVisible
import com.aredruss.mangatana.view.extensions.menuItemsVisible
import com.aredruss.mangatana.view.extensions.setBarTitle

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val modo = App.INSTANCE.modo

    fun setupFragment(titleRes: Int, showBackButton: Boolean, showMenu: Boolean) {
        activity?.setBarTitle(titleRes)
        activity?.menuItemsVisible(showMenu)
        activity?.backButtonVisible(showBackButton)
    }
}
