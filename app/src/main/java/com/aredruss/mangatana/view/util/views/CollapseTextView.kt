package com.aredruss.mangatana.view.util.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.CollapseTextViewBinding

class CollapseTextView : ConstraintLayout {

    private var isExpanded: Boolean = false
    private var expandDuration: Int = -1

    companion object {
        const val defaultExpandValue = 300
        val defaultAngles = listOf(0f, 90.0F, 180F)
    }

    var binding: CollapseTextViewBinding = CollapseTextViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    constructor(
        context: Context,
        attributeSet: AttributeSet
    ) : super(context, attributeSet) {
        bind()
        setAttributes(attributeSet)
    }

    fun setTitleText(text: String) = with(binding) {
        titleTv.text = text
    }

    fun setTitleText(stringId: Int) = with(binding) {
        titleTv.text = context.getString(stringId)
    }

    fun setContentText(text: String) = with(binding) {
        contentTv.text = text
    }

    fun setContentText(stringId: Int) = with(binding) {
        contentTv.text = context.getString(stringId)
    }

    fun setIcon(resId: Int) = with(binding) {
        arrowIv.setImageResource(resId)
    }

    fun setIcon(drawable: Drawable) = with(binding) {
        arrowIv.setImageDrawable(drawable)
    }

    private fun bind() = with(binding) {
        isExpanded = false
        collapse(true)

        panelCl.setOnClickListener {
            onClick()
        }

        contentTv.setOnClickListener {
            onClick()
        }
    }

    private fun onClick() {
        if (isExpanded) {
            collapse(true)
        } else expand(true)
    }

    private fun setAttributes(attributeSet: AttributeSet) = with(binding) {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CollapseTextView)
        val typeValue = TypedValue()

        attributes.getValue(R.styleable.CollapseTextView_title_text, typeValue)
        titleTv.text = when (typeValue.type) {
            TypedValue.TYPE_STRING -> typeValue.string as String?
            TypedValue.TYPE_REFERENCE -> resources.getString(typeValue.resourceId)
            else -> null
        }

        attributes.getValue(R.styleable.CollapseTextView_content_text, typeValue)
        contentTv.text = when (typeValue.type) {
            TypedValue.TYPE_STRING -> typeValue.string as String?
            TypedValue.TYPE_REFERENCE -> resources.getString(typeValue.resourceId)
            else -> null
        }

        mainCl.setBackgroundResource(
            attributes.getResourceId(R.styleable.CollapseTextView_bg_drawable_regular, -1)
        )

        arrowIv.setImageDrawable(attributes.getDrawable(R.styleable.CollapseTextView_arrow_drawable))

        expandDuration = attributes.getInteger(
            R.styleable.CollapseTextView_expand_duration,
            defaultExpandValue
        )

        attributes.recycle()
    }

    private fun expand(animate: Boolean) {
        if (isExpanded) {
            return
        }
        expandInternal(animate)
    }

    private fun collapse(animate: Boolean) {
        if (!isExpanded) {
            return
        }
        collapseInternal(animate)
    }

    private fun expandInternal(animate: Boolean) = with(binding) {
        contentTv.visibility = View.VISIBLE
        setArrowIvState(true, animate)
        isExpanded = true
    }

    private fun collapseInternal(animate: Boolean) = with(binding) {
        contentTv.visibility = View.GONE
        setArrowIvState(false, animate)
        isExpanded = false
    }

    private fun setArrowIvState(expand: Boolean, animate: Boolean) {
        val angle = if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            if (expand) defaultAngles[1] else defaultAngles[2]
        } else {
            if (expand) defaultAngles[1] else defaultAngles[0]
        }

        binding.arrowIv.animate()
            .rotation(angle)
            .setDuration((if (animate) expandDuration else 0).toLong())
            .start()
    }
}
