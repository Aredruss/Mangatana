package com.aredruss.mangatana.view.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.ScreenCategory
import com.github.terrakok.modo.forward
import timber.log.Timber

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

    override fun onLoading() {
        Timber.e("Nothing to load yet")
    }

    override fun onError(e: Throwable) {
        Timber.e(e)
    }

    private fun openMediaFragment(type: Int, fragmentTitle: Int) {
        activity?.title = getString(fragmentTitle)
        modo.forward(Screens.MediaList(type))
    }
}
