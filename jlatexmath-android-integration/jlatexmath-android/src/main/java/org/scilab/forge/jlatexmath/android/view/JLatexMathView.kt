package org.scilab.forge.jlatexmath.android.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import org.scilab.forge.jlatexmath.android.graphics.ColorA
import org.scilab.forge.jlatexmath.share.TeXConstants
import org.scilab.forge.jlatexmath.share.TeXIcon
import org.scilab.forge.jlatexmath.share.TeXLength
import org.scilab.forge.jlatexmath.share.Unit
import kotlin.math.min

class JLatexMathView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    var fitBoundsMode = FitBoundsMode.AUTO_LINE_BREAKING_THEN_SCALING
        set(value) {
            if (value == field)
                return
            field = value
            resetTextIconBuilder()
            requestLayout()
        }

    var textSize: Float = 0f
        set(value) {
            if (value == field)
                return
            field = value
            resetTextIconBuilder()
            requestLayout()
        }

    var textColor: Int = Color.BLACK
        set(value) {
            if (value == field)
                return
            field = value
            resetTextIconBuilder()
            requestLayout()
        }

    var formulaBackground: Drawable? = null
        set(value) {
            if (value == field)
                return
            field = value
            resetJLatexMathDrawable()
            requestLayout()
        }

    var alignVertical = Align.ALIGN_START
        private set

    var alignHorizontal = Align.ALIGN_START
        private set

    var latex: String? = null
        set(value) {
            if (value == field)
                return
            field = value
            resetTextIconBuilder()
            requestLayout()
        }

    val teXIcon: TeXIcon
        get() {
            if (cachedTeXIcon == null) {
                cachedTeXIcon = textIconBuilder.build()
            }
            return cachedTeXIcon!!
        }

    private val textIconBuilder: TeXFormulaAndroid.TeXIconBuilderAndroid
        get() {
            if (cachedTeXIconBuilder == null) {
                cachedTeXIconBuilder = TeXFormulaAndroid(latex)
                    .TeXIconBuilderAndroid(
                        fgcolor = ColorA(textColor),
                        size = textSize.toDouble(),
                        style = TeXConstants.STYLE_DISPLAY
                    )
            }
            return cachedTeXIconBuilder!!
        }

    private val jLatexMathDrawable: JLatexMathDrawable
        get() {
            if (cachedJLatexMathDrawable == null) {
                cachedJLatexMathDrawable = JLatexMathDrawable.builder(teXIcon)
                    .setBackground(formulaBackground)
                    .build()
            }
            return cachedJLatexMathDrawable!!
        }

    private var cachedTeXIcon: TeXIcon? = null
    private var cachedTeXIconBuilder: TeXFormulaAndroid.TeXIconBuilderAndroid? = null
    private var cachedJLatexMathDrawable: JLatexMathDrawable? = null
    private var scale = 0f
    private var left = 0f
    private var top = 0f
    private var previousWidthMeasureSpec = -1

    fun setAlign(alignVertical: Align, alignHorizontal: Align) {
        this.alignVertical = alignVertical
        this.alignHorizontal = alignHorizontal
        requestLayout()
    }

    fun clear() {
        latex = null
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (latex == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        when (fitBoundsMode) {
            FitBoundsMode.AUTO_LINE_BREAKING_THEN_SCALING,
            FitBoundsMode.AUTO_LINE_BREAKING -> if (previousWidthMeasureSpec != widthMeasureSpec) {
                previousWidthMeasureSpec = widthMeasureSpec
                if (MeasureSpec.UNSPECIFIED == widthMode) {
                    resetTextIconBuilder()
                } else {
                    textIconBuilder.textWidth = TeXLength(Unit.PIXEL, widthSize.toDouble())
                    textIconBuilder.align = TeXConstants.Align.LEFT
                    textIconBuilder.baselineSkip = TeXLength(Unit.EX, 1.5)
                    textIconBuilder.trueValues = false
//                    textIconBuilder.setWidth(TeXConstants.UNIT_PIXEL, widthSize.toFloat(), TeXConstants.ALIGN_LEFT)
//                    textIconBuilder.setInterLineSpacing(TeXConstants.UNIT_EX, 1.5f)
//                    textIconBuilder.setIsMaxWidth(true)
//                    textIconBuilder.setTrueValues(false)
                    resetTeXIcon()
                }
            }
            FitBoundsMode.SCALING,
            FitBoundsMode.NONE -> {
                // Nothing...
            }
        }

        val drawableWidth = jLatexMathDrawable.intrinsicWidth
        val drawableHeight = jLatexMathDrawable.intrinsicHeight
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop

        // okay, a dimension:
        //  if it's exact -> use it
        //  if it's not -> use smallest value of AT_MOST, (drawable.intrinsic + padding)
        var width: Int
        var height: Int
        width = if (MeasureSpec.EXACTLY == widthMode) {
            widthSize
        } else {
            val wrap = drawableWidth + paddingLeft + paddingRight
            if (widthSize > 0) min(widthSize, wrap) else wrap
        }
        height = if (MeasureSpec.EXACTLY == heightMode) {
            heightSize
        } else {
            val wrap = drawableHeight + paddingTop + paddingBottom
            if (heightSize > 0) min(heightSize, wrap) else wrap
        }
        val canvasWidth = width - paddingLeft - paddingRight
        val canvasHeight = height - paddingTop - paddingBottom
        val scale: Float = when (fitBoundsMode) {
            FitBoundsMode.AUTO_LINE_BREAKING_THEN_SCALING,
            FitBoundsMode.SCALING ->
                // now, let's see if these dimensions change our drawable scale (we need modify it to fit)
                if (drawableWidth < canvasWidth && drawableHeight < canvasHeight) {
                    // we do not need to modify drawable
                    1f
                } else {
                    min(canvasWidth.toFloat() / drawableWidth, canvasHeight.toFloat() / drawableHeight)
                }
            FitBoundsMode.AUTO_LINE_BREAKING,
            FitBoundsMode.NONE -> 1f
        }
        val displayWidth = (drawableWidth * scale + .5f).toInt()
        val displayHeight = (drawableHeight * scale + .5f).toInt()
        if (MeasureSpec.EXACTLY != widthMode) {
            width = displayWidth + paddingLeft + paddingRight
        }
        if (MeasureSpec.EXACTLY != heightMode) {
            height = displayHeight + paddingTop + paddingBottom
        }

        // let's see if we should align our formula
        val left = alignment(alignHorizontal, (width - paddingLeft - paddingRight - displayWidth).toFloat())
        val top = alignment(alignVertical, (height - paddingTop - paddingBottom - displayHeight).toFloat())
        this.scale = scale
        this.left = paddingLeft + left
        this.top = paddingTop + top
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (latex == null) {
            return
        }
        val save = canvas.save()
        try {
            if (left > .0f) {
                canvas.translate(left, 0f)
            }
            if (top > .0f) {
                canvas.translate(0f, top)
            }
            when (fitBoundsMode) {
                FitBoundsMode.AUTO_LINE_BREAKING_THEN_SCALING,
                FitBoundsMode.SCALING -> if (scale > .0f && scale.compareTo(1f) != 0) {
                    canvas.scale(scale, scale)
                }
                FitBoundsMode.AUTO_LINE_BREAKING,
                FitBoundsMode.NONE -> {
                    // Nothing...
                }
            }
            jLatexMathDrawable.draw(canvas)
        } finally {
            canvas.restoreToCount(save)
        }
    }

    private fun resetTextIconBuilder() {
        cachedTeXIconBuilder = null
        resetTeXIcon()
    }

    private fun resetTeXIcon() {
        cachedTeXIcon = null
        resetJLatexMathDrawable()
    }

    private fun resetJLatexMathDrawable() {
        cachedJLatexMathDrawable = null
    }

    enum class FitBoundsMode {
        AUTO_LINE_BREAKING_THEN_SCALING,
        AUTO_LINE_BREAKING,
        SCALING,
        NONE,
    }

    enum class Align {
        ALIGN_START,
        ALIGN_CENTER,
        ALIGN_END,
    }

    companion object {
        private fun alignment(align: Align, difference: Float): Float {
            return when (align) {
                Align.ALIGN_START -> .0f
                Align.ALIGN_CENTER -> difference / 2
                Align.ALIGN_END -> difference
            }
        }
    }
}