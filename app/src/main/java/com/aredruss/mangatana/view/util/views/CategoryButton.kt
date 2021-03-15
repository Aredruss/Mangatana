package com.aredruss.mangatana.view.util.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.CategoryButtonBinding

class CategoryButton(
    context: Context,
    val attr: AttributeSet
) : ConstraintLayout(context, attr) {

    var binding: CategoryButtonBinding

    init {
        binding = CategoryButtonBinding.inflate(
            LayoutInflater.from(context),
            this, true
        )
        setAttributes()
    }

    fun setIcon(image: Int) = with(binding) {
        categoryIconIv.setImageDrawable(ContextCompat.getDrawable(context, image))
    }

    fun setText(text: String) = with(binding) {
        categoryNameTv.text = text
    }

    fun setText(text: Int) = with(binding) {
        categoryNameTv.text = root.context.getString(text)
    }

    fun setSubText(subText: String) = with(binding) {
        categorySubTv.text = subText
    }

    fun setSubText(text: Int) = with(binding) {
        categorySubTv.text = root.context.getString(text)
    }

    private fun setAttributes() {
        val attributes = context.obtainStyledAttributes(
            attr,
            R.styleable.CategoryButton
        )
        binding.apply {
            categoryIconIv.setImageDrawable(attributes.getDrawable(R.styleable.CategoryButton_icon))
            categoryNameTv.text = attributes.getString(R.styleable.CategoryButton_text)
            categorySubTv.text = attributes.getString(R.styleable.CategoryButton_subText)
        }
        attributes.recycle()
    }
}
