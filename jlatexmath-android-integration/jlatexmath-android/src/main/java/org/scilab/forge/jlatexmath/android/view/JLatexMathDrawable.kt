package org.scilab.forge.jlatexmath.android.view

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import org.scilab.forge.jlatexmath.android.graphics.Graphics2DA
import org.scilab.forge.jlatexmath.share.TeXIcon
import org.scilab.forge.jlatexmath.share.platform.graphics.Insets
import kotlin.math.min

class JLatexMathDrawable constructor(builder: Builder) : Drawable() {

    /**
     * @since 0.1.1
     */
    val icon: TeXIcon = builder.teXIcon

    private val align: Align?
    private val background: Drawable?
    private val graphics2D: Graphics2DA
    private val iconWidth: Int
    private val iconHeight: Int

    init {
        val insets = builder.insets
        if (insets != null) {
            icon.insets = insets
        }
        align = builder.align
        background = builder.background
        graphics2D = Graphics2DA()
        iconWidth = icon.iconWidth
        iconHeight = icon.iconHeight
        setBounds(0, 0, iconWidth, iconHeight)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        if (background != null) {
            background.bounds = bounds
        }
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val save = canvas.save()
        try {
            // draw background before _possibly_ modifying latex (we should not modify background,
            //  it should always be the bounds we received)
            background?.draw(canvas)
            val w = bounds.width()
            val h = bounds.height()
            val scale = if (iconWidth > w || iconHeight > h) {
                min(w.toFloat() / iconWidth, h.toFloat() / iconHeight)
            } else {
                1f
            }
            val targetW = (iconWidth * scale + 0.5f).toInt()
            val targetH = (iconHeight * scale + 0.5f).toInt()
            val top = (h - targetH) / 2
            val left = when (align) {
                Align.ALIGN_CENTER -> (w - targetW) / 2
                Align.ALIGN_RIGHT -> w - targetW
                Align.ALIGN_LEFT -> 0
                else -> 0
            }
            if (top != 0 || left != 0) {
                canvas.translate(left.toFloat(), top.toFloat())
            }
            if (scale.compareTo(1f) != 0) {
                canvas.scale(scale, scale)
            }
            graphics2D.canvas = canvas
            icon.paintIcon(null, graphics2D, 0, 0)
        } finally {
            canvas.restoreToCount(save)
        }
    }

    override fun setAlpha(alpha: Int) {
        // no op
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // no op
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun getIntrinsicWidth(): Int {
        return iconWidth
    }

    override fun getIntrinsicHeight(): Int {
        return iconHeight
    }

    enum class Align {
        ALIGN_LEFT,
        ALIGN_CENTER,
        ALIGN_RIGHT
    }

    class Builder(val teXIcon: TeXIcon) {
        var align: Align? = null
            private set
        var background: Drawable? = null
            private set
        var insets: Insets? = null
            private set

        fun setAlign(align: Align?): Builder {
            this.align = align
            return this
        }

        fun setBackground(background: Drawable?): Builder {
            this.background = background
            return this
        }

        fun setBackground(backgroundColor: Int): Builder {
            background = ColorDrawable(backgroundColor)
            return this
        }

        fun setPadding(padding: Int): Builder {
            insets = Insets(padding, padding, padding, padding)
            return this
        }

        fun setPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            insets = Insets(top, left, bottom, right)
            return this
        }

        fun build(): JLatexMathDrawable {
            return JLatexMathDrawable(this)
        }
    }

    companion object {
        fun builder(teXIcon: TeXIcon): Builder {
            return Builder(teXIcon)
        }
    }
}