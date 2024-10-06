package kr.linkerbell.campusmarket.android

import timber.log.Timber

class CampusMarketDebugApplication : CampusMarketApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
