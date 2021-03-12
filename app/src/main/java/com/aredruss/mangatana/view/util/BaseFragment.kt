package com.aredruss.mangatana.view.util

import androidx.fragment.app.Fragment
import com.aredruss.mangatana.App
import com.aredruss.mangatana.view.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val modo = App.INSTANCE.modo
    protected val viewModel: MainViewModel by sharedViewModel()

    abstract fun onLoading()

    abstract fun onError(e: Throwable)
}
