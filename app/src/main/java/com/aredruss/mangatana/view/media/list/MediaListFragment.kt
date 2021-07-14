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
import com.aredruss.mangatana.modo.ScreenCategory
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.changeLayersColor
import com.aredruss.mangatana.view.extensions.clear
import com.aredruss.mangatana.view.extensions.getColor
import com.aredruss.mangatana.view.extensions.getString
import com.aredruss.mangatana.view.extensions.hide
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.github.terrakok.modo.back
import com.github.terrakok.modo.forward
import com.github.terrakok.modo.replace
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MediaListFragment : BaseFragment(R.layout.fragment_media_list) {

    private val viewModel: MediaListViewModel by sharedViewModel()
    private val binding: FragmentMediaListBinding by viewBinding()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)
    private var mediaType: String = JikanRepository.TYPE_MANGA
    private var screenCategory = 0
    private var isSearch: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        screenCategory = this.arguments?.getInt(CATEGORY) ?: ScreenCategory.EXPLORE
        setupViews()
        setupAction()
        viewModel.getMediaList(mediaType, screenCategory)
    }

    override fun setupViews() = with(binding) {
        setupToolbar()
        setupTabs()
        setupRv()
        setupSearch()
        loadingAv.changeLayersColor(R.color.colorAccent)
    }

    private fun setupAction() {
        observeListState()
    }

    private fun setupToolbar() = with(binding) {
        labelTv.text = binding.getString(getFragmentTitle(screenCategory))
        backBtn.setOnClickListener { modo.back() }
    }

    private fun setupRv() = with(binding) {
        mediaRv.adapter = mediaRvAdapter
        mediaRv.itemAnimator = null
        mediaRv.layoutManager = GridLayoutManager(context, 2)
        mediaRv.scrollToPosition(0)
    }

    private fun setupSearch() = with(binding) {
        searchIb.setOnClickListener {
            if (searchSv.isVisible) {
                searchSv.hide()
                isSearch = false
            } else {
                isSearch = true
                searchSv.visible()
            }
            resolveSearchIbBg()
        }

        with(searchSv) {
            clear()
            setOnClickListener { isIconified = false }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        viewModel.searchForMedia(query, mediaType)
                        mediaRv.scrollToPosition(0)
                        isSearch = true
                    } else {
                        isSearch = false
                        searchSv.hide()
                    }
                    resolveSearchIbBg()
                    return false
                }

                @Suppress("EmptyFunctionBlock")
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            setOnCloseListener {
                if (this.query.isNullOrEmpty()) {
                    hide()
                    isSearch = false
                    viewModel.getMediaList(mediaType, screenCategory)
                    mediaRv.scrollToPosition(0)
                } else {
                    clear()
                }
                resolveSearchIbBg()
                return@setOnCloseListener true
            }
        }
    }

    private fun setupTabs() = with(binding) {
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
        isSearch = false

        mediaTypeTl.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    mediaRv.scrollToPosition(0)
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

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    mediaRv.scrollToPosition(0)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    mediaRv.scrollToPosition(0)
                }
            }
        )
    }

    private fun observeListState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.content?.let {
                onLoaded(it as ArrayList<MediaDb>)
            }
            state.error?.consume()?.let {
                onError(it)
            }
            if (state.isLoading) {
                onLoading()
            }
            if (state.isEmpty) {
                onEmpty()
            }
        }
    }

    private fun onLoading() = with(binding) {
        hideViews(listOf(mediaRv, infoMv, infoActionTv))
        loadingAv.visible()
    }

    private fun onEmpty() = with(binding) {
        hideViews(listOf(loadingAv, mediaRv))
        infoMv.apply {
            setIcon(R.drawable.ic_empty_msg)
            setText(R.string.empty_result_message)
            visible()
        }
        if (screenCategory != ScreenCategory.EXPLORE) {
            infoActionTv.apply {
                setText(R.string.media_list_find_new)
                setOnClickListener(null)
                setOnClickListener {
                    modo.replace(
                        Screens.MediaList(screenCategory),
                        Screens.MediaList(ScreenCategory.EXPLORE)
                    )
                }
                visible()
            }
        }
    }

    private fun onLoaded(payload: ArrayList<MediaDb>) = with(binding) {
        hideViews(listOf(loadingAv, infoMv, infoActionTv))
        mediaRv.visible()
        mediaRvAdapter.setMedia(payload)
    }

    private fun onError(e: Throwable) = with(binding) {
        hideViews(listOf(loadingAv, mediaRv))
        infoMv.apply {
            setIcon(R.drawable.ic_error_logo)
            setText(e.localizedMessage ?: e::class.java.name)
            visible()
        }
        infoActionTv.apply {
            text = binding.getString(R.string.media_list_reload)
            setOnClickListener(null)
            setOnClickListener {
                viewModel.getMediaList(mediaType, screenCategory)
            }
            visible()
        }
    }

    private fun openMedia(id: Long) {
        viewModel.mediaType = mediaType
        modo.forward(Screens.MediaInfo(id, mediaType, true))
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

    private fun resolveSearchIbBg() = with(binding) {
        searchIb.apply {
            if (isSearch) {
                setColorFilter(binding.getColor(R.color.colorAccent))
            } else {
                setColorFilter(binding.getColor(R.color.barIconTint))
            }
        }
    }

    companion object {
        private const val CATEGORY = "category"
        fun create(category: Int) = MediaListFragment().apply {
            arguments = Bundle().apply { putInt(CATEGORY, category) }
        }
    }
}
