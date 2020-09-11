package io.github.fabiantauriello.moviecatalog.app

import android.app.Application
import android.content.Context

/**
 * I've extended the application so I can get the application context from
 * anywhere in the app through the companion object below.
 */
class MovieCatalog : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MovieCatalog? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

}