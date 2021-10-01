/**
 * This file is part of the JLaTeXMath library - https://github.com/opencollab/jlatexmath/
 *
 * Copyright (C) 2019 JLaTeXMath developers and GeoGebra Gmbh
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */
package org.scilab.forge.jlatexmath.android.graphics

import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.Paint.Join
import org.scilab.forge.jlatexmath.share.platform.graphics.BasicStroke

data class BasicStrokeA(
    private val width: Float,
    private val cap: Cap?,
    private val join: Join?,
    private val miterLimit: Float,
    private val dashPathEffect: DashPathEffect?
) : BasicStroke {

    constructor(paint: Paint) : this(
        width = paint.strokeWidth,
        cap = paint.strokeCap,
        join = paint.strokeJoin,
        miterLimit = paint.strokeMiter,
        dashPathEffect = paint.pathEffect as? DashPathEffect,
    )

    constructor(width: Double, cap: Int, join: Int, miterLimit: Double) : this(
        width.toFloat(),
        getPaintCap(cap),
        getPaintJoin(join),
        miterLimit.toFloat(),
        dashPathEffect = null
    )

    constructor(width: Double, dashes: FloatArray) : this(
        width.toFloat(),
        getPaintCap(BasicStroke.CAP_BUTT),
        getPaintJoin(BasicStroke.JOIN_MITER),
        miterLimit = 10f,
        DashPathEffect(dashes, 0f)
    )

    fun applyToPaint(paint: Paint) {
        paint.strokeWidth = width
        cap?.also {
            paint.strokeCap = it
        }
        join?.also {
            paint.strokeJoin = it
        }
        // TODO ANDROID: check this...
        paint.strokeMiter = miterLimit
        paint.pathEffect = dashPathEffect
    }

    companion object {
        private fun getPaintCap(cap: Int): Cap? {
            return when (cap) {
                BasicStroke.CAP_BUTT -> Cap.BUTT
                BasicStroke.CAP_ROUND -> Cap.ROUND
                BasicStroke.CAP_SQUARE -> Cap.SQUARE
                else -> null
            }
        }

        private fun getPaintJoin(join: Int): Join? {
            return when (join) {
                BasicStroke.JOIN_MITER -> Join.MITER
                BasicStroke.JOIN_ROUND -> Join.ROUND
                BasicStroke.JOIN_BEVEL -> Join.BEVEL
                else -> null
            }
        }
    }
}