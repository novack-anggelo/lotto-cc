package com.novack.lotto_cc

import android.app.Application
import com.novack.lotto_cc.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class LottoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR) // DI failures show up in Logcat
            androidContext(this@LottoApplication)
            modules(appModule)
        }
    }
}
