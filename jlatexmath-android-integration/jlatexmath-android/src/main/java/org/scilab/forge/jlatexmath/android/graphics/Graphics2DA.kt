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

import org.scilab.forge.jlatexmath.share.platform.graphics.Graphics2DInterface
import android.graphics.RectF
import org.scilab.forge.jlatexmath.android.geom.AreaA
import org.scilab.forge.jlatexmath.android.geom.ShapeA
import org.scilab.forge.jlatexmath.share.platform.graphics.ImageBase64
import android.graphics.Bitmap
import org.scilab.forge.jlatexmath.share.platform.FactoryProvider
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import org.scilab.forge.jlatexmath.android.font.FontA
import org.scilab.forge.jlatexmath.android.font.FontRenderContextA
import org.scilab.forge.jlatexmath.share.platform.font.Font
import org.scilab.forge.jlatexmath.share.platform.font.FontRenderContext
import org.scilab.forge.jlatexmath.share.platform.geom.Line2D
import org.scilab.forge.jlatexmath.share.platform.geom.Rectangle2D
import org.scilab.forge.jlatexmath.share.platform.geom.RoundRectangle2D
import org.scilab.forge.jlatexmath.share.platform.geom.Shape
import org.scilab.forge.jlatexmath.share.platform.graphics.Color
import org.scilab.forge.jlatexmath.share.platform.graphics.Image
import org.scilab.forge.jlatexmath.share.platform.graphics.Stroke
import org.scilab.forge.jlatexmath.share.platform.graphics.Transform
import org.scilab.forge.jlatexmath.share.platform.graphics.stubs.AffineTransform
import java.lang.Exception
import java.util.ArrayDeque

class Graphics2DA : Graphics2DInterface {

    lateinit var canvas: Canvas

