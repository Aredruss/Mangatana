package com.aredruss.mangatana.view.media.info

import android.app.AlertDialog
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
import com.aredruss.mangatana.view.extensions.context
import com.aredruss.mangatana.view.extensions.getColor
import com.aredruss.mangatana.view.extensions.getDrawable
import com.aredruss.mangatana.view.extensions.getString
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.openLink
import com.aredruss.mangatana.view.extensions.shareLink
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.DateHelper
import com.aredruss.mangatana.view.util.GlideHelper
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

        super.setupFragment(
            titleRes = R.string.fr_about_media,
            showBackButton = true,
            showMenu = false
        )

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
                AlertDialog.Builder(
                    binding.context(),
                    R.style.ThemeOverlay_MaterialComponents_Dialog
                )
                    .apply {
                        setTitle(R.string.dialog_delete)
                        setMessage(R.string.dialog_delete_message)
                        setPositiveButton(R.string.dialog_ok) { _, _ ->

                            viewModel.deleteMediaEntry(media.malId)
                        }
                        setNegativeButton(R.string.dialog_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
            }
        } else {
            isStarred = false
            hideViews(listOf(deleteBtn, statusTv))
        }

        likeBtn.setOnClickListener {
            isStarred = !isStarred
            starMedia(localEntry?.status ?: MediaDb.ONGOING_STATUS, isStarred)
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
            JikanRepository.TYPE_MANGA -> {
                authorTv.text =
                    media.authorList?.first()?.name
                        ?.replace(",", "")
                        ?.split(" ")
                        ?.reversed()
                        ?.joinToString(" ")
            }
            JikanRepository.TYPE_ANIME -> {
                authorTv.text = media.authorList?.first()?.name
            }
        }
    }

    private fun setupYear(date: String) = with(binding) {
        yearTv.text = DateHelper.parseDate(date)
    }

    private fun saveMedia(status: Int) {
        viewModel.editMediaEntry(status, isStarred)
        showActionResult("Saved!")
    }

    private fun starMedia(status: Int, isLiked: Boolean) {
        if (isLiked) showActionResult(binding.getString(R.string.media_favorite))
        viewModel.editMediaEntry(status, isLiked)
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

        fun create(malId: Long, mediaType: String) = MediaInfoFragment().apply {
            arguments = Bundle().apply {
                putLong(MEDIA_ID, malId)
                putString(MEDIA_TYPE, mediaType)
            }
        }
    }
}
