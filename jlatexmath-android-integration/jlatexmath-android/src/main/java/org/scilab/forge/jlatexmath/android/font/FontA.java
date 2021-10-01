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
package org.scilab.forge.jlatexmath.android.font;

import android.annotation.SuppressLint;
import android.graphics.Path;
import android.graphics.Typeface;

import org.scilab.forge.jlatexmath.android.geom.ShapeA;
import org.scilab.forge.jlatexmath.share.CharFont;
import org.scilab.forge.jlatexmath.share.platform.font.Font;
import org.scilab.forge.jlatexmath.share.platform.font.FontRenderContext;
import org.scilab.forge.jlatexmath.share.platform.font.TextAttribute;
import org.scilab.forge.jlatexmath.share.platform.geom.Shape;

import java.util.Locale;
import java.util.Map;

public class FontA implements Font {

    public final Typeface typeface;
    public final float size;

    public FontA(Typeface typeface, float size) {
        this.typeface = typeface;
        this.size = size;
    }

	public FontA(String name, int style, int size) {
        this.typeface = createTypeface(name, style);
        this.size = size;
	}

    private FontA(FontA fontA, int style, float size) {
        this.typeface = applyStyle(fontA.typeface, style);
        this.size = size;
    }

	@Override
	public Font deriveFont(int type) {
		return new FontA(this, type, size);
	}

	@Override
	public Font deriveFont(Map<TextAttribute, Object> map) {
		return this;
	}

	@Override
	public boolean isEqual(Font f) {
	    if (!(f instanceof FontA)) {
	        return false;
        }
	    FontA fA = (FontA) f;

		return (typeface == fA.typeface || (typeface != null && typeface.equals(fA.typeface))) && size == fA.size;
	}

	@Override
	public int getScale() {
		return 1; // TODO ANDROID
	}

	@Override
	public Shape getGlyphOutline(FontRenderContext frc, CharFont cf) {
        FontRenderContextA frcA = (FontRenderContextA) frc;
        Path glyphPath = new Path();
        frcA.getPaint().getTextPath(cf.c + "", 0, 1, 0, 0, glyphPath);
		return new ShapeA(glyphPath);
	}

	@Override
	public boolean canDisplay(char ch) {
	    return true;
		//return impl.canDisplay(ch);
	}

	@Override
	public boolean canDisplay(int c) {
	    return true;
		//return impl.canDisplay(c);
	}

    private static Typeface applyStyle(Typeface typeface, int style) {
        final int current = (typeface.isBold() ? BOLD : 0) | (typeface.isItalic() ? ITALIC : 0);
        if (current != style) {
            // both will be 3 (BOLD_ITALIC)
            @SuppressLint("WrongConstant") final int out = ((style & BOLD) != 0 ? BOLD : 0) | ((style & ITALIC) != 0 ? ITALIC : 0);
            typeface = Typeface.create(typeface, out);
        }

        return typeface;
    }

    private static Typeface createTypeface(String name, int style) {
        Typeface typeface = Typeface.create(name.toLowerCase(Locale.US), toAndroidStyle(style));
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }

    private static int toAndroidStyle(int style) {
        if (style == PLAIN) {
            return Typeface.NORMAL;
        }
        return ((style & BOLD) != 0 ? Typeface.BOLD : 0)
                | ((style & ITALIC) != 0 ? Typeface.ITALIC : 0);
    }
}

