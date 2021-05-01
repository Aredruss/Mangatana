package com.aredruss.mangatana.view.media.info

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.databinding.FragmentDetailsBinding
import com.aredruss.mangatana.model.Genre
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.repo.JikanRepository
import com.aredruss.mangatana.utils.ParseHelper
import com.aredruss.mangatana.view.extensions.changeLayersColor
import com.aredruss.mangatana.view.extensions.context
import com.aredruss.mangatana.view.extensions.getDrawable
import com.aredruss.mangatana.view.extensions.getString
import com.aredruss.mangatana.view.extensions.gone
import com.aredruss.mangatana.view.extensions.hideViews
import com.aredruss.mangatana.view.extensions.openLink
import com.aredruss.mangatana.view.extensions.shareLink
import com.aredruss.mangatana.view.extensions.showSingle
import com.aredruss.mangatana.view.extensions.visible
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.DialogHelper
import com.aredruss.mangatana.view.util.GlideHelper
import com.aredruss.mangatana.view.util.dialog.SaveDialog
import com.aredruss.mangatana.view.util.dialog.SaveDialog.Companion.SAVE_DIALOG_TAG
import com.github.terrakok.modo.back
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : BaseFragment(R.layout.fragment_details) {

    private val binding: FragmentDetailsBinding by viewBinding()
    private val viewModel: DetailsViewModel by viewModel()
    private var isStarred: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        setupViews()
        setupAction()
    }

    override fun setupViews() = with(binding) {
        genreTv.text = ""
        loadingAv.changeLayersColor(R.color.colorAccent)
    }

    private fun setupAction() {
        viewModel.getMediaDetails(
            type = arguments?.getString(MEDIA_TYPE) ?: JikanRepository.TYPE_MANGA,
            malId = arguments?.getLong(MEDIA_ID) ?: 1L
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

        if (localEntry != null) {
            setupStatus(localEntry.status)
            isStarred = localEntry.isStarred
        } else {
            isStarred = false
            statusTv.gone()
        }

        likeBtn.setOnClickListener {
            isStarred = !isStarred
            starMedia(localEntry?.status ?: MediaDb.ONGOING_STATUS)
        }

        setupMainInfo(media)
        setupAuthor(media)
        if (genreTv.text.isEmpty()) setupGenres(media.genreList)
        setupSaveEditButton(localEntry != null)
        setupFavorite()
        setupYear(media.releaseDate.started)
        setupToolbar(media.malId, media.url, localEntry != null)
        setupViewers(media.viewerCount)

        val statusListener = View.OnClickListener {
            SaveDialog(
                currentStatus = localEntry?.status ?: 0,
                saveAction = this@DetailsFragment::saveMedia
            )
                .showSingle(childFragmentManager, SAVE_DIALOG_TAG)
        }

        saveBtnCl.setOnClickListener(statusListener)
        statusTv.setOnClickListener(statusListener)

        contentCl.visible()
    }

    private fun onError(e: Throwable) = with(binding) {
        hideViews(listOf(loadingAv, contentCl))
        infoMv.setIcon(R.drawable.ic_error_logo)
        infoMv.setText(e::class.java.name)
        infoMv.visible()
    }

    private fun setupMainInfo(media: MediaResponse) = with(binding) {
        mediaTitleTv.text = media.title
        if (media.altTitle != null && media.altTitle != media.title) {
            altTitleTv.text = media.altTitle
            altTitleTv.visible()
        } else {
            altTitleTv.gone()
        }
        ratingTv.text = media.score.toString()
        if (!media.synopsis.isNullOrBlank()) {
            aboutTv.setContentText(media.synopsis)
        } else {
            aboutTv.gone()
        }

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

    private fun setupGenres(genres: List<Genre>) = with(binding) {
        genreTv.append(ParseHelper.parseGenres(genres))
    }

    private fun setupAuthor(media: MediaResponse) = with(binding) {
        if (media.authorList?.isEmpty() == true) {
            authorTv.gone()
            return@with
        }
        val author = media.authorList?.first()?.name
        if (!author.isNullOrBlank()) {
            when (arguments?.getString(MEDIA_TYPE)) {
                JikanRepository.TYPE_ANIME -> authorTv.text = author
                else -> authorTv.text = ParseHelper.parseAuthor(author)
            }
        }
    }

    private fun setupYear(date: String) = with(binding) {
        yearTv.text = ParseHelper.parseDate(date)
    }

    private fun setupViewers(count: Long) = with(binding) {
        viewersTv.text = count.toString()
    }

    private fun setupSaveEditButton(isLocal: Boolean) = with(binding) {
        if (isLocal) {
            saveIconIv.setImageDrawable(binding.getDrawable(R.drawable.ic_edit))
            saveTextTv.text = "EDIT"
        } else {
            saveIconIv.setImageDrawable(binding.getDrawable(R.drawable.ic_add))
            saveTextTv.text = "SAVE"
        }
        saveBtnCl.visible()
    }

    private fun setupToolbar(id: Long, url: String, isLocal: Boolean) = with(binding) {
        shareBtn.setOnClickListener {
            activity?.shareLink(url)
        }
        backBtn.setOnClickListener {
            modo.back()
        }

        detailsMenuIb.setOnClickListener {
            PopupMenu(this.root.context, it).apply {
                inflate(R.menu.menu_details)
                menu.findItem(R.id.action_delete).isVisible = isLocal
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_browser -> activity?.openLink(url)
                        R.id.action_delete -> DialogHelper.buildConfirmDialog(
                            context = binding.context(),
                            title = R.string.dialog_delete,
                            message = R.string.dialog_delete_message,
                            argument = id,
                            action = this@DetailsFragment::deleteMedia
                        )
                    }
                    return@setOnMenuItemClickListener false
                }
                show()
            }
        }
    }

    private fun saveMedia(status: Int) {
        viewModel.upsertMediaEntry(status, isStarred)
    }

    private fun starMedia(status: Int) {
        viewModel.upsertMediaEntry(status, isStarred)
    }

    private fun deleteMedia(malId: Long) {
        viewModel.deleteMediaEntry(malId)
    }

    private fun getStatus(status: Int) = when (status) {
        MediaDb.ONGOING_STATUS -> binding.getString(R.string.status_ongoing)
        MediaDb.BACKLOG_STATUS -> binding.getString(R.string.status_backlog)
        else -> binding.getString(R.string.status_finished)
    }

    companion object {
        private const val MEDIA_ID = "malId"
        private const val MEDIA_TYPE = "type"

        private const val COVER_WIDTH = 150
        private const val COVER_HEIGHT = 250

        fun create(malId: Long, mediaType: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(MEDIA_ID, malId)
                putString(MEDIA_TYPE, mediaType)
            }
        }
    }
}
