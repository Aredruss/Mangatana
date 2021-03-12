package com.aredruss.mangatana.view.media.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentMediaListBinding
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.gone
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.ScreenCategory
import com.github.terrakok.modo.forward
import com.google.android.material.tabs.TabLayout
import timber.log.Timber

class MediaListFragment : BaseFragment(R.layout.fragment_media_list) {

    companion object {
        private const val CATEGORY = "category"
        fun create(category: Int) = MediaListFragment().apply {
            Timber.e("screen cat = $category")
            arguments = Bundle().apply { putInt(CATEGORY, category) }
        }
    }

    private val binding: FragmentMediaListBinding by viewBinding()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)
    private var mediaType: String = JikanRepository.TYPE_MANGA

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenCategory = this.arguments?.getInt(CATEGORY) ?: ScreenCategory.EXPLORE

        binding.apply {
            mediaRv.adapter = mediaRvAdapter
            mediaRv.layoutManager = GridLayoutManager(context, 2)

            when (viewModel.mediaType) {
                JikanRepository.TYPE_ANIME -> mediaTypeTl.getTabAt(1)?.select()
                else -> mediaTypeTl.getTabAt(0)?.select()
            }

            viewModel.getMediaList(null, screenCategory = screenCategory)

            mediaTypeTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

        viewModel.mediaList.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is ListState.Loading -> {
                        onLoading()
                    }
                    is ListState.Empty -> {
                        onLoading()
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

    override fun onLoading() = with(binding) {
        listLoadingCl.visible()
        hideViews(listOf(listContentCl, listErrorCl))
    }

    private fun onLoaded(payload: ArrayList<LiteMedia>) = with(binding) {
        listContentCl.visible()
        hideViews(listOf(listLoadingCl, listErrorCl))
        mediaRvAdapter.setMedia(payload)
    }

    override fun onError(e: Throwable) = with(binding) {
        listErrorCl.visible()
        listLoadingCl.gone()
        mediaListErrorTv.text = e.message
    }

    private fun openMedia(id: Long) {
        viewModel.mediaType = mediaType
        modo.forward(Screens.MediaInfo(id, mediaType))
    }
}
