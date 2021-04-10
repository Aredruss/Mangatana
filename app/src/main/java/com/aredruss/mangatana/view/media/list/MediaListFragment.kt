package com.aredruss.mangatana.view.media.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentMediaListBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.clear
import com.aredruss.mangatana.view.extensions.hide
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.ScreenCategory
import com.github.terrakok.modo.forward
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MediaListFragment : BaseFragment(R.layout.fragment_media_list) {

    private val viewModel: MediaListViewModel by sharedViewModel()
    private val binding: FragmentMediaListBinding by viewBinding()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)
    private var mediaType: String = JikanRepository.TYPE_MANGA
    private var isSearch: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        val screenCategory = this.arguments?.getInt(CATEGORY) ?: ScreenCategory.EXPLORE

        super.setupFragment(
            titleRes = getFragmentTitle(screenCategory),
            showBackButton = true,
            showMenu = false
        )

        binding.apply {
            mediaRv.adapter = mediaRvAdapter
            mediaRv.itemAnimator = null
            mediaRv.layoutManager = GridLayoutManager(context, 2)
            setupSearch()

            when (viewModel.mediaType) {
                JikanRepository.TYPE_ANIME -> {
                    mediaType = JikanRepository.TYPE_ANIME
                    mediaTypeTl.getTabAt(1)?.select()
                }
                else -> {
                    mediaType = JikanRepository.TYPE_MANGA
                    mediaTypeTl.getTabAt(0)?.select()
                }
            }

            viewModel.getMediaList(mediaType, screenCategory = screenCategory)

            mediaTypeTl.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        mediaType = when (tab?.position) {
                            1 -> JikanRepository.TYPE_ANIME
                            else -> JikanRepository.TYPE_MANGA
                        }

                        if (isSearch) {
                            viewModel.searchForMedia(searchSv.query.toString(), mediaType)
                        } else {
                            viewModel.getMediaList(mediaType, screenCategory)
                        }
                    }

                    @Suppress("EmptyFunctionBlock")
                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    @Suppress("EmptyFunctionBlock")
                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })

            searchSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        viewModel.searchForMedia(query, mediaType)
                        mediaRv.scrollToPosition(0)
                        isSearch = true
                    } else {
                        isSearch = false
                        searchSv.hide()
                    }
                    return false
                }

                @Suppress("EmptyFunctionBlock")
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            searchSv.setOnCloseListener {
                viewModel.getMediaList(mediaType, screenCategory = screenCategory, isSearch)
                isSearch = false
                mediaRv.scrollToPosition(0)
                return@setOnCloseListener false
            }

            searchSv.setOnClickListener {
                searchSv.isIconified = false
            }

            searchIb.setOnClickListener {
                if (searchSv.isVisible) {
                    searchSv.hide()
                    isSearch = false
                } else {
                    isSearch = true
                    searchSv.visible()
                }
            }
        }

        viewModel.listState.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is ListState.Loading -> {
                        onLoading()
                    }
                    is ListState.Empty -> {
                        onEmpty()
                    }
                    is ListState.Success -> {
                        onLoaded(it.payload)
                    }
                    is ListState.Error -> {
                        onError(it.error)
                    }
                }
            }
        )
    }

    private fun onLoading() = with(binding) {
        hideViews(listOf(mediaRv, infoMv))
        loadingAv.visible()
    }

    private fun onEmpty() = with(binding) {
        hideViews(listOf(loadingAv, mediaRv))
        infoMv.setIcon(R.drawable.empty_logo)
        infoMv.setText(R.string.empty_result_message)
        infoMv.visible()
    }

    private fun onLoaded(payload: ArrayList<MediaDb>) = with(binding) {
        hideViews(listOf(loadingAv, infoMv))
        mediaRv.visible()
        mediaRvAdapter.setMedia(payload)
    }

    private fun onError(e: Throwable) = with(binding) {
        hideViews(listOf(loadingAv, mediaRv))
        infoMv.setIcon(R.drawable.error_logo)
        infoMv.setText(e::class.java.name)
        infoMv.visible()
    }

    private fun setupSearch() = with(binding) {
        searchSv.setOnCloseListener {
            if (searchSv.query.isEmpty()) {
                searchSv.hide()
            } else {
                searchSv.clear()
            }
            return@setOnCloseListener true
        }
    }

    private fun openMedia(id: Long) {
        viewModel.mediaType = mediaType
        modo.forward(Screens.MediaInfo(id, mediaType))
    }

    private fun getFragmentTitle(screenCategory: Int): Int {
        return when (screenCategory) {
            ScreenCategory.ON_GOING -> R.string.fr_ongoing_title
            ScreenCategory.BACKLOG -> R.string.fr_backlog_title
            ScreenCategory.FINISHED -> R.string.fr_finished_title
            ScreenCategory.EXPLORE -> R.string.fr_explore_title
            else -> R.string.fr_starred_title
        }
    }

    companion object {
        private const val CATEGORY = "category"
        fun create(category: Int) = MediaListFragment().apply {
            arguments = Bundle().apply { putInt(CATEGORY, category) }
        }
    }
}
