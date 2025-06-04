package com.youkydesign.recipeapp.feature.discovery.ui.discovery

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.DrawableCompat
import com.youkydesign.recipeapp.R

class CustomAssistChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var chipText: String? = "Assist Chip"
    private var twSlate400: Int = ResourcesCompat.getColor(resources, R.color.tw_slate_950, null)
    private var chipIcon: Drawable? = null
    private var chipIconTint: Int = twSlate400
    private var twSlate200: Int = ResourcesCompat.getColor(resources, R.color.tw_slate_200, null)
    private var outlineWidthVal: Float = 4f
    private var cornerRadiusVal: Float = 32f

    private val textPaint: TextPaint
    private val outlinePaint: Paint
    private val contentRect = RectF()
    private val textBounds = Rect()
    private var iconSize = 24f
    private var iconPadding = 16f
    private var textPaddingStart = 0f
    private var textPaddingEnd = 24f
    private var verticalPadding = 32f

    init {
        attrs?.let {
            context.withStyledAttributes(
                it,
                R.styleable.CustomOutlinedAssistChip,
                defStyleAttr,
                0
            ) {

                chipText = getString(R.styleable.CustomOutlinedAssistChip_android_text) ?: chipText

                getDrawable(R.styleable.CustomOutlinedAssistChip_chipIcon)?.let {
                    chipIcon =
                        it.mutate()
                }
                if (hasValue(R.styleable.CustomOutlinedAssistChip_chipIconTint)) {
                    chipIconTint =
                        getColor(R.styleable.CustomOutlinedAssistChip_chipIconTint, twSlate400)
                }

                outlineWidthVal = getDimension(
                    R.styleable.CustomOutlinedAssistChip_outlineWidth,
                    outlineWidthVal
                )
                cornerRadiusVal = getDimension(
                    R.styleable.CustomOutlinedAssistChip_cornerRadius,
                    cornerRadiusVal
                )
            }
        }

        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = twSlate400
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics
            )
            textAlign = Paint.Align.LEFT
        }

        outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = twSlate200
            strokeWidth = outlineWidthVal
        }

        iconSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics // Default icon size
        )
        iconPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        )
        textPaddingStart = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        )
        textPaddingEnd = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics
        )
        verticalPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        )

        chipIconTint.let { tint ->
            chipIcon?.let { icon ->
                DrawableCompat.setTint(icon, tint)
            }
        }

        isClickable = true
        isFocusable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(chipText ?: "", 0, chipText?.length ?: 0, textBounds)

        var contentWidth = 0f

        if (chipIcon != null) {
            contentWidth += iconSize + iconPadding
        }

        contentWidth += textPaddingStart + textBounds.width() + textPaddingEnd

        val desiredWidth = paddingLeft + paddingRight + contentWidth.toInt()

        val width = desiredWidth
        val fixedHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            32f,
            resources.displayMetrics
        ).toInt()

        setMeasuredDimension(width, fixedHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        contentRect.set(
            outlineWidthVal / 2f,
            outlineWidthVal / 2f,
            width - outlineWidthVal / 2f,
            height - outlineWidthVal / 2f
        )

        canvas.drawRoundRect(contentRect, cornerRadiusVal, cornerRadiusVal, outlinePaint)

        var currentX = paddingLeft + (if (chipIcon != null) iconPadding else textPaddingStart)

        chipIcon?.let {
            val iconTop = (height - iconSize) / 2f
            it.setBounds(
                currentX.toInt(),
                iconTop.toInt(),
                (currentX + iconSize).toInt(),
                (iconTop + iconSize).toInt()
            )
            it.draw(canvas)
            currentX += iconSize + textPaddingStart
        }

        chipText?.let {
            val textY = (height / 2f) - (textBounds.exactCenterY())
            canvas.drawText(it, currentX, textY, textPaint)
        }
    }

    fun setText(text: String?) {
        chipText = text
        requestLayout()
        invalidate()
    }

    fun setChipIcon(drawable: Drawable?) {
        chipIcon = drawable?.mutate()
        chipIconTint.let { tint -> chipIcon?.let { icon -> DrawableCompat.setTint(icon, tint) } }
        requestLayout()
        invalidate()
    }

    fun setOutlineColor(color: Int) {
        twSlate200 = color
        outlinePaint.color = color
        invalidate()
    }

}