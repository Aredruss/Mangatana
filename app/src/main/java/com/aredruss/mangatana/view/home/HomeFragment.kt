package com.aredruss.mangatana.view.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.view.extensions.setBarTitle
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.ScreenCategory
import com.github.terrakok.modo.forward

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setBarTitle(R.string.app_name)

        with(binding) {
            inProgressBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.ON_GOING)
            }
            upNextBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.BACKLOG)
            }
            finishedBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.FINISHED)
            }
            starredBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.STARRED)
            }
            exploreBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.EXPLORE)
            }
        }
    }

    private fun openMediaFragment(type: Int) {
        modo.forward(Screens.MediaList(type))
    }

    companion object {
        fun create() = HomeFragment()
    }
}
