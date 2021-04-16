package com.aredruss.mangatana.view.util

import androidx.fragment.app.Fragment
import com.aredruss.mangatana.App

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val modo = App.INSTANCE.modo

    abstract fun setupViews()

}
