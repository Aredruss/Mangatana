package com.aredruss.mangatana.view.util.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.DialogFeedbackBinding
import com.aredruss.mangatana.view.extensions.composeEmail
import com.aredruss.mangatana.view.extensions.openLink
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeedbackDialog : BottomSheetDialogFragment() {

    lateinit var binding: DialogFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_BottomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFeedbackBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cancelTv.setOnClickListener { dismiss() }

            githubBtn.setOnClickListener {
                activity?.openLink(GITHUB_ISSUE_LINK)
            }
            twitterBtn.setOnClickListener {
                activity?.openLink(TWITTER_MENTION_LINK)
            }
            emailBtn.setOnClickListener {
                activity?.composeEmail()
            }
        }
    }

    companion object {
        const val FEEDBACK_DIALOG_TAG = "feedback"
        private const val GITHUB_ISSUE_LINK = "https://github.com/Aredruss/Mangatana/issues/new"
        private const val TWITTER_MENTION_LINK = "https://twitter.com/intent/tweet?text=@Aredruss"
    }
}
