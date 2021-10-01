package org.scilab.forge.jlatexmath.android.view

import org.scilab.forge.jlatexmath.share.Box
import org.scilab.forge.jlatexmath.share.BreakFormula
import org.scilab.forge.jlatexmath.share.TeXConstants
import org.scilab.forge.jlatexmath.share.TeXEnvironment
import org.scilab.forge.jlatexmath.share.TeXFont
import org.scilab.forge.jlatexmath.share.TeXFormula
import org.scilab.forge.jlatexmath.share.TeXIcon
import org.scilab.forge.jlatexmath.share.TeXLength
import org.scilab.forge.jlatexmath.share.platform.graphics.Color

class TeXFormulaAndroid : TeXFormula {
    constructor() : super()
    constructor(s: String?, xmlMap: MutableMap<String, String>?) : super(s, xmlMap)
    constructor(s: String?) : super(s)
    constructor(s: String?, textStyle: String?) : super(s, textStyle)

    override fun createTeXIcon(style: Int, size: Double): TeXIcon {
        return TeXIconBuilderAndroid(style, size).build()
    }

    override fun createTeXIcon(style: Int, size: Double, type: Int): TeXIcon {
        return TeXIconBuilderAndroid(
            style = style,
            size = size,
            fontType = type,
        ).build()
    }

    override fun createTeXIcon(style: Int, size: Double, type: Int, fgcolor: Color?): TeXIcon {
        return TeXIconBuilderAndroid(
            style = style,
            size = size,
            fontType = type,
            fgcolor = fgcolor,
        ).build()
    }

    override fun createTeXIcon(style: Int, size: Double, trueValues: Boolean): TeXIcon {
        return TeXIconBuilderAndroid(
            style = style,
            size = size,
            trueValues = trueValues
        ).build()
    }

    override fun createTeXIcon(style: Int, size: Double, align: TeXConstants.Align?): TeXIcon {
        return createTeXIcon(style, size, 0, align);
    }

    override fun createTeXIcon(
        style: Int,
        size: Double,
        type: Int,
        align: TeXConstants.Align?
    ): TeXIcon {
        return TeXIconBuilderAndroid(
            style = style,
            size = size,
            fontType = type,
            align = align,
        ).build()
    }

    inner class TeXIconBuilderAndroid(
        var style: Int,
        var size: Double,
        var fontType: Int? = null,
        var fgcolor: Color? = null,
        var trueValues: Boolean = false,
        var align: TeXConstants.Align? = null,
        var textWidth: TeXLength? = null,
        var baselineSkip: TeXLength? = null,
    ) {
        fun build(): TeXIcon {
            val fontType = fontType
            val textWidth = textWidth
            val baselineSkip = baselineSkip

            val font = if (fontType == null) TeXFont(size) else createFont(size, fontType)
            val te = TeXEnvironment(style, font, textStyle)
            if (textWidth != null) {
                te.lengthSettings().setLength("textwidth", textWidth)
            }
            if (baselineSkip != null) {
                te.lengthSettings().setLength("baselineskip", baselineSkip)
            }
            var box: Box? = createBox(te)
            val ti: TeXIcon
            val textwidth = te.lengthSettings().getLength("textwidth", te)
            if (!java.lang.Double.isInfinite(textwidth) && !java.lang.Double.isNaN(textwidth)) {
                val baselineskip = te.lengthSettings().getLength(
                    "baselineskip",
                    te
                )
                box = BreakFormula.split(box, textwidth, baselineskip, align)
            }
            ti = TeXIconAndroid(box, size, trueValues)
            if (fgcolor != null) {
                ti.setForeground(fgcolor)
            }
            ti.isColored = te.isColored
            return ti
        }
    }

}