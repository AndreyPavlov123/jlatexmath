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
package org.scilab.forge.jlatexmath.android.graphics;

import org.scilab.forge.jlatexmath.share.platform.graphics.Color;

public class ColorA implements Color {

    public static final Color black = new ColorA(0xFF000000);
    public static final Color white = new ColorA(0xFFffffff);
    public static final Color red = new ColorA(0xFFff0000);
    public static final Color green = new ColorA(0xFF00ff00);
    public static final Color blue = new ColorA(0xFF0000ff);
    public static final Color cyan = new ColorA(android.graphics.Color.parseColor("cyan"));
    public static final Color magenta = new ColorA(android.graphics.Color.parseColor("magenta"));
    public static final Color yellow = new ColorA(android.graphics.Color.parseColor("yellow"));

    public static final Color BLACK = black;
    public static final Color RED = red;

    public static Color decode(String s) {
        return new ColorA(android.graphics.Color.parseColor(s));
    }

    private final int color;

    public ColorA(int color) {
        this.color = color;
    }

    public ColorA(int r, int g, int b) {
        this(android.graphics.Color.rgb(r, g, b));
    }

    public ColorA(float r, float g, float b) {
        this(
                (int) (r * 255 + .5F),
                (int) (g * 255 + .5F),
                (int) (b * 255 + .5F)
        );
    }

    public int getRed() {
        return android.graphics.Color.red(color);
    }

    public int getBlue() {
        return android.graphics.Color.blue(color);
    }

    public int getGreen() {
        return android.graphics.Color.green(color);
    }

    public int getAlpha() {
        return 255;
    }

    public int getColorInt() {
        return color;
    }
}
