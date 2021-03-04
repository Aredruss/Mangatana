package com.aredruss.mangatana.ui.media.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.data.model.MediaResponse
import com.aredruss.mangatana.databinding.FragmentMediaBinding
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.ui.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment : Fragment(R.layout.fragment_media) {

    private val binding: FragmentMediaBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMediaDetails(
            type = this.arguments?.getString("mediaType") ?: JikanInteractor.TYPE_MANGA,
            malId = this.arguments?.getLong("mediaId") ?: 1L
        )

        viewModel.mediaInfo.observe(viewLifecycleOwner, {
            when (it) {
                is MediaInfoState.InProgress -> {
                    onLoading()
                }
                is MediaInfoState.Success -> {
                    onMediaLoaded(it.media)
                }
                is MediaInfoState.Error -> {
                    onError(it.error)
                }
            }
        })
    }

    private fun onLoading() = with(binding) {
        mediaVf.displayedChild = 0
    }

    private fun onMediaLoaded(media: MediaResponse) = with(binding) {
        mediaVf.displayedChild = 1

        mediaTitleTv.text = media.title
        mediaDescTv.setTitleText("About")
        mediaDescTv.setContentText(media.synopsis)
        mediaDescTv.expand(true)
        mediaAuthorTv.text = media.authorList.first().name
        mediaRatingTv.text = media.score.toString()

        val requestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(10))

        Glide.with(binding.root.context)
            .load(media.imageUrl)
            .apply(RequestOptions().override(150, 250))
            .apply(requestOptions)
            .into(binding.mediaCoverIv)

        addProgressBtn.setOnClickListener {
            saveMedia(MediaDb.ONGOING_STATUS)
        }
        addBacklogBtn.setOnClickListener {
            saveMedia(MediaDb.BACKLOG_STATUS)
        }
        addFinishedBtn.setOnClickListener {
            saveMedia(MediaDb.FINISHED_STATUS)
        }
    }

    private fun onError(e: Throwable) = with(binding) {
        mediaVf.displayedChild = 2
        mediaErrorTv.text = e.message
    }

    private fun saveMedia(status: Int) {
        viewModel.saveMedia(status)
        Toast.makeText(binding.root.context, "SAVED WITH STATUS $status", Toast.LENGTH_SHORT).show()
    }
}