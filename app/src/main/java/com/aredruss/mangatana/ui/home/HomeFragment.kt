package com.aredruss.mangatana.ui.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.ui.util.BaseFragment
import com.aredruss.mangatana.ui.util.ScreenCategory
import com.github.terrakok.modo.forward

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.app_name)

        with(binding) {
            inProgressBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.IN_PROGRESS, R.string.fr_in_progress)
            }
            upNextBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.BACKLOG, R.string.fr_backlog)
            }
            finishedBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.FINISHED, R.string.fr_finished)
            }
            starredBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.STARRED, R.string.fr_starred)
            }
            exploreBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.EXPLORE, R.string.fr_explore)
            }

            clearBtn.setOnClickListener {
                viewModel.clearDatabase()
            }
        }
    }

    private fun openMediaFragment(type: Int, fragmentTitle: Int) {
        activity?.title = getString(fragmentTitle)
        modo.forward(Screens.MediaList(type))
    }

    companion object {
        fun create() = HomeFragment()
    }

}