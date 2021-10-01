package org.scilab.forge.jlatexmath.android

import android.app.Application

class App : Application() {
    init {
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: App
    }
}
