package com.aredruss.mangatana.view.media.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentMediaListBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.setBarTitle
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.ScreenCategory
import com.github.terrakok.modo.forward
import com.google.android.material.tabs.TabLayout

class MediaListFragment : BaseFragment(R.layout.fragment_media_list) {

    companion object {
        private const val CATEGORY = "category"
        fun create(category: Int) = MediaListFragment().apply {
            arguments = Bundle().apply { putInt(CATEGORY, category) }
        }
    }

    private val binding: FragmentMediaListBinding by viewBinding()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)
    private var mediaType: String = JikanRepository.TYPE_MANGA

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenCategory = this.arguments?.getInt(CATEGORY) ?: ScreenCategory.EXPLORE
        activity?.setBarTitle(
            stringId = when (screenCategory) {
                ScreenCategory.ON_GOING -> R.string.fr_ongoing_title
                ScreenCategory.BACKLOG -> R.string.fr_backlog_title
                ScreenCategory.FINISHED -> R.string.fr_finished_title
                ScreenCategory.EXPLORE -> R.string.fr_explore_title
                else -> R.string.fr_starred_title
            }
        )

        binding.apply {
            mediaRv.adapter = mediaRvAdapter
            mediaRv.itemAnimator = null
            mediaRv.layoutManager = GridLayoutManager(context, 2)

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

            mediaTypeTl.addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        mediaType = when (tab?.position) {
                            1 -> JikanRepository.TYPE_ANIME
                            else -> JikanRepository.TYPE_MANGA
                        }
                        viewModel.getMediaList(mediaType, screenCategory)
                    }

                    @Suppress("EmptyFunctionBlock")
                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    @Suppress("EmptyFunctionBlock")
                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
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
        hideViews(listOf(mediaRv, listInfoMv))
        listLoadingAv.visible()
    }

    private fun onEmpty() = with(binding) {
        hideViews(listOf(listLoadingAv, mediaRv))
        listInfoMv.setIcon(R.drawable.empty_logo)
        listInfoMv.setText(R.string.empty_result_message)
        listInfoMv.visible()
    }

    private fun onLoaded(payload: ArrayList<MediaDb>) = with(binding) {
        hideViews(listOf(listLoadingAv, listInfoMv))
        mediaRv.visible()
        mediaRvAdapter.setMedia(payload)
    }

    private fun onError(e: Throwable) = with(binding) {
        hideViews(listOf(listLoadingAv, mediaRv))
        listInfoMv.setIcon(R.drawable.error_logo)
        listInfoMv.setText(e::class.java.name)
        listInfoMv.visible()
    }

    private fun openMedia(id: Long) {
        viewModel.mediaType = mediaType
        modo.forward(Screens.MediaInfo(id, mediaType))
    }
}
