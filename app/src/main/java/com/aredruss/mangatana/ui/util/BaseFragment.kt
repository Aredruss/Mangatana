package com.aredruss.mangatana.ui.util

import androidx.fragment.app.Fragment
import com.aredruss.mangatana.App
import com.aredruss.mangatana.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    protected val modo = App.INSTANCE.modo
    protected val viewModel: MainViewModel by sharedViewModel()

    // abstract fun onLoading()

    //abstract fun onLoaded()

    // abstract fun onError(e: Throwable)
}