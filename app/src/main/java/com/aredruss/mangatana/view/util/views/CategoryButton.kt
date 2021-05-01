package com.aredruss.mangatana.view.util.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.CategoryButtonBinding

class CategoryButton(
    private val ctx: Context,
    private val attrSet: AttributeSet
) : ConstraintLayout(ctx, attrSet) {

    var binding: CategoryButtonBinding = CategoryButtonBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    init {
        setAttributes()
    }

    fun setIcon(image: Int) = with(binding) {
        iconIv.setImageDrawable(ContextCompat.getDrawable(context, image))
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

    private fun setAttributes() {
        val attributes = context.obtainStyledAttributes(
            attrSet,
            R.styleable.CategoryButton
        )
        binding.apply {
            iconIv.setImageDrawable(attributes.getDrawable(R.styleable.CategoryButton_icon))
            nameTv.text = attributes.getString(R.styleable.CategoryButton_text)
            subTv.text = attributes.getString(R.styleable.CategoryButton_subText)
        }
        attributes.recycle()
    }
}
