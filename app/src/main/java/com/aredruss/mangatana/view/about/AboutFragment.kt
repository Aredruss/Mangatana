package com.aredruss.mangatana.view.about

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.BuildConfig
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentAboutBinding
import com.aredruss.mangatana.view.extensions.showSingle
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.dialog.FeedbackDialog
import com.aredruss.mangatana.view.util.dialog.FeedbackDialog.Companion.FEEDBACK_DIALOG_TAG
import com.github.terrakok.modo.back

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    private val binding: FragmentAboutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun setupViews() = with(binding) {
        versionTv.text = BuildConfig.VERSION_NAME
        backBtn.setOnClickListener {
            modo.back()
        }

        feedbackTv.setOnClickListener {
            FeedbackDialog().showSingle(childFragmentManager, FEEDBACK_DIALOG_TAG)
        }
    }

    companion object {
        fun create() = AboutFragment()
    }
}
