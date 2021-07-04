package com.aredruss.mangatana.view.home

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.modo.ScreenCategory
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.showViews
import com.aredruss.mangatana.view.media.list.MediaRvAdapter
import com.aredruss.mangatana.view.util.BaseFragment
import com.github.terrakok.modo.forward
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding<FragmentHomeBinding>()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)
    private val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel.getRecentManga(JikanRepository.TYPE_MANGA)
    }

    override fun setupViews() {
        setupButtons()
        setupMenu()
        observeHomeState()
        setupRv()
    }

    private fun setupMenu() = with(binding) {
        homeMenuIb.setOnClickListener {
            val menu = PopupMenu(this.root.context, it).apply {
                inflate(R.menu.menu_main)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_about -> modo.forward(Screens.About())
                        R.id.action_settings -> modo.forward(Screens.Settings())
                    }
                    return@setOnMenuItemClickListener false
                }
                show()
            }
        }
    }

    private fun setupButtons() = with(binding.categoryLt) {
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

    private fun setupRv() = with(binding) {
        recentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recentRv.adapter = mediaRvAdapter
    }

    private fun observeHomeState() {
        viewModel.homeState.observe(viewLifecycleOwner, {
            when (it) {
                is HomeState.Empty -> onEmpty()
                is HomeState.Success -> onLoaded(it.payload)
            }
        })
    }

    private fun onEmpty() = with(binding) {
        hideViews(listOf(recentRv, recentTv))
    }

    private fun onLoaded(payload: ArrayList<MediaDb>) = with(binding) {
        showViews(listOf(recentRv, recentTv))
        mediaRvAdapter.setMedia(payload)
    }

    private fun openMediaFragment(type: Int) {
        modo.forward(Screens.MediaList(type))
    }

    private fun openMedia(id: Long) {
        modo.forward(Screens.MediaInfo(id, JikanRepository.TYPE_MANGA))
    }

    companion object {
        fun create() = HomeFragment()
    }
}
