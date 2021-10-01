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
package org.scilab.forge.jlatexmath.android.font

import android.content.Context
import android.graphics.Typeface
import org.scilab.forge.jlatexmath.share.exception.ResourceParseException
import org.scilab.forge.jlatexmath.share.platform.FactoryProvider
import org.scilab.forge.jlatexmath.share.platform.font.Font
import org.scilab.forge.jlatexmath.share.platform.font.FontLoader
import java.io.InputStream

class FontLoaderA(val context: Context) : FontLoader {

    @Throws(ResourceParseException::class)
    override fun loadFont(name: String): Font {
        FactoryProvider.debugS("loadFont():$name")
        return try {
            val typeface = loadTypeface(name)
            FontA(typeface, 1f)//, FontLoader.FONT_SCALE_FACTOR.toFloat()) // TODO ANDROID
        } catch (e: Throwable) {
            throw ResourceParseException(
                "FontLoader" + ": FontLoader '"
                        + name + "'. Error message: " + e.message
            )
        }
    }

    private fun getResourceAsStream(path: String): InputStream {
        return context.assets.open(BASE + path)
    }

    private fun loadTypeface(path: String): Typeface {
        return Typeface.createFromAsset(context.assets, BASE + path)
    }

    companion object {
        private const val BASE = "org/scilab/forge/jlatexmath/"
    }
}
