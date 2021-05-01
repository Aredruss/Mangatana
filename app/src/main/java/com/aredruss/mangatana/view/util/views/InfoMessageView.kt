package com.aredruss.mangatana.view.util.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.InfoViewBinding

class InfoMessageView(
    context: Context,
    private val attr: AttributeSet
) : ConstraintLayout(context, attr) {

    var binding: InfoViewBinding

    init {
        binding = InfoViewBinding.inflate(
            LayoutInflater.from(context),
            this, true
        )
        setAttributes()
    }

    fun setIcon(image: Int) = with(binding) {
        iconIv.setImageDrawable(ContextCompat.getDrawable(context, image))
    }

    fun setText(text: String) = with(binding) {
        messageTv.text = root.context.getString(R.string.error_message, text)
    }

    fun setText(text: Int) = with(binding) {
        messageTv.text = root.context.getString(text)
    }

    private fun setAttributes() {
        val attributes = context.obtainStyledAttributes(
            attr,
            R.styleable.InfoMessageView
        )
        binding.apply {
            iconIv.setImageDrawable(attributes.getDrawable(R.styleable.InfoMessageView_info_icon))
            messageTv.text = attributes.getString(R.styleable.InfoMessageView_info_text)
        }
        attributes.recycle()
    }
}
