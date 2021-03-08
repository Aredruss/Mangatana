package com.aredruss.mangatana.ui.media.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentMediaListBinding
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.ui.util.BaseFragment
import com.aredruss.mangatana.ui.util.BaseState
import com.github.terrakok.modo.forward
import com.google.android.material.tabs.TabLayout
import timber.log.Timber

class MediaListFragment : BaseFragment(R.layout.fragment_media_list) {

    companion object {
        private const val SCREEN_CATEGORY = "category"
        fun create(category: Int) = MediaListFragment().apply {
            arguments = Bundle().apply { putInt(SCREEN_CATEGORY, category) }
        }
    }

    private val binding: FragmentMediaListBinding by viewBinding()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)
    private var mediaType: String = JikanInteractor.TYPE_MANGA
    private var screenCategory = this.arguments?.getInt(SCREEN_CATEGORY) ?: 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) Timber.e("ON VIEW CREATED")

        binding.apply {
            mediaRv.adapter = mediaRvAdapter
            mediaRv.layoutManager = GridLayoutManager(context, 2)

            mediaTypeTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    mediaType = when (tab?.position) {
                        1 -> JikanInteractor.TYPE_ANIME
                        else -> JikanInteractor.TYPE_MANGA
                    }
                    viewModel.getContent(mediaType, screenCategory)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            viewModel.getContent(screenCategory = screenCategory)
        }

        viewModel.mediaList.observe(viewLifecycleOwner, {
            when (it) {
                is BaseState.Loading -> {
                    onLoading()
                }
                is BaseState.Success -> {
                    onLoaded(it.payload)
                }
                is BaseState.Error -> {
                    onError(it.error)
                }
            }
        })
    }

    private fun openMedia(id: Long) {
        modo.forward(Screens.MediaInfo(id, mediaType))
    }

    private fun onLoading() = with(binding) {
        mediaListVf.displayedChild = 0
    }

    private fun onLoaded(media: ArrayList<LiteMedia>) = with(binding) {
        mediaListVf.displayedChild = 1
        mediaRvAdapter.setMedia(media)
    }

    private fun onError(e: Throwable) = with(binding) {
        mediaListVf.displayedChild = 2
        mediaListErrorTv.text = e.message
    }
}