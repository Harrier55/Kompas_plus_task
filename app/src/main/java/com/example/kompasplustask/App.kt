package com.example.kompasplustask

import android.app.Application
import com.example.kompasplustask.di.dataModule
import com.example.kompasplustask.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // init Koin
        startKoin {
            androidContext(this@App)
            modules(dataModule, viewModelModule)
        }
    }
}