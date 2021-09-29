package org.scilab.forge.jlatexmath.android.geom

import android.graphics.Path
import android.graphics.RectF
import org.scilab.forge.jlatexmath.share.platform.geom.Shape

class ShapeA(
    val path: Path = Path().apply {
        fillType = Path.FillType.EVEN_ODD // TODO ANDROID check this
    }
) : Shape {

    private val _bounds2DX by lazy {
        path.computeBounds(rectF, true)
        rectF.toRectangle2D()
    }

    override fun getBounds2DX() = _bounds2DX

    companion object {
        private val rectF = RectF()
    }
}