    private val bufferRectF = RectF()
    private val bufferMatrix = Matrix()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.BUTT
        strokeJoin = Paint.Join.MITER
        textSize = 1f // TODO ANDROID
    }
    private lateinit var currentPath: Path
    private val transformStack = ArrayDeque<AffineTransform>()
    private var currentTransform = AffineTransform()

    override fun setStroke(stroke: Stroke) {
        val strokeA = stroke as BasicStrokeA
        strokeA.applyToPaint(paint)
    }

    override fun getStroke(): Stroke {
        return BasicStrokeA(paint)
    }

    override fun setColor(color: Color) {
        paint.color = (color as ColorA).colorInt
    }

    override fun getColor(): Color {
        return ColorA(paint.color)
    }

    override fun getTransform(): Transform {
        return currentTransform
    }

    override fun getFont(): Font {
        return FontA(paint.typeface, paint.textSize)
    }

    override fun setFont(font: Font) {
        font as FontA
        paint.typeface = font.typeface
        paint.textSize = font.size
    }

    override fun fillRect(x: Int, y: Int, width: Int, height: Int) {
        paint.style = Paint.Style.FILL
        canvas.drawRect(
            x.toFloat(),
            y.toFloat(),
            (x + width).toFloat(),
            (y + height).toFloat(),
            paint
        )
    }

    override fun fill(s: Shape) {
        paint.style = Paint.Style.FILL
        if (s is AreaA) {
            s.drawInArea(canvas) { p: Path ->
                canvas.drawPath(p, paint)
            }
        } else if (s is ShapeA) {
            canvas.drawPath(s.path, paint)
        }
        //impl.fill((Shape) s);
    }

    override fun startDrawing() {
        currentPath = Path()
    }

    override fun moveTo(x: Double, y: Double) {
        currentPath.moveTo(x.toFloat(), y.toFloat())
    }

    override fun lineTo(x: Double, y: Double) {
        currentPath.lineTo(x.toFloat(), y.toFloat())
    }

    override fun quadraticCurveTo(x: Double, y: Double, x1: Double, y1: Double) {
        currentPath.quadTo(x.toFloat(), y.toFloat(), x1.toFloat(), y1.toFloat())
    }

    override fun bezierCurveTo(
        x: Double, y: Double, x1: Double, y1: Double,
        x2: Double, y2: Double
    ) {
        currentPath.cubicTo(
            x.toFloat(),
            y.toFloat(),
            x1.toFloat(),
            y1.toFloat(),
            x2.toFloat(),
            y2.toFloat()
        )
    }

    override fun finishDrawing() {
        paint.style = Paint.Style.FILL
        canvas.drawPath(currentPath, paint)
        //impl.fill(path);
    }

    override fun draw(rectangle: Rectangle2D) {
        paint.style = Paint.Style.STROKE
        canvas.drawRect(
            rectangle.x.toFloat(),
            rectangle.y.toFloat(),
            (rectangle.x + rectangle.width).toFloat(),
            (rectangle.y + rectangle.height).toFloat(),
            paint
        )
    }

    override fun draw(rectangle: RoundRectangle2D) {
        paint.style = Paint.Style.STROKE
        bufferRectF.set(
            rectangle.x.toFloat(),
            rectangle.y.toFloat(),
            (rectangle.x + rectangle.width).toFloat(),
            (rectangle.y + rectangle.height).toFloat(),
        )
        canvas.drawRoundRect(
            bufferRectF,
            rectangle.arcW.toFloat(),
            rectangle.arcH.toFloat(),
            paint,
        )
    }

    override fun draw(line: Line2D) {
        paint.style = Paint.Style.STROKE
        canvas.drawLine(
            line.x1.toFloat(),
            line.y1.toFloat(),
            line.x2.toFloat(),
            line.y2.toFloat(),
            paint,
        )
    }

    override fun drawChars(data: CharArray, offset: Int, length: Int, x: Int, y: Int) {
        canvas.drawText(data, offset, length, x.toFloat(), y.toFloat(), paint)
    }

    override fun drawArc(
        x: Int, y: Int, width: Int, height: Int, startAngle: Int,
        arcAngle: Int
    ) {
        paint.style = Paint.Style.STROKE
        bufferRectF[x.toFloat(), y.toFloat(), (x + width).toFloat()] = (y + height).toFloat()
        canvas.drawArc(bufferRectF, startAngle.toFloat(), arcAngle.toFloat(), false, paint!!)
    }

    override fun fillArc(
        x: Int, y: Int, width: Int, height: Int, startAngle: Int,
        arcAngle: Int
    ) {
        paint.style = Paint.Style.FILL
        bufferRectF[x.toFloat(), y.toFloat(), (x + width).toFloat()] = (y + height).toFloat()
        canvas.drawArc(bufferRectF, startAngle.toFloat(), arcAngle.toFloat(), false, paint!!)
    }

    override fun translate(x: Double, y: Double) {
        currentTransform.translate(x, y)
        canvas.translate(x.toFloat(), y.toFloat())
    }

    override fun scale(x: Double, y: Double) {
        currentTransform.scale(x, y)
        canvas.scale(x.toFloat(), y.toFloat())
    }

    override fun rotate(theta: Double, x: Double, y: Double) {
        currentTransform.rotate(theta, x, y)
        canvas.rotate(theta.toFloat(), x.toFloat(), y.toFloat())
    }

    override fun rotate(theta: Double) {
        currentTransform.rotate(theta)
        canvas.rotate(theta.toFloat())
    }

    override fun drawImage(image: Image, x: Int, y: Int) {
        if (image is ImageBase64) {
            canvas.drawBitmap(base64ToBufferedImage(image)!!, x.toFloat(), y.toFloat(), null)
            //			impl.drawImage(base64ToBufferedImage((ImageBase64) image), x, y,
//					null);
        } else {
            canvas.drawBitmap((image as ImageABitmap).bitmap, x.toFloat(), y.toFloat(), null)
            //impl.drawImage((java.awt.Image) image, x, y, null);
        }
    }

    override fun drawImage(image: Image, transform: Transform) {
        setAndroidMatrix(transform, bufferMatrix)
        if (image is ImageBase64) {
            canvas.drawBitmap(base64ToBufferedImage(image)!!, bufferMatrix, null)
            //			impl.drawImage(base64ToBufferedImage((ImageBase64) image),
//					(AffineTransform) transform, null);
        } else {
            canvas.drawBitmap((image as ImageABitmap).bitmap, bufferMatrix, null)
            //			impl.drawImage((java.awt.Image) image, (AffineTransform) transform,
//					null);
        }
    }

    override fun getFontRenderContext(): FontRenderContext {
        return FontRenderContextA(paint)
    }

    override fun dispose() {
        //canvas = null
        //impl.dispose();
    }

    override fun setRenderingHint(key: Int, value: Int) {
//		impl.setRenderingHint(getNativeRenderingKey(key),
//				getNativeRenderingValue(value));
    }

    override fun getRenderingHint(key: Int): Int {
//		Key nKey = getNativeRenderingKey(key);
//		Object val = impl.getRenderingHint(nKey);
        //return getRenderingValue(val);
        return -1
    }

    override fun saveTransformation() {
        canvas.save()
        transformStack.add(currentTransform.createClone() as AffineTransform)
    }

    override fun restoreTransformation() {
        canvas.restore()
        currentTransform = transformStack.removeLast()
    }

    // todo android delete
    fun setAndroidMatrix(transform: Transform, output: Matrix) {
        output.reset()
        output.setTranslate(
            transform.translateX.toFloat(), transform.translateY.toFloat()
        )
        output.setSkew(transform.shearX.toFloat(), transform.shearY.toFloat())
        output.setScale(transform.scaleX.toFloat(), transform.scaleY.toFloat())
    }

    companion object {
        private fun base64ToBufferedImage(image: ImageBase64): Bitmap? {
            var pngBase64 = image.base64
            val pngMarker = "data:image/png;base64,"
            pngBase64 = if (pngBase64.startsWith(pngMarker)) {
                pngBase64.substring(pngMarker.length)
            } else {
                FactoryProvider.debugS("invalid base64 image")
                return null
            }
            val imageData = Base64.decode(pngBase64)
            return try {
                BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            } catch (e: Exception) {
                null
            }
        }

        //	private static Key getNativeRenderingKey(int key) {
        //		switch (key) {
        //		case RenderingHints.KEY_ANTIALIASING:
        //			return java.awt.RenderingHints.KEY_ANTIALIASING;
        //		case RenderingHints.KEY_RENDERING:
        //			return java.awt.RenderingHints.KEY_RENDERING;
        //		case RenderingHints.KEY_TEXT_ANTIALIASING:
        //			return java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
        //		default:
        //			return null;
        //		}
        //	}
        private fun getNativeRenderingValue(value: Int): Any? {
//		switch (value) {
//		case RenderingHints.VALUE_ANTIALIAS_ON:
//			return java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
//		case RenderingHints.VALUE_RENDER_QUALITY:
//			return java.awt.RenderingHints.VALUE_RENDER_QUALITY;
//		case RenderingHints.VALUE_TEXT_ANTIALIAS_ON:
//			return java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
//		default:
//			return null;
//		}
            return null
        }

        private fun getRenderingValue(value: Any): Int {
//		if (value == java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
//			return RenderingHints.VALUE_INTERPOLATION_BICUBIC;
//		} else if (value == java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
//			return RenderingHints.VALUE_INTERPOLATION_BILINEAR;
//		} else if (value == java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
//			return RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
//		} else {
//			return -1;
//		}
            return -1
        }
    }
}