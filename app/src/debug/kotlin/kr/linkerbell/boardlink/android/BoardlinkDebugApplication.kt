package kr.linkerbell.boardlink.android

import timber.log.Timber

class BoardlinkDebugApplication : BoardlinkApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
