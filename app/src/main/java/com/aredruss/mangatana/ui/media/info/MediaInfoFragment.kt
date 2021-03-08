package com.aredruss.mangatana.ui.media.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.data.model.MediaResponse
import com.aredruss.mangatana.databinding.FragmentMediaInfoBinding
import com.aredruss.mangatana.domain.JikanInteractor
import com.aredruss.mangatana.ui.util.BaseFragment
import com.aredruss.mangatana.ui.util.BaseState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class MediaInfoFragment : BaseFragment(R.layout.fragment_media_info) {

    companion object {
        private const val MEDIA_ID = "malId"
        private const val MEDIA_TYPE = "type"

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
            type = this.arguments?.getString(MEDIA_TYPE) ?: JikanInteractor.TYPE_MANGA,
            malId = this.arguments?.getLong(MEDIA_ID) ?: 1L
        )

        viewModel.mediaInfo.observe(viewLifecycleOwner, {
            when (it) {
                is BaseState.Loading -> {
                    onLoading()
                }
                is BaseState.Success -> {
                    onMediaLoaded(it.payload)
                }
                is BaseState.Error -> {
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
//        mediaDescTv.setTitleText("About")
//        mediaDescTv.setContentText(media.synopsis)
//        mediaDescTv.expand(true)
        //  mediaAuthorTv.text = media.authorList.first().name
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