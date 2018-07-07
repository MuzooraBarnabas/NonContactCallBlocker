package xyz.mcomella.noncontactcallblocker

import android.app.Application
import xyz.mcomella.noncontactcallblocker.config.Config

class CallBlockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Config.init(this)
    }
}
