package org.scilab.forge.jlatexmath.android.geom

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF
import org.scilab.forge.jlatexmath.share.platform.geom.Area
import org.scilab.forge.jlatexmath.share.platform.geom.Rectangle2D

data class AreaA(
    val path: Path = Path().apply {
        fillType = Path.FillType.EVEN_ODD // TODO ANDROID check this
    },
) : Area {

    override fun getBounds2DX(): Rectangle2D {
        path.computeBounds(rectF, true)
        return rectF.toRectangle2D()
    }

    override fun add(abody: Area) {
        abody as AreaA
        path.addPath(abody.path)
    }

    override fun duplicate(): Area = AreaA(Path(path))

    override fun scale(x: Double) {
        matrix.reset()
        matrix.setScale(x.toFloat(), x.toFloat())
        path.transform(matrix)
    }

    override fun translate(x: Double, y: Double) {
        matrix.reset()
        matrix.setTranslate(x.toFloat(), y.toFloat())
        path.transform(matrix)
    }

    inline fun drawInArea(canvas: Canvas, crossinline drawPath: (path: Path) -> Unit) {
        drawPath(path)
    }

    companion object {
        private val matrix = Matrix()
        private val rectF = RectF()
    }
}