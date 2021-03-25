package com.aredruss.mangatana.view.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.ScreenCategory
import com.github.terrakok.modo.forward
import java.util.*

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        fun create() = HomeFragment()
    }

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.app_name)

        with(binding) {
            inProgressBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.ON_GOING, R.string.fr_ongoing_title)
            }
            upNextBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.BACKLOG, R.string.fr_backlog_title)
            }
            finishedBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.FINISHED, R.string.fr_finished_title)
            }
            starredBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.STARRED, R.string.fr_starred_title)
            }
            exploreBtn.setOnClickListener {
                openMediaFragment(ScreenCategory.EXPLORE, R.string.fr_explore_title)
            }
        }
    }

    private fun openMediaFragment(type: Int, fragmentTitle: Int) {
        activity?.title = getString(fragmentTitle)
        modo.forward(Screens.MediaList(type))
    }
}
