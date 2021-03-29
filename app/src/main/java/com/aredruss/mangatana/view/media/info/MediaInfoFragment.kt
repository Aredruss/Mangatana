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
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.setBarTitle
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.GlideHelper
import com.aredruss.mangatana.view.util.context
import com.aredruss.mangatana.view.util.getColor
import com.aredruss.mangatana.view.util.getDrawable
import com.aredruss.mangatana.view.util.getString
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class MediaInfoFragment : BaseFragment(R.layout.fragment_media_info) {

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
        activity?.setBarTitle(R.string.media_about)

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
        hideViews(listOf(contentCl, infoMv))
        loadingAv.visible()
    }

    private fun onMediaLoaded(media: MediaResponse, localEntry: MediaDb?) = with(binding) {
        hideViews(listOf(loadingAv, infoMv))
        genreCg.removeAllViews()

        setUpMainInfo(media)
        setupAuthor(media)
        setupGenres(media)

        if (localEntry != null) {
            deleteBtn.visible()
            setupStatus(localEntry.status)
            isStarred = localEntry.isStarred
            setupFavorite()
            actionsFab.mainFab.setImageDrawable(binding.getDrawable(R.drawable.ic_edit))
            deleteBtn.setOnClickListener {
                viewModel.deleteMediaEntry(localEntry)
            }
        } else {
            actionsFab.mainFab.setImageDrawable(binding.getDrawable(R.drawable.ic_add))
            hideViews(listOf(deleteBtn, statusTv))
        }

        actionsFab.addOnMenuItemClickListener { _, _, itemId ->
            when (itemId) {
                R.id.action_ongoing -> saveMedia(MediaDb.ONGOING_STATUS)
                R.id.action_backlog -> saveMedia(MediaDb.BACKLOG_STATUS)
                R.id.action_finish -> saveMedia(MediaDb.FINISHED_STATUS)
            }
        }

        likeBtn.setOnClickListener {
            isStarred = !isStarred
            setupFavorite()
            saveMedia(localEntry?.status ?: MediaDb.UNKNOWN_STATUS)
        }

        contentCl.visible()
    }

    private fun onError(e: Throwable) = with(binding) {
        hideViews(listOf(loadingAv, contentCl))
        infoMv.setIcon(R.drawable.error_logo)
        infoMv.setText(e::class.java.name)
        infoMv.visible()
    }

    private fun setUpMainInfo(media: MediaResponse) = with(binding) {
        mediaTitleTv.text = media.title
        ratingTv.text = media.score.toString()
        aboutTv.setContentText(media.synopsis)

        GlideHelper.loadCover(
            root.context,
            media.imageUrl,
            coverIv,
            COVER_WIDTH,
            COVER_HEIGHT
        )
    }

    private fun setupStatus(status: Int) = with(binding) {
        statusTv.visible()
        statusTv.text = getStatus(status)
    }

    private fun setupFavorite() = with(binding) {
        likeBtn.visible()
        if (isStarred) {
            likeBtn.setImageDrawable(binding.getDrawable(R.drawable.ic_favorite))
        } else {
            likeBtn.setImageDrawable(binding.getDrawable(R.drawable.ic_favorite_border))
        }
    }

    private fun setupGenres(media: MediaResponse) = with(binding) {
        val genres = if (media.genreList.size <= GENRE_COUNT) {
            (media.genreList)
        } else {
            media.genreList.subList(0, GENRE_COUNT)
        }

        genres.sortedBy { it.name.length }.forEach {
            genreCg.addView(
                Chip(context).apply {
                    text = it.name
                    backgroundDrawable = binding.getDrawable(R.drawable.bg_primary)
                }
            )
        }
    }

    private fun setupAuthor(media: MediaResponse) = with(binding) {
        when (arguments?.getString(MEDIA_TYPE) ?: JikanRepository.TYPE_MANGA) {
            JikanRepository.TYPE_MANGA -> {
                authorTv.text =
                    media.authorList?.first()?.name
                        ?.replace(",", "")
                        ?.split(" ")
                        ?.reversed()
                        ?.joinToString(" ")
            }
            JikanRepository.TYPE_ANIME -> {
                authorTv.text = media.producerList?.first()?.name
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
        )
            .setBackgroundTint(binding.getColor(R.color.statusBlue))
            .setTextColor(binding.getColor(R.color.white))
            .show()
    }

    private fun getStatus(status: Int) = when (status) {
        MediaDb.ONGOING_STATUS -> binding.getString(R.string.status_ongoing)
        MediaDb.BACKLOG_STATUS -> binding.getString(R.string.status_backlog)
        MediaDb.UNKNOWN_STATUS -> "Favorite"
        else -> binding.getString(R.string.status_finished)
    }

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
}
