package com.novack.lotto_cc

import android.app.Application
import com.novack.lotto_cc.di.appModule
import org.koin.core.context.startKoin

class LottoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule) }
    }
}
