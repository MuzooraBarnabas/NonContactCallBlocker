package xyz.mcomella.noncontactcallblocker

import android.app.Application

class CallBlockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Config.init(this)
    }
}
