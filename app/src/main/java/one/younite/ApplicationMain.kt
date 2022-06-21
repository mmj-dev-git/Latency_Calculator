package one.younite

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationMain : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}