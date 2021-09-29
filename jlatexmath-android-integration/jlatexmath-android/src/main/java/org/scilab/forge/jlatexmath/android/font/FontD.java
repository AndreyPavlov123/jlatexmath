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
import android.graphics.Typeface;

import org.scilab.forge.jlatexmath.share.CharFont;
import org.scilab.forge.jlatexmath.share.platform.font.Font;
import org.scilab.forge.jlatexmath.share.platform.font.FontLoader;
import org.scilab.forge.jlatexmath.share.platform.font.FontRenderContext;
import org.scilab.forge.jlatexmath.share.platform.font.GlyphVector;
import org.scilab.forge.jlatexmath.share.platform.font.TextAttribute;
import org.scilab.forge.jlatexmath.share.platform.geom.Shape;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FontA implements Font {

    private final Typeface typeface;
    private final String name;
    private final int style;
    private final float size;

//    public static Font createFont(Typeface typeface, float size) {
//        return new FontA(typeface, 0, size);
//    }

	public FontA(String name, int style, int size) {
        this.typeface = createTypeface(name, style);
        this.name = name;
        this.style = style;
        this.size = size;
	}

    private FontA(FontA fontA, int style, float size) {
        this.typeface = applyStyle(fontA.typeface, style);
        this.name = fontA.name;
        this.style = style;
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
		return equalsImpl(this.name, fA.name)
                && equalsImpl(this.style, fA.style)
                && equalsImpl(this.size, fA.size);
	}

	@Override
	public int getScale() {
		return 1; // TODO ANDROID
	}

	public GlyphVector createGlyphVector(FontRenderContext frc, String s) {
		return new GlyphVectorD(impl
				.createGlyphVector((java.awt.font.FontRenderContext) frc, s));
	}

	@Override
	public Shape getGlyphOutline(FontRenderContext frc, CharFont cf) {
		return createGlyphVector(frc, cf.c + "").getGlyphOutline(0);
	}

	public int getSize() {
		return impl.getSize();
	}

	public String getName() {
		return impl.getName();
	}

	@Override
	public boolean canDisplay(char ch) {
		return impl.canDisplay(ch);
	}

	@Override
	public boolean canDisplay(int c) {
		return impl.canDisplay(c);
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
        Typeface.create()
        return typeface;
    }

    private static int toAndroidStyle(int style) {
        if (style == PLAIN) {
            return Typeface.NORMAL;
        }
        return ((style & BOLD) != 0 ? Typeface.BOLD : 0)
                | ((style & ITALIC) != 0 ? Typeface.ITALIC : 0);
    }

    private static boolean equalsImpl(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}

public class Fonqwt {

    public static final int PLAIN = 0;

    public static final int BOLD = 1;

    public static final int ITALIC = 2;

    @Deprecated
    public static Font createFont(int truetypeFont, InputStream fontIn) {
        return null;
    }

    @NonNull
    public static Font createFont(@NonNull Typeface typeface, float size) {
        return new Font(typeface, 0, size);
    }

    private final Typeface typeface;
    private int style;
    private float size;

    public Font(String name, int style, int size) {
        this(createTypeface(name, style), style, size);
    }

    private Font(@NonNull Typeface typeface, int style, float size) {
        this.typeface = applyStyle(typeface, style);
        this.style = style;
        this.size = size;
    }

    @NonNull
    private static Typeface applyStyle(@NonNull Typeface typeface, int style) {
        final int current = (typeface.isBold() ? BOLD : 0) | (typeface.isItalic() ? ITALIC : 0);
        if (current != style) {
            // both will be 3 (BOLD_ITALIC)
            @SuppressLint("WrongConstant") final int out = ((style & BOLD) != 0 ? BOLD : 0) | ((style & ITALIC) != 0 ? ITALIC : 0);
            typeface = Typeface.create(typeface, out);
        }

        return typeface;
    }

    public Font deriveFont(int type) {
        return new Font(typeface, type, size);
    }

    public Typeface typeface() {
        return typeface;
    }

    public int style() {
        return style;
    }

    public float size() {
        return size;
    }

    public boolean isBold() {
        return (style & BOLD) != 0;
    }

    public boolean isItalic() {
        return (style & ITALIC) != 0;
    }

    @NonNull
    private static Typeface createTypeface(@NonNull String name, int style) {
        Typeface typeface = Typeface.create(name.toLowerCase(Locale.US), toAndroidStyle(style));
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }

    // @since 0.1.2
    private static int toAndroidStyle(int style) {
        if (style == PLAIN) {
            return Typeface.NORMAL;
        }
        return ((style & BOLD) != 0 ? Typeface.BOLD : 0)
                | ((style & ITALIC) != 0 ? Typeface.ITALIC : 0);
    }
}

