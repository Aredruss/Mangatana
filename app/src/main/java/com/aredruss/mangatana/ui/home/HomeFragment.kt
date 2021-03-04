package com.aredruss.mangatana.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.ui.MainViewModel
import com.aredruss.mangatana.ui.util.ScreenType
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by viewModel()

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.app_name)

        with(binding) {
            inProgressBtn.setOnClickListener {
                openMediaFragment(ScreenType.IN_PROGRESS, R.string.fr_in_progress)
            }
            upNextBtn.setOnClickListener {
                openMediaFragment(ScreenType.BACKLOG, R.string.fr_backlog)
            }
            finishedBtn.setOnClickListener {
                openMediaFragment(ScreenType.FINISHED, R.string.fr_finished)
            }
            starredBtn.setOnClickListener {
                openMediaFragment(ScreenType.STARRED, R.string.fr_starred)

            }
            exploreBtn.setOnClickListener {
                openMediaFragment(ScreenType.EXPLORE, R.string.fr_explore)
            }

            clearBtn.setOnClickListener {
                viewModel.clearDatabase()
            }
        }
    }

    private fun openMediaFragment(type: Int, fragmentTitle: Int) {
        val navController = findNavController()

        activity?.title = getString(fragmentTitle)

        val action = HomeFragmentDirections.actionHomeToMedia(type)
        navController.navigate(action)
    }

}