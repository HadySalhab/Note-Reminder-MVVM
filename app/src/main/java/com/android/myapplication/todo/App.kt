package com.android.myapplication.todo

import android.app.Application
import com.android.myapplication.todo.data.db.NotesDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App :Application(){
    private val koinModule= module{
        single {
            //Database
            NotesDataBase.getInstance(androidContext())
        }

    }


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModule)
        }
    }
}