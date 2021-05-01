package com.aredruss.mangatana.view.util.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.PreferenceViewBinding

class PreferenceView(
    private val ctx: Context,
    private val attrSet: AttributeSet
) : ConstraintLayout(ctx, attrSet) {
    var binding: PreferenceViewBinding = PreferenceViewBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    init {
        setAttributes()
    }

    fun setText(text: String) = with(binding) {
        nameTv.text = text
    }

    fun setText(text: Int) = with(binding) {
        nameTv.text = root.context.getString(text)
    }

    fun setSubText(subText: String) = with(binding) {
        subTv.text = subText
    }

    fun setSubText(text: Int) = with(binding) {
        subTv.text = root.context.getString(text)
    }

    fun setView(view: View) = with(binding) {
        if (actionFl.childCount == 0) actionFl.addView(view)
    }

    private fun setAttributes() {
        val attributes = context.obtainStyledAttributes(
            attrSet,
            R.styleable.PreferenceView
        )
        binding.apply {
            nameTv.text = attributes.getString(R.styleable.PreferenceView_setting_title)
            subTv.text = attributes.getString(R.styleable.PreferenceView_setting_subtitle)
            root.background = attributes.getDrawable(R.styleable.PreferenceView_setting_bg)
        }

        attributes.recycle()
    }
}
