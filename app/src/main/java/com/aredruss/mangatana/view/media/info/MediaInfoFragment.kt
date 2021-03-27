package com.aredruss.mangatana.view.media.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.aredruss.mangatana.view.util.context
import com.aredruss.mangatana.view.util.getString
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class MediaInfoFragment : BaseFragment(R.layout.fragment_media_info) {

    companion object {
        private const val MEDIA_ID = "malId"
        private const val MEDIA_TYPE = "type"

        private const val COVER_WIDTH = 150
        private const val COVER_HEIGHT = 250

        private const val GENRE_COUNT = 5

        fun create(malId: Long, mediaType: String) = MediaInfoFragment().apply {
            arguments = Bundle().apply {
                putLong(MEDIA_ID, malId)
                putString(MEDIA_TYPE, mediaType)
            }
        }
    }

    private val binding: FragmentMediaInfoBinding by viewBinding()

    private var isStarred: Boolean = false

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

        viewModel.detailsState.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is DetailsState.Loading -> {
                        onLoading()
                    }
                    is DetailsState.Success -> {
                        onMediaLoaded(it.payload, it.localEntry)
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

    private fun onMediaLoaded(media: MediaResponse, localEntry: MediaDb?) = with(binding) {
        hideViews(listOf(mediaLoadingAv, mediaInfoMv))
        mediaGenreCg.removeAllViews()

        setUpMainInfo(media)
        setupAuthor(media)
        setupGenres(media)
        setupButtons()

        if (localEntry != null) {
            addDeleteBtn.visible()
            setupStatus(localEntry.status)
            setupButtons()
            isStarred = localEntry.isStarred
//            setupFavorite(isStarred)

            addDeleteBtn.setOnClickListener {
                viewModel.deleteMediaEntry(localEntry)
            }
        } else {
            hideViews(listOf(addDeleteBtn, mediaStatusTv))
        }

//        mediaFavIv.setOnClickListener {
//            isStarred = !isStarred
//            setupFavorite(isStarred)
//            saveMedia(localEntry?.status ?: MediaDb.UNKNOWN_STATUS)
//        }

        infoContentCl.visible()
    }

    private fun onError(e: Throwable) = with(binding) {
        mediaLoadingAv.gone()
        mediaInfoMv.setIcon(R.drawable.error_logo)
        mediaInfoMv.setText(e::class.java.name)
        mediaInfoMv.visible()
    }

    private fun setUpMainInfo(media: MediaResponse) = with(binding) {
        mediaTitleTv.text = media.title
        mediaRatingTv.text = media.score.toString()
        mediaAboutTv.setContentText(media.synopsis)

        GlideHelper.loadCover(
            root.context,
            media.imageUrl,
            mediaCoverIv,
            COVER_WIDTH,
            COVER_HEIGHT
        )
    }

    private fun setupButtons() = with(binding) {
        addOngoingBtn.setOnClickListener {
            saveMedia(MediaDb.ONGOING_STATUS)
        }
        addBacklogBtn.setOnClickListener {
            saveMedia(MediaDb.BACKLOG_STATUS)
        }
        addFinishBtn.setOnClickListener {
            saveMedia(MediaDb.FINISHED_STATUS)
        }
    }

    private fun setupStatus(status: Int) = with(binding) {
        mediaStatusTv.visible()
        mediaStatusTv.text = getStatus(status)
    }

//    private fun setupFavorite(isStarred: Boolean) = with(binding) {
//        mediaFavIv.visible()
//        if (isStarred) {
//            mediaFavIv.setImageDrawable(binding.getDrawable(R.drawable.ic_favorite))
//        } else {
//            mediaFavIv.setImageDrawable(binding.getDrawable(R.drawable.ic_favorite_border))
//        }
//    }

    private fun setupGenres(media: MediaResponse) = with(binding) {
        val genres = if (media.genreList.size <= GENRE_COUNT) {
            (media.genreList)
        } else {
            media.genreList.subList(0, GENRE_COUNT)
        }

        genres.forEach {
            mediaGenreCg.addView(
                Chip(context).apply {
                    text = it.name
                }
            )
        }
    }

    private fun setupAuthor(media: MediaResponse) = with(binding) {
        when (arguments?.getString(MEDIA_TYPE) ?: JikanRepository.TYPE_MANGA) {
            JikanRepository.TYPE_MANGA -> {
                mediaAuthorTv.text =
                    media.authorList?.first()?.name
                        ?.replace(",", "")
                        ?.split(" ")
                        ?.reversed()
                        ?.joinToString(" ")
            }
            JikanRepository.TYPE_ANIME -> {
                mediaAuthorTv.text = media.producerList?.first()?.name
            }
        }
    }

    private fun saveMedia(status: Int) {
        viewModel.saveMediaEntry(
            status,
            isStarred,
            arguments?.getString(MEDIA_TYPE) ?: JikanRepository.TYPE_MANGA
        )
        Snackbar.make(
            binding.context(),
            binding.root,
            "Saved with status \"${getStatus(status)}\"",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun getStatus(status: Int) = when (status) {
        MediaDb.ONGOING_STATUS -> binding.getString(R.string.status_ongoing)
        MediaDb.BACKLOG_STATUS -> binding.getString(R.string.status_backlog)
        MediaDb.UNKNOWN_STATUS -> "Favorite"
        else -> binding.getString(R.string.status_finished)
    }
}
