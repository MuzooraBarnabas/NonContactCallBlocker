package xyz.mcomella.noncontactcallblocker

import android.app.Application
import xyz.mcomella.noncontactcallblocker.config.Config
import xyz.mcomella.noncontactcallblocker.db.AppDB

class CallBlockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDB.init(this)
        Config.init(this)
    }
}
