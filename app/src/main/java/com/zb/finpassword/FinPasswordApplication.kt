package com.zb.finpassword

import android.app.Application
import com.zb.finpassword.di.module.viewModelModule
import com.zb.smartsaving.di.module.appModule
import com.zb.smartsaving.di.module.repoModule
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.app_locale.SharedPrefsAppLocaleRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class FinPasswordApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FinPasswordApplication)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
        AppLocale.appLocaleRepository = SharedPrefsAppLocaleRepository(this)

    }

}