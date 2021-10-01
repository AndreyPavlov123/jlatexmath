package org.scilab.forge.jlatexmath.android.view

import org.scilab.forge.jlatexmath.share.Box
import org.scilab.forge.jlatexmath.share.TeXIcon

class TeXIconAndroid : TeXIcon {
    constructor(b: Box?, size: Double) : super(b, size)
    constructor(b: Box?, size: Double, trueValues: Boolean) : super(b, size, trueValues)
}