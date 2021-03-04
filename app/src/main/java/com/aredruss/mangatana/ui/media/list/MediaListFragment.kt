package com.aredruss.mangatana.ui.media.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentMediaListBinding
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.ui.MainViewModel
import com.aredruss.mangatana.ui.util.ScreenType
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListFragment : Fragment(R.layout.fragment_media_list) {

    private val binding: FragmentMediaListBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()
    private val mediaRvAdapter = MediaRvAdapter(this::openMedia)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mediaRv.adapter = mediaRvAdapter
            mediaRv.layoutManager = GridLayoutManager(context, 2)
            getContent(JikanInteractor.TYPE_MANGA)
        }

        viewModel.mediaList.observe(viewLifecycleOwner, {
            when (it) {
                is MediaListState.Loading -> {
                    onLoading()
                }
                is MediaListState.Success -> {
                    onLoaded(it.media)
                }
                is MediaListState.Error -> {
                    onError(it.error)
                }
            }
        })
    }

    private fun getContent(type: String) {
        when (this.arguments?.getInt("screenType") ?: 5) {
            ScreenType.IN_PROGRESS -> viewModel.getSavedMedia(MediaDb.ONGOING_STATUS, type)
            ScreenType.BACKLOG -> viewModel.getSavedMedia(MediaDb.BACKLOG_STATUS, type)
            ScreenType.FINISHED -> viewModel.getSavedMedia(MediaDb.FINISHED_STATUS, type)
            ScreenType.STARRED -> {

            }
            ScreenType.EXPLORE -> viewModel.exploreMedia(JikanInteractor.TYPE_MANGA)
        }
    }

    private fun openMedia(id: Long, type: String) {
        val navController = findNavController()
        val action = MediaListFragmentDirections.actionShowMedia(id, type)
        navController.navigate(action)
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