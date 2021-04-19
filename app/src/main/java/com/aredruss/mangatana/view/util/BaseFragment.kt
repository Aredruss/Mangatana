package com.aredruss.mangatana.view.util

import androidx.fragment.app.Fragment
import com.aredruss.mangatana.App
import com.aredruss.mangatana.view.extensions.isOnline

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val modo = App.INSTANCE.modo

    abstract fun setupViews()

    protected fun isOnline(): Boolean {
        return activity?.isOnline() == true
    }
}
