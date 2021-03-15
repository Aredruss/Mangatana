package com.aredruss.mangatana.view.media.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentMediaInfoBinding
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.gone
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.GlideHelper

class MediaInfoFragment : BaseFragment(R.layout.fragment_media_info) {

    companion object {
        private const val MEDIA_ID = "malId"
        private const val MEDIA_TYPE = "type"

        private const val COVER_WIDTH = 150
        private const val COVER_HEIGHT = 250

        fun create(malId: Long, mediaType: String) = MediaInfoFragment().apply {
            arguments = Bundle().apply {
                putLong(MEDIA_ID, malId)
                putString(MEDIA_TYPE, mediaType)
            }
        }
    }

    private val binding: FragmentMediaInfoBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMediaDetails(
            type = this.arguments?.getString(MEDIA_TYPE) ?: JikanRepository.TYPE_MANGA,
            malId = this.arguments?.getLong(MEDIA_ID) ?: 1L
        )

        viewModel.mediaInfo.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is DetailsState.Loading -> {
                        onLoading()
                    }
                    is DetailsState.Success -> {
                        onMediaLoaded(it.payload)
                    }
                    is DetailsState.Error -> {
                        onError(it.error)
                    }
                }
            }
        )
    }

    private fun onLoading() = with(binding) {
        hideViews(listOf(infoContentCl, mediaInfoMv))
        mediaLoadingAv.visible()
    }

    private fun onMediaLoaded(media: MediaResponse) = with(binding) {
        hideViews(listOf(mediaLoadingAv, mediaInfoMv))

        mediaTitleTv.text = media.title
        mediaRatingTv.text = media.score.toString()

        GlideHelper.loadCover(
            root.context,
            media.imageUrl,
            mediaCoverIv,
            COVER_WIDTH,
            COVER_HEIGHT
        )

        addProgressBtn.setOnClickListener {
            saveMedia(MediaDb.ONGOING_STATUS)
        }
        addBacklogBtn.setOnClickListener {
            saveMedia(MediaDb.BACKLOG_STATUS)
        }
        addFinishedBtn.setOnClickListener {
            saveMedia(MediaDb.FINISHED_STATUS)
        }

        infoContentCl.visible()
    }

    private fun onError(e: Throwable) = with(binding) {
        mediaLoadingAv.gone()
        mediaInfoMv.setIcon(R.drawable.error_logo)
        mediaInfoMv.setText(e::class.java.name)
        mediaInfoMv.visible()
    }

    private fun saveMedia(status: Int) {
        viewModel.saveMedia(status)
        Toast.makeText(binding.root.context, "SAVED WITH STATUS $status", Toast.LENGTH_SHORT).show()
    }
}