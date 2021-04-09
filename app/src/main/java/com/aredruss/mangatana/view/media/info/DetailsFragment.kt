package com.aredruss.mangatana.view.media.info

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentDetailsBinding
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.view.extensions.context
import com.aredruss.mangatana.view.extensions.getColor
import com.aredruss.mangatana.view.extensions.getDrawable
import com.aredruss.mangatana.view.extensions.getString
import com.aredruss.mangatana.view.extensions.gone
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.openLink
import com.aredruss.mangatana.view.extensions.shareLink
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.DateHelper
import com.aredruss.mangatana.view.util.GlideHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : BaseFragment(R.layout.fragment_details) {

    private val binding: FragmentDetailsBinding by viewBinding()
    private val viewModel: DetailsViewModel by viewModel()
    private var isStarred: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setupFragment(
            titleRes = R.string.fr_about_media,
            showBackButton = true,
            showMenu = false
        )
        lifecycle.addObserver(viewModel)

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

        if (localEntry != null) {
            setupStatus(localEntry.status)
            isStarred = localEntry.isStarred
            deleteBtn.visible()
            deleteBtn.setOnClickListener {
                viewModel.deleteMediaEntry(media.malId)
            }
        } else {
            isStarred = false
            hideViews(listOf(deleteBtn, statusTv))
        }

        likeBtn.setOnClickListener {
            isStarred = !isStarred
            starMedia(localEntry?.status ?: MediaDb.ONGOING_STATUS)
        }

        shareBtn.setOnClickListener {
            activity?.shareLink(media.url)
        }

        browserBtn.setOnClickListener {
            activity?.openLink(media.url)
        }

        setupMainInfo(media)
        setupAuthor(media)
        setupGenres(media)
        setupFab(localEntry != null)
        setupFavorite()
        setupYear(media.releaseDate.started)

        contentCl.visible()
    }

    private fun onError(e: Throwable) = with(binding) {
        hideViews(listOf(loadingAv, contentCl))
        infoMv.setIcon(R.drawable.error_logo)
        infoMv.setText(e::class.java.name)
        infoMv.visible()
    }

    private fun setupMainInfo(media: MediaResponse) = with(binding) {
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

        genres.sortedBy {
            it.name.length
        }
            .forEach {
                genreCg.addView(
                    Chip(context).apply {
                        text = it.name
                    }
                )
            }
    }

    private fun setupAuthor(media: MediaResponse) = with(binding) {
        when (arguments?.getString(MEDIA_TYPE) ?: JikanRepository.TYPE_MANGA) {
            JikanRepository.TYPE_ANIME -> {
                authorTv.apply {
                    if (media.authorList?.isEmpty() == true) {
                        gone()
                    } else {
                        authorTv.text = media.authorList?.first()?.name
                    }
                }
            }
            else -> {
                authorTv.text =
                    media.authorList?.first()?.name
                        ?.replace(",", "")
                        ?.split(" ")
                        ?.reversed()
                        ?.joinToString(" ")
            }
        }
    }

    private fun setupYear(date: String) = with(binding) {
        yearTv.text = DateHelper.parseDate(date)
    }

    private fun saveMedia(status: Int) {
        viewModel.editMediaEntry(status, isStarred)
        showActionResult("Saved with status \"${getStatus(status)}\"!")
    }

    private fun starMedia(status: Int) {
        viewModel.editMediaEntry(status, isStarred)
        if (isStarred) showActionResult(binding.getString(R.string.media_favorite))
    }

    private fun showActionResult(message: String) {
        Snackbar.make(
            binding.context(),
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(binding.getColor(R.color.mainStatus))
            .setTextColor(binding.getColor(R.color.mainIconTint))
            .show()
    }

    private fun getStatus(status: Int) = when (status) {
        MediaDb.ONGOING_STATUS -> binding.getString(R.string.status_ongoing)
        MediaDb.BACKLOG_STATUS -> binding.getString(R.string.status_backlog)
        else -> binding.getString(R.string.status_finished)
    }

    private fun setupFab(isLocal: Boolean) = with(binding) {
        actionsFab.mainFab.setImageDrawable(
            if (isLocal) {
                binding.getDrawable(R.drawable.ic_edit)
            } else {
                binding.getDrawable(R.drawable.ic_add)
            }
        )

        actionsFab.addOnMenuItemClickListener { _, _, itemId ->
            when (itemId) {
                R.id.action_ongoing -> saveMedia(MediaDb.ONGOING_STATUS)
                R.id.action_backlog -> saveMedia(MediaDb.BACKLOG_STATUS)
                R.id.action_finish -> saveMedia(MediaDb.FINISHED_STATUS)
            }
        }
    }

    companion object {
        private const val MEDIA_ID = "malId"
        private const val MEDIA_TYPE = "type"

        private const val COVER_WIDTH = 150
        private const val COVER_HEIGHT = 250

        private const val GENRE_COUNT = 5

        fun create(malId: Long, mediaType: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(MEDIA_ID, malId)
                putString(MEDIA_TYPE, mediaType)
            }
        }
    }
}
